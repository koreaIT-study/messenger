package com.teamride.messenger.client.dto;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.web.socket.WebSocketSession;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomDTO {
    private String roomId; // pk
    private String roomName;
    private String message; // 마지막 MESSAGE
    private String time;
    private int cnt;
    private String isGroup;
    private List<String> userId;

}
