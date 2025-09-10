package com.melelee.ai;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.deepseek.DeepSeekAssistantMessage;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.ai.deepseek.DeepSeekChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;

@SpringBootTest
public class DeepseekTest {
    @Test
    public void test(@Autowired DeepSeekChatModel deepSeekChatModel) {
        String call = deepSeekChatModel.call("你是谁");
        System.out.println(call);
    }

    @Test
    public void testStream(@Autowired DeepSeekChatModel deepSeekChatModel) {
        Flux<String> flux = deepSeekChatModel.stream("你是deepseek的哪个版本的");
        flux.toIterable().forEach(System.out::print);
    }


    @Test
    public void testOptions(@Autowired DeepSeekChatModel deepSeekChatModel) {
        DeepSeekChatOptions options = DeepSeekChatOptions.builder()
                .temperature(1.99)
                .maxTokens(1024)
                .model("deepseek-reasoner")
                .build();

        Prompt prompt = new Prompt("你是谁", options);
        ChatResponse chatResponse = deepSeekChatModel.call(prompt);
        String text = chatResponse.getResult().getOutput().getText();
        System.out.println(text);
    }

    @Test
    public void testReasoner(@Autowired DeepSeekChatModel deepSeekChatModel) {
        DeepSeekChatOptions options = DeepSeekChatOptions.builder()
                .temperature(1.99)
                .maxTokens(1024)
                .model("deepseek-reasoner")
                .build();

        Prompt prompt = new Prompt("你是谁", options);
        ChatResponse chatResponse = deepSeekChatModel.call(prompt);
        AssistantMessage message = chatResponse.getResult().getOutput();

        if (message instanceof DeepSeekAssistantMessage) {
            System.out.println(((DeepSeekAssistantMessage) message).getReasoningContent());
            System.out.println("--------------------------------------------");
            System.out.println(message.getText());
        } else {
            System.out.println(message.getText());
        }
    }


    @Test
    public void testReasonerStream(@Autowired DeepSeekChatModel deepSeekChatModel) {
        DeepSeekChatOptions options = DeepSeekChatOptions.builder()
                .temperature(1.99)
                .maxTokens(1024)
                .model("deepseek-reasoner")
                .build();

        Prompt prompt = new Prompt("你是谁", options);
        Flux<ChatResponse> stream = deepSeekChatModel.stream(prompt);
        stream.toIterable().forEach(chatResponse -> {
            AssistantMessage message = chatResponse.getResult().getOutput();
            if (message instanceof DeepSeekAssistantMessage) {
                System.out.println(((DeepSeekAssistantMessage) message).getReasoningContent());
            }
        });

        System.out.println("--------------------------------------------");

        stream.toIterable().forEach(chatResponse -> {
            AssistantMessage output = chatResponse.getResult().getOutput();
            if (output instanceof DeepSeekAssistantMessage) {
                System.out.println(output.getText());
            }
        });
    }

}
