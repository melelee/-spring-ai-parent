package com.melelee.ai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@RestController
public class TestController {

    private final ChatClient chatClient;

    public TestController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }


    @GetMapping(value = "/test", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> index(@RequestParam String message) {
        Sinks.Many<String> sink = Sinks.many().multicast().onBackpressureBuffer();
        sink.tryEmitNext("hello");


        new Thread(() -> {
            JobType entity = chatClient.prompt().user(message).call().entity(JobType.class);
            if (entity == JobType.QUERY) {
                sink.tryEmitNext("query");
            }
        }).start();
        return sink.asFlux();
    }
}
