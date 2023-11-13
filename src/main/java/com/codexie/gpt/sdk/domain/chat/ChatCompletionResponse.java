package com.codexie.gpt.sdk.domain.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatCompletionResponse {
    /** 模型 */
    private String model;

    /**
     * 响应内容
     */
    private String data;

    /**
     * 对话id
     */
    private String taskId;


    private String status;

    private int totalTokens;
}
