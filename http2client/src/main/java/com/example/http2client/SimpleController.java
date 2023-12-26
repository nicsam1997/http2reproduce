package com.example.http2client;

import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.HttpProtocol;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;

import static io.netty.channel.ChannelOption.CONNECT_TIMEOUT_MILLIS;

@RestController
public class SimpleController {
    private final WebClient client;

    public SimpleController(WebClient.Builder clientBuilder) {
        client = setUpClient(clientBuilder);
    };

    private WebClient setUpClient(WebClient.Builder clientBuilder) {
        final var connectionProvider = ConnectionProvider
                .builder("custom")
                .maxConnections(2)
                .evictInBackground(Duration.ofSeconds(300))
                .lifo()
                .maxIdleTime(Duration.ofSeconds(300))
                .build();
        final var httpClient =
                HttpClient.create(connectionProvider)
                        .protocol(HttpProtocol.H2C)
                        .responseTimeout(Duration.ofSeconds(4))
                        .option(CONNECT_TIMEOUT_MILLIS, 50);
        httpClient.warmup().block();
        return clientBuilder.clientConnector(new ReactorClientHttpConnector(httpClient)).build();
    }


    @GetMapping("/hello")
    public Mono<String> hello() {
        return client.get().uri("http://localhost:8082/hello").retrieve().bodyToMono(String.class);
    }

    @GetMapping("/slow-hello")
    public Mono<String> slowHello() {
        return client.get().uri("http://localhost:8082/slow-hello").retrieve().bodyToMono(String.class);
    }
}

