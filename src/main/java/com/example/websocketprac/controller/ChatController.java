package com.example.websocketprac.controller;

import com.example.websocketprac.dto.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
    publisher 구현 (WebSocketChatHandler -> ChatController)
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/chat")
@Slf4j(topic = "채팅방 생성/ 조회")
public class ChatController {

    private final SimpMessageSendingOperations messagingTemplate;

    /*
        @MessageMapping -> websocket 으로 들어오는 메세지 발행 처리
     */
    @MessageMapping("/chat/message")
    public void message(ChatMessage message) {
        // 1. 클라이언트 - prefix 붙여 "/pub/chat/message"로 발행 요청

        // 입장 메세지일 경우
        if (ChatMessage.MessageType.ENTER.equals(message.getType())) {
            message.setMessage(message.getSender() + "님이 입장하셨습니다.");
        }

        // 2. "/sub/chat/room/{roomId}"로 메세지 발송
        // 클라이언트 : "/sub/chat/room/{roomId}" 를 구독하고 있다가 메세지가 전달되면 화면에 출력
        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }
}
