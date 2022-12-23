package com.teamride.messenger.client.controller;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teamride.messenger.client.config.Constants;
import com.teamride.messenger.client.dto.ChatRoomDTO;
import com.teamride.messenger.client.repository.ChatRoomRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RequestMapping(value = "/chat")
@Slf4j
@RestController
public class RoomController {

    private final ChatRoomRepository chatRoomRepository;
    private final HttpSession httpSession;

    @GetMapping(value = "/getRoomList")
    public Flux<ChatRoomDTO> rooms() {
        log.info("# All Chat Rooms");
        int userId = (int) httpSession.getAttribute(Constants.LOGIN_SESSION);
        return chatRoomRepository.findAllRooms(userId);
    }

    @PostMapping(value = "/room")
    public Mono<ChatRoomDTO> create(@RequestBody ChatRoomDTO room) {
        return chatRoomRepository.createChatRoomDTO(room)
            .doOnSuccess(r -> log.info("# Create Chat Room :: {}", r));
    }

    @GetMapping(value = "/room")
    public Mono<ChatRoomDTO> getRoom(String roomId) {
        return chatRoomRepository.getRoom(roomId)
            .doOnSuccess(r -> log.info("get Chat Room dto:: {}", r));
    }

}
