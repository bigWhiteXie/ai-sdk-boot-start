package com.codexie.gpt.sdk.domain.chat.chatglm;

import lombok.Data;

@Data
public class ChatGlmChoice {
    private String role;
    private String content;
}
