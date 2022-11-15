package com.teamride.messenger.client.controller;

import com.teamride.messenger.client.config.KafkaConstants;
import com.teamride.messenger.client.dto.ChatMessageDTO;
import com.teamride.messenger.client.service.StompChatService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.annotation.Resource;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StompChatController {
    // topic : user
    // partition : room id 
    
    @Resource(name = "stompChatService")
    private StompChatService stompChatService;

    private final KafkaTemplate<String, ChatMessageDTO> kafkaTemplate;
    
    private static final String topicName = "f8c67dc3ae0a3265";
    
    //Client 가 SEND 할 수 있는 경로
    @MessageMapping(value = "/chat/enter")
    public void enter(ChatMessageDTO message){
        log.info("::: StompChatController.enter in :::");
        stompChatService.enter(message);
    }

    @MessageMapping(value = "/chat/message")
    public void message(ChatMessageDTO message){
        log.info("::: StompChatController.message in :::" + message);
        stompChatService.sendMessage(message);
    }
    
    @GetMapping("/sendMessage")
    public void sendMessage() {
        ChatMessageDTO message = ChatMessageDTO.builder()
//                .roomId(UUID.randomUUID().toString())
                .roomId("f8c67dc3ae0a3265")
                .writer("hong")
                .message("test message 입니다~")
                .timestamp(LocalDateTime.now().toString())
                .build();
        
        // roomId를 가진 사용자들(톡방 안의 users)을 검색해서
        // 반복문으로 send해주는 작업
        
        kafkaTemplate.send(message.getRoomId(), message);
        
    }
    

    @KafkaListener(topics = topicName, groupId = KafkaConstants.GROUP_ID)
    public void listen(ChatMessageDTO message) {
        log.info("Received Msg f8c67dc3ae0a3265 " + message);
        // message의 room id확인
        // topic : user id
        
        
        
    }
}
