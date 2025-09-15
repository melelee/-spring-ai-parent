package com.melelee.ai;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.content.Media;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import reactor.core.publisher.Flux;

@SpringBootTest
public class OllamaTest {
    @Test
    public void test(@Autowired OllamaChatModel chatModel) {
        String call = chatModel.call("一加一等于几");
        System.out.println(call);
    }

    @Test
    public void testNoThink(@Autowired OllamaChatModel chatModel) {
        String call = chatModel.call("一加一等于几/no_think");
        System.out.println(call);
    }


    @Test
    public void testStream(@Autowired OllamaChatModel chatModel) {
        Flux<String> flux = chatModel.stream("你是哪个版本的");
        flux.toIterable().forEach(System.out::print);
    }


    @Test
    public void testOptions(@Autowired OllamaChatModel model) {
        ClassPathResource classPathResource = new ClassPathResource("123.jpg");
        OllamaOptions options = OllamaOptions.builder().model("gemma3").build();

        Media media = new Media(Media.Format.IMAGE_JPEG, classPathResource);

        UserMessage message = UserMessage.builder().media(media).text("描述下图片").build();

        Prompt prompt = new Prompt(message, options);

        ChatResponse chatResponse = model.call(prompt);
        String text = chatResponse.getResult().getOutput().getText();
        System.out.println(text);
    }


}
