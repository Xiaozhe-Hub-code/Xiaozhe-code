package com.zyz.bot.service.impl;

import com.zyz.bot.pojo.ChatInfo;
import com.zyz.bot.service.IChatHistoryService;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChatHistoryServiceImpl implements IChatHistoryService {

    private final Map<String,String> chatHistorys = new LinkedHashMap<>();

    @Override
    public void save(String chatId, String title) {
        chatHistorys.put(chatId, title);
    }

    @Override
    public List<ChatInfo> getChatList() {
        return chatHistorys.entrySet().stream()
                .map(entry -> new ChatInfo(entry.getKey(), entry.getValue()))
                .toList();

    }

    @Override
    public void clear(String chatId) {
        chatHistorys.remove(chatId);
    }
}
