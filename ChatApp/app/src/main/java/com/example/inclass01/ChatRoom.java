package com.example.inclass01;

public class ChatRoom {
    String roomName, roomId;

    public ChatRoom(String roomName, String roomId) {
        this.roomName = roomName;
        this.roomId = roomId;
    }

    @Override
    public String toString() {
        return "ChatRoom{" +
                "roomName='" + roomName + '\'' +
                ", roomId='" + roomId + '\'' +
                '}';
    }
}
