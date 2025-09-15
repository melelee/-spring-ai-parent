package com.melelee.ai;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import java.util.List;

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

    @Test
    public void testPrompt(@Autowired ChatModel chatModel) {
        ChatClient.Builder builder = ChatClient.builder(chatModel);
        builder.defaultSystem("你是个医生AI,你的病人叫{name},{age}岁,{sex}性");

        ChatOptions options = ChatOptions.builder().temperature(1.9).build();

        ChatClient chatClient = builder.defaultOptions(options).build();

        String hello = chatClient.prompt()
                .system(promptSystemSpec -> promptSystemSpec.param("name", "张三").param("age", 18).param("sex", "男"))
                .user("你好")
                .call()
                .content();
        System.out.println(hello);
    }


    @Test
    public void testPromptTemplate(@Autowired ChatModel chatModel, @Value("classpath:/prompt.st") Resource resource) {
        ChatClient.Builder builder = ChatClient.builder(chatModel);
        builder.defaultSystem(resource);

        ChatOptions options = ChatOptions.builder().temperature(1.9).build();

        ChatClient chatClient = builder.defaultOptions(options).build();

        String hello = chatClient.prompt()
                .user("你好")
                .call()
                .content();
        System.out.println(hello);
    }


    @Test
    public void testAdvisors(@Autowired ChatModel chatModel, @Value("classpath:/prompt.st") Resource resource) {
        ChatClient.Builder builder = ChatClient.builder(chatModel);
        builder.defaultSystem(resource).defaultAdvisors(new SimpleLoggerAdvisor(),new SafeGuardAdvisor(List.of("你好"),"shibai",0));

        ChatOptions options = ChatOptions.builder().temperature(1.9).build();

        ChatClient chatClient = builder.defaultOptions(options).build();

        String hello = chatClient.prompt()
                .user("你好")
                .call()
                .content();
        System.out.println(hello);
    }

}
