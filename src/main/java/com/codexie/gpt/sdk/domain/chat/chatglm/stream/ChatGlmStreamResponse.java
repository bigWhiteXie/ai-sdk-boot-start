package com.codexie.gpt.sdk.domain.chat.chatglm.stream;

import com.codexie.gpt.sdk.listener.Meta;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class ChatGlmStreamResponse {
    /**
     * 请求id：代表一次完整响应的唯一标识
     */
    private String id;

    /**
     * 流式响应的事件类型：add、finnish、error、interrupted
     */
    private String event;

    /**
     * 响应内容
     */
    private String data;

    private Meta meta;



}
