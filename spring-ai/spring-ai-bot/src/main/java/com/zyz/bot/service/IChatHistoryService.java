package com.zyz.bot.service;

import com.zyz.bot.pojo.ChatInfo;

import java.util.List;

public interface IChatHistoryService {
    /**
     * 保存会话历史，如果是新的则新增，否则更新
     * @param chatId
     * @param title
     */
    void save(String chatId, String title);

    /**
     * 获取所有的会话列表
     * @return
     */
    List<ChatInfo> getChatList();

    /**
     * 删除指定的会话
     * @param chatId
     */
    void clear(String chatId);
}
