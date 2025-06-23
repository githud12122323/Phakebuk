package com.example.phakebukvip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class PhakebukvipApplication {

    public static void main(String[] args) {
        SpringApplication.run(PhakebukvipApplication.class, args);
    }

}
