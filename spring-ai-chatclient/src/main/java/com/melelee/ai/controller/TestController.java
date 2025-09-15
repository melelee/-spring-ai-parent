package com.melelee.ai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;


@RestController
public class TestController {

    private final ChatModel chatModel;

    public TestController(ChatModel chatModel) {
        this.chatModel = chatModel;
    }


    @GetMapping(value = "/test",produces = "text/stream;charset=utf-8")
    public Flux<String> test(@RequestParam String  question) {

        ChatClient.Builder builder = ChatClient.builder(chatModel);

        ChatOptions options = ChatOptions.builder().temperature(1.9).build();

        ChatClient chatClient = builder.defaultOptions(options).build();

        return chatClient.prompt().user(question).stream().content();

    }
}
