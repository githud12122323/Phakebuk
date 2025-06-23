package com.example.phakebukvip.controller;

import com.example.phakebukvip.model.Message;
import com.example.phakebukvip.model.User;
import com.example.phakebukvip.repository.MessageRepository;
import com.example.phakebukvip.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatPageController {

    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    @GetMapping("/chat/{username}")
    public String chatWithUser(@PathVariable String username, Model model, Principal principal) {
        User currentUser = userRepository.findByUsername(principal.getName()).orElseThrow();
        User otherUser = userRepository.findByUsername(username).orElseThrow();

        List<Message> messages = messageRepository
                .findBySenderAndRecipientOrRecipientAndSenderOrderBySentAtAsc(currentUser, otherUser, currentUser, otherUser);

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("otherUser", otherUser);
        model.addAttribute("messages", messages);
        return "chat";
    }
}
