package com.example.phakebukvip.controller;

import com.example.phakebukvip.model.Post;
import com.example.phakebukvip.model.User;
import com.example.phakebukvip.repository.PostRepository;
import com.example.phakebukvip.repository.UserRepository;
import com.example.phakebukvip.service.AsyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AsyncService asyncService;
    private LocalDateTime createdAt;
    @GetMapping("/post_form")
    public String showPostForm() {
        return "post_form"; // Tên file view: post_form.html
    }

    // 🟢 Create - Tạo bài viết
    @PostMapping("/post/create")
    public String createPost(@RequestParam("content") String content,
                             @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                             @RequestParam(value = "videoFile", required = false) MultipartFile videoFile,
                             Principal principal) throws IOException {

        String imageUrl = null;
        String videoUrl = null;

        if (imageFile != null && !imageFile.isEmpty()) {
            String filename = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
            Path path = Paths.get("uploads/images", filename);
            Files.createDirectories(path.getParent());
            Files.write(path, imageFile.getBytes());
            imageUrl = "/uploads/images/" + filename;
        }

        if (videoFile != null && !videoFile.isEmpty()) {
            String filename = UUID.randomUUID() + "_" + videoFile.getOriginalFilename();
            Path path = Paths.get("uploads/videos", filename);
            Files.createDirectories(path.getParent());
            Files.write(path, videoFile.getBytes());
            videoUrl = "/uploads/videos/" + filename;
        }

        // Tạo bài viết
        User user = userRepository.findByUsername(principal.getName()).orElseThrow();
        Post post = Post.builder()
                .content(content)
                .imageUrl(imageUrl)
                .videoUrl(videoUrl)
                .user(user)
                .createdAt(new Date())
                .build();

        postRepository.save(post);
        return "redirect:/home";
    }




    // 🟡 Read - Xem form chỉnh sửa bài viết
    @GetMapping("/post/edit/{id}")
    public String editPostForm(@PathVariable Long id, Model model, Principal principal) {
        Post post = postRepository.findById(id).orElseThrow();
        String username = principal.getName();

        if (!post.getUser().getUsername().equals(username)) {
            return "redirect:/home"; // Không phải tác giả thì không cho sửa
        }

        model.addAttribute("post", post);
        return "edit_post";
    }

    // 🟠 Update - Cập nhật bài viết
    @PostMapping("/post/update/{id}")
    public String updatePost(@PathVariable Long id,
                             @RequestParam("content") String content,
                             @RequestParam(value = "imageUrl", required = false) String imageUrl,
                             @RequestParam(value = "videoUrl", required = false) String videoUrl,
                             Principal principal) {
        Post post = postRepository.findById(id).orElseThrow();
        String username = principal.getName();

        if (post.getUser().getUsername().equals(username)) {
            post.setContent(content);
            post.setImageUrl(imageUrl);
            post.setVideoUrl(videoUrl);
            postRepository.save(post);
        }

        return "redirect:/home";
    }

    // 🔴 Delete - Xoá bài viết
    @PostMapping("/post/delete/{id}")
    public String deletePost(@PathVariable Long id, Principal principal) {
        Post post = postRepository.findById(id).orElseThrow();
        String username = principal.getName();

        if (post.getUser().getUsername().equals(username)) {
            postRepository.delete(post);
        }

        return "redirect:/home";
    }
}
