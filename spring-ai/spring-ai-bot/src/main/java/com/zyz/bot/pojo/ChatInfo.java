package com.zyz.bot.pojo;

import lombok.Data;

@Data
public class ChatInfo {
    private String chatId;
    private String title;

    public ChatInfo(String chatId, String title) {
        this.chatId = chatId;
        this.title = title==null?"无标题":(title.length()>10?title.substring(0,10):title);
    }
}
