package com.melelee.ai;

import com.alibaba.cloud.ai.autoconfigure.dashscope.DashScopeChatProperties;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 公众号：程序员徐庶
 */
@Configuration
public class AiConfig {

    @Bean
    public ChatClient planningChatClient(DashScopeChatModel chatModel,
                                         DashScopeChatProperties options,
                                         ChatMemory chatMemory,
                                         ToolService toolService) {
        DashScopeChatOptions dashScopeChatOptions = DashScopeChatOptions.fromOptions(options.getOptions());
        dashScopeChatOptions.setTemperature(0.7);

        return  ChatClient.builder(chatModel)
                .defaultSystem("""
                            # 票务助手任务拆分规则
                            ## 1.要求
                            ### 1.1 根据用户内容识别任务
                            
                            ## 2. 任务
                            ### 2.1 JobType:退票(CANCEL) 要求用户提供姓名和预定号， 或者从对话中提取；
                            ### 2.2 JobType:查票(QUERY) 要求用户提供预定号， 或者从对话中提取；
                            ### 2.3 JobType:其他(OTHER)
                            """)
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(chatMemory).build()
                )
                .defaultTools(toolService)
                .defaultOptions(dashScopeChatOptions)
                .build();
    }

    @Bean
    public ChatClient botChatClient(DashScopeChatModel chatModel,
                                    DashScopeChatProperties options,
                                    ChatMemory chatMemory) {

        DashScopeChatOptions dashScopeChatOptions = DashScopeChatOptions.fromOptions(options.getOptions());
        dashScopeChatOptions.setTemperature(1.2);
        return  ChatClient.builder(chatModel)
                .defaultSystem("""
                           你是XS航空智能客服代理， 请以友好的语气服务用户。
                            """)
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(chatMemory).build()
                )
                .defaultOptions(dashScopeChatOptions)
                .build();
    }

}
