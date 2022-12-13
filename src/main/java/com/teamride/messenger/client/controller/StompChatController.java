package com.teamride.messenger.client.controller;

import javax.annotation.Resource;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;
import com.teamride.messenger.client.config.KafkaConstants;
import com.teamride.messenger.client.dto.ChatMessageDTO;
import com.teamride.messenger.client.dto.ChatRoomDTO;
import com.teamride.messenger.client.repository.ChatRoomRepository;
import com.teamride.messenger.client.service.StompChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StompChatController {

	@Resource(name = "stompChatService")
	private StompChatService stompChatService;

	private final ChatRoomRepository chatRoomRepository;

	private final KafkaTemplate<String, ChatMessageDTO> kafkaTemplate;

	@MessageMapping(value = "/chat/message")
	public void message(ChatMessageDTO message) {
		log.info("::: StompChatController.message in :::" + message);
		// view에서 message 보내기 누르면 들어옴
		// service 호출
		kafkaTemplate.send(KafkaConstants.CHAT_SERVER, message);

	}

	@KafkaListener(topics = KafkaConstants.CHAT_CLIENT, groupId = KafkaConstants.GROUP_ID)
	public void listen(ChatMessageDTO message, Acknowledgment ack) {
		log.info("Received Msg chat-client " + message);

		// message 받음
		// 사용자들이 room id를 구독하고 있어서
		// room id 에대한 user id 조회 logic 필요 없음
		try {
			stompChatService.sendMessage(message);
			log.info("message:::" + message);
			ChatRoomDTO chatRoomDTO = chatRoomRepository.findRoomById(message.getRoomId());
			log.info("chatRoom DTO::" + chatRoomDTO);
			stompChatService.sendMessageRoomList(chatRoomDTO);
			ack.acknowledge();
		} catch (Exception e) {
			log.error("error::{}", e);
		}
	}
}
