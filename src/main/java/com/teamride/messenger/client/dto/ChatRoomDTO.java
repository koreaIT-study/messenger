package com.teamride.messenger.client.dto;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.web.socket.WebSocketSession;

import lombok.Data;

@Data
public class ChatRoomDTO {
    private String roomId; //pk
    private String roomName;
    private String isGroup;
    
    private List<String> userId;

    private Set<WebSocketSession> socketSessions = new HashSet<>();
}
