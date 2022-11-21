package com.teamride.messenger.client.controller;

import com.teamride.messenger.client.dto.ChatRoomDTO;
import com.teamride.messenger.client.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping(value = "/chat")
@Slf4j
@RestController
public class RoomController {

    private final ChatRoomRepository chatRoomRepository;

    @GetMapping(value = "/rooms")
    public List<ChatRoomDTO> rooms(){
        log.info("# All Chat Rooms");
        return chatRoomRepository.findAllRooms();
    }

    @PostMapping(value = "/room")
    public ChatRoomDTO create(@RequestBody ChatRoomDTO room){
        log.info("# Create Chat Room , name: " + room);
        log.info("chatRoomRepository : {}" , chatRoomRepository);
        return chatRoomRepository.createChatRoomDTO(room);
    }

    @GetMapping(value = "/room")
    public ChatRoomDTO getRoom(String roomId){
        log.info("# get Chat Room, roomID : " + roomId);
        return chatRoomRepository.findRoomById(roomId);
    }
}
