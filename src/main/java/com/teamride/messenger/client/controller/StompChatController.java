package com.teamride.messenger.client.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.http.MediaType;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.teamride.messenger.client.config.KafkaConstants;
import com.teamride.messenger.client.dto.ChatMessageDTO;
import com.teamride.messenger.client.dto.ChatRoomDTO;
import com.teamride.messenger.client.repository.ChatRoomRepository;
import com.teamride.messenger.client.service.StompChatService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.util.List;

@EnableAsync
@RestController
@RequiredArgsConstructor
@Slf4j
public class StompChatController {

    @Resource(name = "stompChatService")
    private StompChatService stompChatService;

    private final ChatRoomRepository chatRoomRepository;

    private final KafkaTemplate<String, ChatMessageDTO> kafkaTemplate;

    private final HttpSession httpSession;

    @MessageMapping("/chat/input")
    public void chatInput(ChatMessageDTO messageDTO) {
        kafkaTemplate.send(KafkaConstants.CHAT_INPUT, messageDTO); // 순서 보장 필요없을 듯, partition 100개로 RR(Round Robin)
    }

    @KafkaListener(topics = KafkaConstants.CHAT_INPUT, groupId = KafkaConstants.GROUP_ID)
    public void listenInput(ChatMessageDTO chatMessageDTO, Acknowledgment ack) {
        log.info("Received Msg chat-input {}", chatMessageDTO);

        try {
            stompChatService.sendChatInput(chatMessageDTO);
            ack.acknowledge();
        } catch (Exception e) {
            log.error("error::{}", e);
        }
    }

    @MessageMapping(value = "/chat/message")
    public void message(ChatMessageDTO message) {
        log.info("::: StompChatController.message in :::" + message);
        // view에서 message 보내기 누르면 들어옴
        // service 호출
        String partitionKey = message.getRoomId()
            .substring(0, 2);
        ListenableFuture<SendResult<String, ChatMessageDTO>> future = kafkaTemplate.send(KafkaConstants.CHAT_SERVER,
                partitionKey, message);

        future.addCallback((result) -> {
            int partition = result.getRecordMetadata()
                .partition();
            log.info("message 전송 성공, message :: {}, partition num is {},  result is :: {}", message, partition, result);
        }, (ex) -> {
            log.error("message 전송 실패, message :: {}, error is :: {}", message, ex);
        });
    }

    @KafkaListener(topics = KafkaConstants.CHAT_CLIENT, groupId = KafkaConstants.GROUP_ID)
    public void listen(ChatMessageDTO message, Acknowledgment ack) {
        log.info("Received Msg chat-client " + message);

        // message 받음
        // 사용자들이 room id를 구독하고 있어서
        // room id 에대한 user id 조회 logic 필요 없음
        try {
            stompChatService.sendMessage(message);
            log.info("message:::" + message);

            Mono<ChatRoomDTO> monoChatRoomDTO = chatRoomRepository.findRoomById(message.getRoomId());
            monoChatRoomDTO.subscribe(room -> {
                log.info("chatRoom DTO::" + room);
                stompChatService.sendMessageRoomList(room);
            });
            ack.acknowledge();
        } catch (Exception e) {
            log.error("error::{}", e);
        }
    }

    @PostMapping(value = "/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void fileUpload(@RequestParam(value = "files", required = false) MultipartFile[] files){
        for (MultipartFile file: files) {
            log.info("file name :::: {}",file.getOriginalFilename());
        }
    }
}
