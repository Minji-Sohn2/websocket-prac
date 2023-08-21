package com.example.websocketprac.config;

import com.example.websocketprac.dto.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RedisPublisher {

    private final RedisTemplate<String, Object> redisTemplate;

    /*
        채팅방에 입장 후 메세지 작성
        -> 해당 메세지 Redis Topic에 발행
        -> 대기하고 있던 redis 구독 서비스가 메세지 처리
     */
    public void publish(ChannelTopic topic, ChatMessage message) {
        redisTemplate.convertAndSend(topic.getTopic(), message);
    }
}
