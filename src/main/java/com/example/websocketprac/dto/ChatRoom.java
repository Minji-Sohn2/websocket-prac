package com.example.websocketprac.dto;

import lombok.Getter;

import java.util.UUID;
/*
    pub/sub 방식을 이용하면 구독자 관리 자동으로 가능
    -> 웹소켓 세션 관리 필요 X
    발송 구현도 자동으로 가능
    -> 클라이언트에게 메세지를 발송하는 구현 필요 X
 */
@Getter
public class ChatRoom {

    private String roomId;
    private String name;

    public static ChatRoom create(String name) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.roomId = UUID.randomUUID().toString();
        chatRoom.name = name;
        return chatRoom;
    }
}