package com.teamride.messenger.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDTO {
//	private String messageId; // pk
    private String roomId; // fk
    private String writer;
    private String message;
    private String timestamp; //  보낸 시간
    
    // timestamp + roomId = pk
}
