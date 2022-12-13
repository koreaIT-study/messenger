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
	public ChatRoomDTO create(@RequestBody ChatRoomDTO room) {
		ChatRoomDTO chatRoomDTO = chatRoomRepository.createChatRoomDTO(room);
		log.info("# Create Chat Room :: " + chatRoomDTO);
		return chatRoomDTO;
	}

	@GetMapping(value = "/room")
	public ChatRoomDTO getRoom(String roomId) {
		log.info("# get Chat Room, roomID : " + roomId);
		ChatRoomDTO chatRoomDTO = chatRoomRepository.getRoom(roomId);
		log.info("chatRoom dto::" + chatRoomDTO);
		return chatRoomDTO;
	}

}
