package com.zyz.bot.pojo;

import lombok.Data;
import org.springframework.ai.chat.messages.Message;

@Data
public class MessageVo {
    private String role;
    private String content;

    public MessageVo(Message message) {
        switch(message.getMessageType()) {
            case USER -> {
                this.role = "user";
                break;
            }
            case ASSISTANT -> {
                this.role = "assistant";
                break;
            }
            case SYSTEM -> {
                this.role = "system";
                break;
            }
            case TOOL -> {
                this.role = "tool";
                break;
            }
        }
        this.content = message.getText();
    }
}
