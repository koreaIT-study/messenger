package com.teamride.messenger.client.handler;

import java.util.HashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ChatHandler extends TextWebSocketHandler {
	private static HashMap<String, WebSocketSession> sessionMap = new HashMap<>();

	protected void handlerTextMessage(WebSocketSession session, TextMessage msg) throws Exception {
		String payload = msg.getPayload();
		log.info("payload :: " + payload);

		sessionMap.get("key").sendMessage(msg);
	}

	public void afterConnectionEstablished(WebSocketSession session) throws Exception {

		sessionMap.put("key", session);

		log.info(session + " 클라이언트 접속");
	}

	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

		log.info(session + " 클라이언트 접속 해제");
		sessionMap.remove("key");
	}
}