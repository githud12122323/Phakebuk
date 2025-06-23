package com.example.phakebukvip.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AsyncService {

    @Async
    public void logPostCreation(String username, String content) {
        try {
            Thread.sleep(2000); // giả lập xử lý nặng
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        log.info("📢 Người dùng '{}' đã đăng bài: {}", username, content);
    }
}
