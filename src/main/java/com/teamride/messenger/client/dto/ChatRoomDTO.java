package com.teamride.messenger.client.dto;

import lombok.Data;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
public class ChatRoomDTO {

    private String roomId;
    private String roomName;
    private Set<WebSocketSession> socketSessions = new HashSet<>();

    public static ChatRoomDTO create(String roomName){
        ChatRoomDTO room = new ChatRoomDTO();

        room.setRoomId(UUID.randomUUID().toString());
        room.setRoomName(roomName);
        return room;
    }
}
