package org.example.spring_ai_chatbot.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "chat_messages")
@Getter @Setter
public class ChatMessage {
    @Id
    private String id;
    private String userId;
    private String role;
    private String content;
    private long timestamp;

    public ChatMessage(String userId, String role, String content){
        this.userId = userId;
        this.role = role;
        this.content = content;
        this.timestamp = System.currentTimeMillis();
    }
}
