package com.teamride.messenger.client.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.teamride.messenger.client.dto.ChatMessageDTO;
import com.teamride.messenger.client.utils.RestResponse;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@RestController
@Slf4j
public class ServerConnectController {
    @Autowired
    private WebClient webClient;

    @Autowired
    private HttpSession httpSession;

    @GetMapping("/get-chat-message/{roomId}")
    public RestResponse getChatMessage(@PathVariable("roomId") String roomId) {
        // Mono<List<ChatMessageDTO>> resp = webClient.mutate()
        // .build()
        // .get()
        // .uri("/get-chat-message?roomId=" + roomId)
        // .retrieve()
        // .bodyToMono(new ParameterizedTypeReference<List<ChatMessageDTO>>() {
        // });

        Flux<ChatMessageDTO> resp = webClient.mutate()
            .build()
            .get()
            .uri("/get-chat-message?roomId=" + roomId)
            .retrieve()
            .bodyToFlux(ChatMessageDTO.class);

        List<ChatMessageDTO> messageList = resp.collectList()
            .block();
        return new RestResponse(messageList);
    }

}
