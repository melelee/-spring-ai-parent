package com.melelee.ai;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ChatClientTest {
    @Test
    public void test(@Autowired ChatClient.Builder builder) {
        ChatClient chatClient = builder.build();
        String hello = chatClient.prompt().user("你好，你是谁").call().content();
        System.out.println(hello);
    }

    @Test
    public void test(@Autowired ChatModel chatModel) {
        System.out.println(chatModel.getClass());

        ChatClient.Builder builder = ChatClient.builder(chatModel);

        ChatOptions options = ChatOptions.builder().temperature(1.9).build();

        ChatClient chatClient = builder.defaultOptions(options).build();

        String hello = chatClient.prompt().user("你好，你是谁").call().content();
        System.out.println(hello);
    }

}
