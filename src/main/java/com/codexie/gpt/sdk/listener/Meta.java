package com.codexie.gpt.sdk.listener;

import com.codexie.gpt.sdk.domain.chat.chatglm.ChatGlmCompletionResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Meta implements Serializable {
    @JsonProperty("request_id")
    private String requestId;

    @JsonProperty("task_id")
    private String taskId;

    @JsonProperty("task_status")
    private String taskStatus;

    private ChatGlmCompletionResponse.Usage usage;
}