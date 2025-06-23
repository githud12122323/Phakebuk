package com.example.phakebukvip.controller;

import com.example.phakebukvip.model.Post;
import com.example.phakebukvip.model.User;
import com.example.phakebukvip.repository.PostRepository;
import com.example.phakebukvip.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class SearchController {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @GetMapping("/search")
    public String search(@RequestParam("q") String keyword, Model model) {
        List<User> users = userRepository.findByFullNameContainingIgnoreCase(keyword);
        List<Post> posts = postRepository.findByContentContainingIgnoreCase(keyword);

        model.addAttribute("keyword", keyword);
        model.addAttribute("users", users);
        model.addAttribute("posts", posts);

        return "search_results";
    }
}
