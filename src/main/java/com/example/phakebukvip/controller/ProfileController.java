package com.example.phakebukvip.controller;

import com.example.phakebukvip.model.Post;
import com.example.phakebukvip.model.User;
import com.example.phakebukvip.repository.FollowRepository;
import com.example.phakebukvip.repository.PostRepository;
import com.example.phakebukvip.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class ProfileController {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final FollowRepository followRepository;


    @GetMapping("/profile/{username}")
    public String showUserProfile(@PathVariable String username, Model model, Principal principal) {
        User profileUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        User currentUser = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người đăng nhập"));

        List<Post> posts = postRepository.findByUserOrderByCreatedAtDesc(profileUser);

        boolean isFollowing = followRepository.existsByFollowerAndFollowing(currentUser, profileUser);

        model.addAttribute("profileUser", profileUser);
        model.addAttribute("user", currentUser);
        model.addAttribute("posts", posts);
        model.addAttribute("isFollowing", isFollowing);

        return "profile";
    }
    @PostMapping("/profile/update")
    public String updateProfile(@RequestParam("avatar") MultipartFile avatar,
                                Principal principal) throws IOException {
        if (!avatar.isEmpty()) {
            String fileName = UUID.randomUUID() + "_" + avatar.getOriginalFilename();
            String uploadDir = new File("src/main/resources/static/Avts").getAbsolutePath();
            File folder = new File(uploadDir);
            if (!folder.exists()) folder.mkdirs();

            Path path = Paths.get(uploadDir, fileName);
            Files.copy(avatar.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            User user = userRepository.findByUsername(principal.getName()).orElseThrow();
            user.setAvatar("/static/uploads/" + fileName);
            userRepository.save(user);
        }

        return "redirect:/profile/" + principal.getName();
    }

}