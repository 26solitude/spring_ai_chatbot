package org.example.spring_ai_chatbot.controller;

import org.example.spring_ai_chatbot.config.Config;
import org.example.spring_ai_chatbot.entity.ChatMessage;
import org.example.spring_ai_chatbot.repository.ChatMessageRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class AIController {

    private final ChatClient chatClient;
    private final ChatMessageRepository chatMessageRepository;

    AIController(ChatClient chatClient, ChatMessageRepository chatMessageRepository) {
        this.chatClient = chatClient;
        this.chatMessageRepository = chatMessageRepository;
    }

    @GetMapping("/ai/chat/history")
    public List<ChatMessage> getChatHistory(@RequestParam String userId) {
        return chatMessageRepository.findByUserIdOrderByTimestampAsc((userId));
    }

    @PostMapping("/ai/chat")
    public Map<String, String> sendMessage(@RequestParam String userId, @RequestBody Map<String, String> request) {
        ChatClient.ChatClientRequestSpec prompt = chatClient.prompt().system(Config.DEFAULT_PROMPT);
        String userInput = request.get("userInput");
        String aiResponse = prompt.user(userInput).call().content();

        chatMessageRepository.save(new ChatMessage(userId, "user", userInput));
        chatMessageRepository.save(new ChatMessage(userId, "assistant", aiResponse));

        return Map.of("role", "assistant", "content", aiResponse);
    }
}

