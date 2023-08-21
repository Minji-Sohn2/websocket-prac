package com.example.websocketprac.dto;
import com.example.websocketprac.service.ChatService;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;

@Getter
public class ChatRoom {
    private String roomId;
    private String name;

    // 입장한 클라이언트들의 session 정보
    private Set<WebSocketSession> sessions = new HashSet<>();

    /* 생성자 */
    @Builder
    public ChatRoom(String roomId, String name) {
        this.roomId = roomId;
        this.name = name;
    }

    public void handleActions(WebSocketSession session, ChatMessage chatMessage, ChatService chatService) {
        // 채팅방에 입장했을 때
        if (chatMessage.getType().equals(ChatMessage.MessageType.ENTER)) {
            sessions.add(session);
            chatMessage.setMessage(chatMessage.getSender() + "님이 입장했습니다.");
        }

        sendMessage(chatMessage, chatService);
    }

    public <T> void sendMessage(T message, ChatService chatService) {
        // 채팅방의 모든 클라이언트에게 메세지 전송
        sessions.parallelStream().forEach(session -> chatService.sendMessage(session, message));
    }
}