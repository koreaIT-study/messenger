package com.teamride.messenger.client.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.teamride.messenger.client.dto.ChatMessageDTO;
import com.teamride.messenger.client.utils.RestResponse;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class ServerConnectController {
	@Autowired
	private WebClient webClient;
	
	@GetMapping("/get-chat-message")
	public void getChatMessage(String roomId) {
		Mono<List<ChatMessageDTO>> resp = webClient.mutate()
				.build()
				.get().uri("/get-chat-message?roomId=" + roomId)
				.retrieve().bodyToMono(new ParameterizedTypeReference<List<ChatMessageDTO>>() {
				});
		
		resp.block().forEach(c->{
			log.info(c.toString());
		});

	}
}
