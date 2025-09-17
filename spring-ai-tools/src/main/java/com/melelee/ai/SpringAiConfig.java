package com.melelee.ai;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringAiConfig {

    @Bean
    public ChatClient chatClient(ChatModel chatModel, ChatMemory chatMemory) {
        DashScopeChatOptions options = DashScopeChatOptions.builder().withTemperature(1.9).build();

        return ChatClient.builder(chatModel)
                .defaultOptions(options)
                .defaultSystem("""
                        # 票务助手任务拆分规则
                        ## 1. 要求
                        ### 1. 根据用户内容识别任务
                        
                        ## 2. 任务
                        ### 1. JobType:退票(CANCEL)要求用户提供订单号，或从对话中获取
                        ### 2. JobType:查票(QUERY)要求用户提供车次，或从对话中获取
                        ### 3. JobType:其他(OTHER)
                        """)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }
}
