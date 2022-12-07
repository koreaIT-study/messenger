package com.teamride.messenger.client.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.annotation.Resource;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.teamride.messenger.client.config.KafkaConstants;
import com.teamride.messenger.client.dto.ChatMessageDTO;
import com.teamride.messenger.client.dto.ChatRoomDTO;
import com.teamride.messenger.client.repository.ChatRoomRepository;
import com.teamride.messenger.client.service.StompChatService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StompChatController {
    // topic : user
    // partition : room id
    @Autowired
    private WebClient webClient;

    @Resource(name = "stompChatService")
    private StompChatService stompChatService;
    
    private final ChatRoomRepository chatRoomRepository;

    private final KafkaTemplate<String, ChatMessageDTO> kafkaTemplate;

    private static final String topicName = "chat-client";

    // 채팅방 처음 만들때 logic
    // 친구 두번 클릭하면
    // db에서 chat room이 있는지 select하고
    // 없으면 uuid로 생성
    // 있으면 /chat/enter logic 하면 되고

    // Client 가 SEND 할 수 있는 경로
    @MessageMapping(value = "/chat/enter")
    public void enter(ChatMessageDTO message) {
        log.info("::: StompChatController.enter in :::");

        stompChatService.enter(message);

        // 기존에 있던 채팅방에 들어가는
        // 기존 message select
        // view 뿌려주기

        List<ChatMessageDTO> resp = webClient.mutate()
            .build()
            .get()
            .uri("/get-chat-message?roomId=" + message.getRoomId())
            .retrieve()
            .bodyToFlux(ChatMessageDTO.class).collectList().block();

//        resp.block()
//            .forEach(c -> {
//                // message 받았으니 view에 뿌려주면 됨
//                log.info(c.toString());
//            });

    }

    @MessageMapping(value = "/chat/message")
    public void message(ChatMessageDTO message) {
        log.info("::: StompChatController.message in :::" + message);
        // view에서 message 보내기 누르면 들어옴
        // service 호출
        kafkaTemplate.send(KafkaConstants.CHAT_SERVER, message);

    }

    @GetMapping("/sendMessage")
    public void sendMessage() {
        ChatMessageDTO message = ChatMessageDTO.builder()
            // .roomId(UUID.randomUUID().toString())
            .roomId("f8c67dc3ae0a3265")
            .writer("hong")
            .message("test message 입니다2222~")
            .timestamp(LocalDateTime.now()
                .toString())
            .build();

        // roomId를 가진 사용자들(톡방 안의 users)을 검색해서
        // 반복문으로 send해주는 작업

        // kafkaTemplate.send(message.getRoomId(), message);
        ListenableFuture<SendResult<String, ChatMessageDTO>> listenableFuture = kafkaTemplate
            .send(KafkaConstants.CHAT_SERVER, message);
        listenableFuture.addCallback(new ListenableFutureCallback<SendResult<String, ChatMessageDTO>>() {

            @Override
            public void onSuccess(SendResult<String, ChatMessageDTO> result) {
                // TODO Auto-generated method stub
                ProducerRecord<String, ChatMessageDTO> record = result.getProducerRecord();
                ChatMessageDTO chatMessageDTO = record.value();

            }

            @Override
            public void onFailure(Throwable ex) {
                throw new RuntimeException("kafka 전송 error", ex);
            }
        });
    }

    @KafkaListener(topics = KafkaConstants.CHAT_CLIENT, groupId = KafkaConstants.GROUP_ID)
    public void listen(ChatMessageDTO message, Acknowledgment ack) {
        log.info("Received Msg chat-client " + message);

        // message 받음

        // message의 room id확인

        // 사용자들이 room id를 구독하고 있어서
        // room id 에대한 user id 조회 logic 필요 없음
        try {

            stompChatService.sendMessage(message);
            log.info("message:::"+message);
            ChatRoomDTO chatRoomDTO = chatRoomRepository.findRoomById(message.getRoomId());
            log.info("chatRoom DTO::"+chatRoomDTO);
            stompChatService.sendMessageRoomList(chatRoomDTO);
            ack.acknowledge();
        } catch (Exception e) {
            log.error("error::{}", e);
        }

        // topic : user id

    }
}
