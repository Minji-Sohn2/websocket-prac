package com.example.websocketprac.service;

import com.example.websocketprac.dto.ChatRoom;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.*;

@Slf4j(topic = "ChatService")
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ObjectMapper objectMapper;

    // 생성된 모든 채팅방 정보 roomId - ChatRoom
    private Map<String, ChatRoom> chatRooms;

    @PostConstruct
    private void init() {
        chatRooms = new LinkedHashMap<>();
    }

    /**
     * 모든 채팅방 조회
     *
     * @return 채팅방 list
     */
    public List<ChatRoom> findAllRoom() {
        return new ArrayList<>(chatRooms.values());
    }

    /**
     * 채팅방 조회
     *
     * @param roomId 조회할 채팅방 id
     * @return 채팅방
     */
    public ChatRoom findRoomById(String roomId) {
        return chatRooms.get(roomId);
    }

    /**
     * 채팅방 생성
     *
     * @param name 생성할 채팅방 이름
     * @return 채팅방
     */
    public ChatRoom createRoom(String name) {
        String randomId = UUID.randomUUID().toString();
        ChatRoom chatRoom = ChatRoom.builder()
                .roomId(randomId)
                .name(name)
                .build();

        chatRooms.put(randomId, chatRoom);
        return chatRoom;
    }

    /**
     * 메세지 발송
     *
     * @param session 목적지 session
     * @param message 발송할 message
     * @param <T>     type
     */
    public <T> void sendMessage(WebSocketSession session, T message) {
        try {
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
