package com.jackie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class ChatRoomApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChatRoomApplication.class, args);
    }
}
