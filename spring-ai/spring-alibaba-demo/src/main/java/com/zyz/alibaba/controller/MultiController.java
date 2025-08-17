package com.zyz.alibaba.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.content.Media;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RequestMapping("/multi")
@RestController
public class MultiController {

    private final ChatClient chatClient;

    public MultiController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping("/image")
    public String image(String message) throws Exception {
        String url = "https://dashscope.oss-cn-beijing.aliyuncs.com/images/dog_and_girl.jpeg";
        Media media = new Media(MimeTypeUtils.IMAGE_JPEG, new URI(url).toURL().toURI());
        List<Media> mediaList = List.of(media);
        UserMessage userMessage = UserMessage.builder().text(message).media(mediaList).build();
        return chatClient.prompt(new Prompt(userMessage)).call().content();
    }

}
