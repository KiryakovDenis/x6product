package ru.kdv.study;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class x6product {
    public static void main(String[] args) {
        SpringApplication.run(x6product.class, args);
    }
}