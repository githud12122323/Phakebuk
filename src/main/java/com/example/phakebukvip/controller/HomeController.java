package com.example.phakebukvip.controller;

import com.example.phakebukvip.model.Follow;
import com.example.phakebukvip.model.Post;
import com.example.phakebukvip.model.User;
import com.example.phakebukvip.repository.FollowRepository;
import com.example.phakebukvip.repository.PostRepository;
import com.example.phakebukvip.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final FollowRepository followRepository;


    @GetMapping("/home")
    public String showHomePage(Model model, Principal principal) {
        String username = principal.getName();
        User currentUser = userRepository.findByUsername(username).orElseThrow();

        List<Post> posts = postRepository.findAllByOrderByCreatedAtDesc();

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        for (Post post : posts) {
            if (post.getCreatedAt() != null) {
                post.setFormattedDate(formatter.format(post.getCreatedAt()));
            }
        }

        List<Follow> followings = followRepository.findByFollower(currentUser);
        List<User> followedUsers = followings.stream()
                .map(Follow::getFollowing)
                .toList();

        model.addAttribute("user", currentUser);
        model.addAttribute("posts", posts);
        model.addAttribute("followedUsers", followedUsers); // 👈 để hiển thị danh sách bạn follow
        List<String> followedUsernames = followedUsers.stream()
                .map(User::getUsername)
                .toList();

        model.addAttribute("followedUsernames", followedUsernames);
        return "home";
    }

}