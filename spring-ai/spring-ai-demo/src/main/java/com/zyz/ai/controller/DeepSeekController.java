package com.zyz.ai.controller;

import com.zyz.ai.entity.Recipe;
import org.codehaus.groovy.transform.GroovyASTTransformation;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/deepseek")
public class DeepSeekController {

    @Autowired
    private OpenAiChatModel openAiChatModel;

    @Autowired
    private ChatClient chatClient;

    @GetMapping("/chat")
    public String chat(String message) {
        return openAiChatModel.call(message);
    }

    @GetMapping("/call")
    public String call(String message) {
        return chatClient.prompt()
                // 用户输入
                .user(message)
                // 调用模型
                .call()
                // 获取模型返回的内容
                .content();
    }

    @GetMapping("/entity")
    public String entity(String message) {
        return chatClient.prompt()
                .user(String.format("请帮我生成一个%s的食谱",message))
                .call()
                .entity(Recipe.class).toString();
    }

    @GetMapping(value = "/stream",produces = "text/html;charset=utf-8")
    public Flux<String> stream(String message) {
        Flux<String> content = chatClient.prompt()
                .user(message)
                .stream()
                .content();
        return content;
    }
}
