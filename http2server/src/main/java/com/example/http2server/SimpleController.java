package com.example.http2server;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
public class SimpleController {

    @GetMapping("/hello")
    public Mono<String> hello() {
        return Mono.just("world");
    }

    @GetMapping("/slow-hello")
    public Mono<String> slowHello() {
        return Mono.just("world").delayElement(Duration.ofSeconds(5000L));
    }
}
