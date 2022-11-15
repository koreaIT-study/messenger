package com.teamride.messenger.client.dto;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.web.socket.WebSocketSession;

import lombok.Data;

@Data
public class ChatRoomDTO {

    private String roomId; //pk
    private String roomName;
    
    private List<String> userId;
    
    
    private Set<WebSocketSession> socketSessions = new HashSet<>();

    public static ChatRoomDTO create(String roomName){
        ChatRoomDTO room = new ChatRoomDTO();

        room.setRoomId(UUID.randomUUID().toString());
        room.setRoomName(roomName);
        return room;
    }
}
