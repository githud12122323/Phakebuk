package com.example.phakebukvip.controller;

import com.example.phakebukvip.model.Like;
import com.example.phakebukvip.model.Post;
import com.example.phakebukvip.model.User;
import com.example.phakebukvip.repository.LikeRepository;
import com.example.phakebukvip.repository.PostRepository;
import com.example.phakebukvip.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;



@Controller
@RequiredArgsConstructor
public class LikeController {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @PostMapping("/post/{postId}/like")
    public String likePost(@PathVariable Long postId, Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username).orElseThrow();
        Post post = postRepository.findById(postId).orElseThrow();

        // Check if user already liked the post
        boolean alreadyLiked = post.getLikes().stream()
                .anyMatch(like -> like.getUser().getId().equals(user.getId()));

        if (!alreadyLiked) {
            Like like = new Like();
            like.setPost(post);
            like.setUser(user);
            likeRepository.save(like);
        }

        return "redirect:/home#post-" + postId;

    }
}
