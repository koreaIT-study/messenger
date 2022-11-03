package com.teamride.messenger.client.service;

import com.teamride.messenger.client.dto.ChatMessageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service(value = "stompChatService")
@RequiredArgsConstructor
@Slf4j
public class StompChatService {
    // 특정 Broker로 메세지를 전달
    private final SimpMessagingTemplate template;

    private final String destination = "/sub/chat/room/";


    public void sendMessage(ChatMessageDTO message){
        template.convertAndSend(destination + message.getRoomId(), message);
    }

    public void enter(ChatMessageDTO message){
        log.info("::: StompChatService.enter in :::");
        message.setMessage(message.getWriter() + "님이 채팅빙에 입장하였습니다.");
        sendMessage(message);
    }

}
