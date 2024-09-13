package com.example.mega;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MegaApplication {

    public static void main(String[] args) {
        SpringApplication.run(MegaApplication.class, args);
    }

}
