package com.teamride.messenger.client.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.teamride.messenger.client.dto.ChatMessageDTO;
import com.teamride.messenger.client.dto.ChatRoomDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service(value = "stompChatService")
@RequiredArgsConstructor
@Slf4j
public class StompChatService {
    // 특정 Broker로 메세지를 전달
    private final SimpMessagingTemplate template;
    // 특정 채팅방 목적지
    private final static String destination = "/sub/chat/room/";

    // 채팅방 목록 목적지
    private final static String destinationRoomList = "/sub/chat/roomList/";

    public void sendMessage(ChatMessageDTO message) {
        template.convertAndSend(destination + message.getRoomId(), message);
    }

    public void sendMessageRoomList(ChatRoomDTO message) {
        log.info("message~~~~~~~~~~"+message);
        message.getUserId()
            .forEach(userId -> {
                template.convertAndSend(destinationRoomList + userId, message);
            });
    }

    public void enter(ChatMessageDTO message) {
        log.info("::: StompChatService.enter in :::");
        message.setMessage(message.getWriter() + "님이 채팅빙에 입장하였습니다.");
        sendMessage(message);
    }

}
