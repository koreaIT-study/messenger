package com.teamride.messenger.client.controller;

import com.teamride.messenger.client.dto.ChatMessageDTO;
import com.teamride.messenger.client.service.StompChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

@Controller
@RequiredArgsConstructor
@Slf4j
public class StompChatController {

    @Resource(name = "stompChatService")
    private StompChatService stompChatService;

    //Client 가 SEND 할 수 있는 경로
    @MessageMapping(value = "/chat/enter")
    public void enter(ChatMessageDTO message){
        log.info("::: StompChatController.enter in :::");
        stompChatService.enter(message);
    }

    @MessageMapping(value = "/chat/message")
    public void message(ChatMessageDTO message){
        log.info("::: StompChatController.message in :::" + message);
        stompChatService.sendMessage(message);
    }
}
