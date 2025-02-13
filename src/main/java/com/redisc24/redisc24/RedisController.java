package com.redisc24.redisc24;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RedisController {

    @GetMapping("/hello")
    public String sayHello() {
        return "Hello from Fixed Window!";
    }

    @GetMapping("/hello-token")
    public String sayHelloTokenBucket() {
        return "Hello from Token Bucket!";
    }
}
