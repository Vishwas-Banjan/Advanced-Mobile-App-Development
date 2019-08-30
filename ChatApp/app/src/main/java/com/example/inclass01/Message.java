package com.example.inclass01;

import com.google.firebase.Timestamp;

import java.util.ArrayList;

public class Message {

    String messageId;
    String messageText;
    String messageTimeStamp;
    String userId;
    String userName;
    String chatRoomId;
    ArrayList<String> upvotedBy;

    public Message() {
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageId='" + messageId + '\'' +
                ", messageText='" + messageText + '\'' +
                ", messageTimeStamp=" + messageTimeStamp +
                ", userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
