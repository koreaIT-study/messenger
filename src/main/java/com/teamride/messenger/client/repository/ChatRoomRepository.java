package com.teamride.messenger.client.repository;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;

import com.teamride.messenger.client.dto.ChatRoomDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ChatRoomRepository {

    private final WebClient webClient;

    public Flux<ChatRoomDTO> findAllRooms(int userId) {
        Flux<ChatRoomDTO> resp = webClient.mutate()
            .build()
            .get()
            .uri("/get-room-list?userId=" + userId)
            .retrieve()
            .bodyToFlux(ChatRoomDTO.class);
        
        log.info("roomList::" + resp);
        return resp;
    }

    public Mono<ChatRoomDTO> findRoomById(String roomId) {
        // 이부분도 나중에 server 에 요청 보내서 데이터 가져오기
        return webClient.post()
                .uri("/find-room-by-id")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(roomId)
                .retrieve()
                .bodyToMono(ChatRoomDTO.class);
    }
    
    public ChatRoomDTO getRoom(String roomId) {
        // 이부분도 나중에 server 에 요청 보내서 데이터 가져오기
        return webClient.post()
                .uri("/get-room")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(roomId)
                .retrieve()
                .bodyToMono(ChatRoomDTO.class)
                .block();
    }

    public ChatRoomDTO createChatRoomDTO(ChatRoomDTO room) {
        // server 에 요청을 보내 생성된 채팅방 정보 저장
        return webClient.post()
            .uri("/room")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(room)
            .retrieve()
            .bodyToMono(ChatRoomDTO.class)
            .block();
    }
    
}
