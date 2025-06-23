// ForgotPasswordController.java
package com.example.phakebukvip.controller;

import com.example.phakebukvip.model.User;
import com.example.phakebukvip.repository.UserRepository;
import com.example.phakebukvip.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class ForgotPasswordController {

    private final UserRepository userRepository;
    private final MailService mailService;

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "forgot_password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("email") String email, Model model) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            String token = UUID.randomUUID().toString();
            user.setResetToken(token);
            userRepository.save(user);

            String resetLink = "http://localhost:8080/reset-password?token=" + token;
            mailService.sendEmail(email, "Đặt lại mật khẩu", "Click vào link để đặt lại mật khẩu: " + resetLink);

            model.addAttribute("message", "Email đặt lại mật khẩu đã được gửi!");
        } else {
            model.addAttribute("error", "Không tìm thấy người dùng với email này.");
        }
        return "forgot_password";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
        Optional<User> optionalUser = userRepository.findByResetToken(token);
        if (optionalUser.isEmpty()) {
            model.addAttribute("error", "Token không hợp lệ hoặc đã hết hạn.");
            return "forgot_password";
        }
        model.addAttribute("token", token);
        return "reset_password";
    }

    @PostMapping("/reset-password")
    public String processResetPassword(@RequestParam("token") String token,
                                       @RequestParam("password") String password,
                                       Model model) {
        Optional<User> optionalUser = userRepository.findByResetToken(token);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setPassword(password); // Bạn nên mã hoá bằng BCrypt nếu có security
            user.setResetToken(null);
            userRepository.save(user);
            model.addAttribute("message", "Đặt lại mật khẩu thành công!");
            return "login";
        } else {
            model.addAttribute("error", "Token không hợp lệ.");
            return "reset_password";
        }
    }
}
