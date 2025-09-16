package com.melelee.ai;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
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
    public void testAdvisors(@Autowired ChatModel chatModel) {
        ChatClient.Builder builder = ChatClient.builder(chatModel);
        builder.defaultAdvisors(new SimpleLoggerAdvisor(),
                        new SafeGuardAdvisor(List.of("敏感词"), "触发了敏感词", 0),
                        new BaseAdvisorImpl());

        ChatOptions options = ChatOptions.builder().temperature(1.9).build();

        ChatClient chatClient = builder.defaultOptions(options).build();

        String content = chatClient.prompt()
                .user("我眼睛痛")
                .call()
                .content();
        System.out.println(content);
    }


    @Test
    public void testChatMemory(@Autowired ChatModel chatModel, @Autowired ChatMemory chatMemory) {
        ChatClient.Builder builder = ChatClient.builder(chatModel);
        builder.defaultAdvisors(PromptChatMemoryAdvisor.builder(chatMemory).build());


        ChatClient chatClient = builder.build();

        String content = chatClient.prompt()
                .user("我叫张三")
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, "1"))
                .call()
                .content();
        System.out.println(content);

        content = chatClient.prompt()
                .user("我叫什么")
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, "1"))
                .call()
                .content();
        System.out.println(content);


        content = chatClient.prompt()
                .user("我叫李四")
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, "2"))
                .call()
                .content();
        System.out.println(content);

        content = chatClient.prompt()
                .user("我叫李四")
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, "2"))
                .call()
                .content();
        System.out.println(content);
    }


    @TestConfiguration
    static class TestConfig {

        @Bean
        public ChatMemory builder(ChatMemoryRepository chatMemoryRepository) {
            return MessageWindowChatMemory.builder().maxMessages(10).chatMemoryRepository(chatMemoryRepository).build();
        }
    }

}
