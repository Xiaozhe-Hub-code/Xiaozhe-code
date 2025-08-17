package com.zyz.bot.controller;

import com.zyz.bot.pojo.ChatInfo;
import com.zyz.bot.pojo.MessageVo;
import com.zyz.bot.service.impl.ChatHistoryServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ChatClient chatClient;
    @Autowired
    private ChatMemory chatMemory;
    @Autowired
    private ChatHistoryServiceImpl chatHistoryService;

    @GetMapping(value = "/stream")
    public Flux<String> stream(String prompt,String chatId, HttpServletResponse response) {
        response.setContentType("text/event-stream;charset=utf-8");
        response.setCharacterEncoding("UTF-8");
        log.info("会话chatId:{},prompt:{}",chatId,prompt);
        chatHistoryService.save(chatId,prompt); // 更新会话列表（如果是新的就添加，不是新的就更新）
        return chatClient.prompt()
                .user(prompt)
                .advisors(s -> s.param(MessageChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY,chatId))
                .stream()
                .content();
    }

    @GetMapping("/getChatIds")
    public List<ChatInfo> getChatIds() {
        return chatHistoryService.getChatList();
    }

    @GetMapping("/getChatHistory")
    public List<MessageVo> getChatHistory(String chatId) {
        log.info("获取会话chatId:{}的聊天记录",chatId);
        List<Message> messages = chatMemory.get(chatId, 20);
        if (messages == null || messages.isEmpty()) return List.of();
        return messages.stream().map(MessageVo::new).toList();
    }

    @GetMapping("/deleteChat")
    public boolean deleteChat(String chatId) {
        log.info("删除会话chatId:{}",chatId);
        try {
            chatMemory.clear(chatId);
            chatHistoryService.clear(chatId);
        } catch (Exception e) {
            log.info("删除会话失败chatId:{}",chatId);
            return false;
        }
        return true;
    }


}
