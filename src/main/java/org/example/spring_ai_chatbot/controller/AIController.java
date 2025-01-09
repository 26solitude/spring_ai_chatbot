package org.example.spring_ai_chatbot.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AIController {

    private final ChatClient chatClient;

    AIController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/ai/chat")
    public Map<String, String> completion(@RequestParam(value = "message", defaultValue = "농담 하나 해보거라") String message) {
        return Map.of("달건이", this.chatClient.prompt().user(message).call().content());
    }

}

