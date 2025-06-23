package com.example.phakebukvip.controller;

import com.example.phakebukvip.model.Follow;
import com.example.phakebukvip.model.User;
import com.example.phakebukvip.repository.FollowRepository;
import com.example.phakebukvip.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class FollowController {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    @PostMapping("/follow/{username}")
    public String followUser(@PathVariable String username, Principal principal) {
        User currentUser = userRepository.findByUsername(principal.getName()).orElseThrow();
        User targetUser = userRepository.findByUsername(username).orElseThrow();

        if (!followRepository.existsByFollowerAndFollowing(currentUser, targetUser)) {
            Follow follow = new Follow();
            follow.setFollower(currentUser);
            follow.setFollowing(targetUser);
            followRepository.save(follow);
        }

        return "redirect:/profile/" + username;
    }

    @PostMapping("/unfollow/{username}")
    public String unfollowUser(@PathVariable String username, Principal principal) {
        User currentUser = userRepository.findByUsername(principal.getName()).orElseThrow();
        User targetUser = userRepository.findByUsername(username).orElseThrow();

        followRepository.findByFollowerAndFollowing(currentUser, targetUser)
                .ifPresent(followRepository::delete);

        return "redirect:/profile/" + username;
    }
}
