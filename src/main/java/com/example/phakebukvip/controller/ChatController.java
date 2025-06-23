package com.example.phakebukvip.controller;

import com.example.phakebukvip.dto.ChatMessage;
import com.example.phakebukvip.model.Message;
import com.example.phakebukvip.model.User;
import com.example.phakebukvip.repository.MessageRepository;
import com.example.phakebukvip.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.time.LocalDateTime;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessage chatMessage, Principal principal) {
        // Gửi WebSocket
        messagingTemplate.convertAndSendToUser(
                chatMessage.getRecipient(), "/queue/messages", chatMessage
        );
        chatMessage.setTimestamp(LocalDateTime.now().toString());

        // Lưu vào DB
        Message msg = new Message();
        User sender = userRepository.findByUsername(principal.getName()).orElseThrow();
        User recipient = userRepository.findByUsername(chatMessage.getRecipient()).orElseThrow();
        msg.setSender(sender);
        msg.setRecipient(recipient);
        msg.setContent(chatMessage.getContent());
        msg.setSentAt(LocalDateTime.now());

        messageRepository.save(msg);
    }
}
