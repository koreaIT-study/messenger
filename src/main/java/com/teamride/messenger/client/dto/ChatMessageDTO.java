package com.teamride.messenger.client.dto;

import java.util.concurrent.atomic.AtomicLong;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDTO {
    private String roomId; // fk
    private String writer; // writer id
    private String message;
    private String timestamp; //  보낸 시간
    private String writerName;
    
    // timestamp + roomId = pk
}
