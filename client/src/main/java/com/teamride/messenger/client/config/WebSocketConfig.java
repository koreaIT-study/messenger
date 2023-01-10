package com.teamride.messenger.client.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

//    private final ChatHandler chatHandler;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/stomp/chat")
                .setAllowedOrigins("http://localhost:8081")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 클라이언트에서 SENd 요청을 처리
        config.setApplicationDestinationPrefixes("/pub");
        // 이 경로로 SimpleBroker를 등록.
        // SimpleBroker는 이 경로를 SUBSCRIBE 하는 클라이언트에게 메시지를 전달하는 작업 수행
        config.enableSimpleBroker("/sub");
    }
}
