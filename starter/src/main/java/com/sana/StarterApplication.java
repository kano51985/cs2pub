package com.sana;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author: 庞宇
 * @CreateTime: 2025-04-08
 * @Description: 启动类
 * @Version: 1.0
 */

@SpringBootApplication(scanBasePackages = "com.sana")
public class StarterApplication {
    public static void main(String[] args) {
        SpringApplication.run(StarterApplication.class, args);
    }
}
