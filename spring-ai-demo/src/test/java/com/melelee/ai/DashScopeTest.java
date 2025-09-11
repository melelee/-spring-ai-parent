package com.melelee.ai;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.alibaba.cloud.ai.dashscope.image.DashScopeImageModel;
import com.alibaba.cloud.ai.dashscope.image.DashScopeImageOptions;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.deepseek.DeepSeekAssistantMessage;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.ai.deepseek.DeepSeekChatOptions;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;

@SpringBootTest
public class DashScopeTest {
    @Test
    public void test(@Autowired DashScopeChatModel chatModel) {
        String call = chatModel.call("你是谁");
        System.out.println(call);
    }

    @Test
    public void testStream(@Autowired DashScopeChatModel chatModel) {
        Flux<String> flux = chatModel.stream("你是哪个版本的");
        flux.toIterable().forEach(System.out::print);
    }


    @Test
    public void testOptions(@Autowired DashScopeChatModel chatModel) {
        DashScopeChatOptions options = DashScopeChatOptions.builder()
                .build();

        Prompt prompt = new Prompt("你是谁", options);
        ChatResponse chatResponse = chatModel.call(prompt);
        String text = chatResponse.getResult().getOutput().getText();
        System.out.println(text);
    }



    @Test
    public void test(@Autowired DashScopeImageModel imageModel) {
        DashScopeImageOptions options = DashScopeImageOptions.builder()
                .withModel("wanx2.1-t2i-turbo")
                .build();

        ImagePrompt imagePrompt = new ImagePrompt("画一个吉祥物", options);
        ImageResponse imageResponse = imageModel.call(imagePrompt);
        System.out.println(imageResponse);
    }


}
