package com.example.websocketprac.service;

import com.example.websocketprac.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Slf4j(topic = "StompHandler")
@RequiredArgsConstructor
@Component
public class StompHandler implements ChannelInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    /*
        Websocket 을 통해 들어온 요청 처리하기 전
        jwt token 유효성 검증
     */
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        // websocket 연결 시
        if(StompCommand.CONNECT == accessor.getCommand()) {
            jwtTokenProvider.validateToken(accessor.getFirstNativeHeader("token"));
        }

        return message;
    }
}
