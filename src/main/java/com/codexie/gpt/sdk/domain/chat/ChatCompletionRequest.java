package com.codexie.gpt.sdk.domain.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatCompletionRequest {
    /**
     * 服务厂商
     */
    private String company;

    /**
     * 调用方式
     */
    private String invoke;

    /**
     * 请求内容
     */
    private List<Message> prompt;

    /**
     * 请求模型
     */
    private String model;


    private String uid;



}
