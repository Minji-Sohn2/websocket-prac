package com.example.websocketprac.controller;

import com.example.websocketprac.config.RedisPublisher;
import com.example.websocketprac.dto.ChatMessage;
import com.example.websocketprac.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
    publisher 구현 (WebSocketChatHandler -> ChatController)
 */
@RequiredArgsConstructor
@Controller
@Slf4j(topic = "ChatController")
public class ChatController {

    private final RedisPublisher redisPublisher;
    private final ChatRoomRepository chatRoomRepository;

    /*
        Websocket "/pub/chat/message"로 들어오는 메세지 처리
     */
    @MessageMapping("/chat/message")
    public void message(ChatMessage message) {
        // 입장 메세지일 경우
        if (ChatMessage.MessageType.ENTER.equals(message.getType())) {
            chatRoomRepository.enterChatRoom(message.getRoomId());
            message.setMessage(message.getSender() + "님이 입장하셨습니다.");
        }

        // Websocket 에 발행된 메세지 redis 로 발행 (publish)
        redisPublisher.publish(chatRoomRepository.getTopic(message.getRoomId()), message);
    }
}
