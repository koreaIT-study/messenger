package com.teamride.messenger.client.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;

import com.teamride.messenger.client.dto.ChatMessageDTO;
import com.teamride.messenger.client.dto.ChatRoomDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ChatRoomRepository {

    private final WebClient webClient;
    // 나중에 DB로 바꾸면 필요 없어짐
    private Map<String, ChatRoomDTO> chatRoomDTOMap;

    // 생성자가 호출되었을 때 bean을 초기화 시켜주는 어노테이션
    @PostConstruct
    private void init() {
        chatRoomDTOMap = new LinkedHashMap<>();
    }

    public List<ChatRoomDTO> findAllRooms(int userId) {
        Flux<ChatRoomDTO> resp = webClient.mutate()
            .build()
            .get()
            .uri("/get-room-list?userId=" + userId)
            .retrieve()
            .bodyToFlux(ChatRoomDTO.class);

        List<ChatRoomDTO> roomList = resp.collectSortedList((o1, o2) -> {
            return o1.getTime()
                .compareTo(o2.getTime());

        })
            .block();

        log.info("roomList::" + roomList);
        return roomList;
    }

    public ChatRoomDTO findRoomById(String roomId) {
        // 이부분도 나중에 server 에 요청 보내서 데이터 가져오기
        return chatRoomDTOMap.get(roomId);
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
