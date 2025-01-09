package org.example.spring_ai_chatbot.controller;

import org.example.spring_ai_chatbot.config.Config;
import org.example.spring_ai_chatbot.entity.ChatMessage;
import org.example.spring_ai_chatbot.repository.ChatMessageRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.UserMessage;
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

    @GetMapping("/ai/chat")
    public Map<String, String> completion(@RequestParam(value = "message", defaultValue = "농담 하나 해보거라") String message) {
        return Map.of("달건이", this.chatClient.prompt().user(message).call().content());
    }

    @PostMapping("/ai/chat")
    public String sendMessage(@RequestParam String userId, @RequestBody String userInput) {

        List<ChatMessage> history = chatMessageRepository.findByUserIdOrderByTimestampAsc(userId);

        ChatClient.ChatClientRequestSpec prompt = chatClient.prompt().system(Config.DEFAULT_PROMPT);

        for (var chat : history) {
            if ("user".equals(chat.getRole())) {
                prompt.messages(new UserMessage(chat.getContent()));
            } else if ("assistant".equals(chat.getRole())) {
                prompt.messages(new AssistantMessage(chat.getContent()));
            }
        }

        String aiResponse = prompt.user(userInput).call().content();

        chatMessageRepository.save(new ChatMessage(userId, "user", userInput));
        chatMessageRepository.save(new ChatMessage(userId, "assistant", aiResponse));

        return aiResponse;
    }
}

