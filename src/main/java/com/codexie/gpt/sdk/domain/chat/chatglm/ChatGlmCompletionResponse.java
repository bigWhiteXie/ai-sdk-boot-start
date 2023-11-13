package com.codexie.gpt.sdk.domain.chat.chatglm;


import com.codexie.gpt.sdk.domain.chat.ChatCompletionResponse;
import com.codexie.gpt.sdk.domain.chat.chatgpt.ChatGptChoice;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class ChatGlmCompletionResponse implements Serializable {

    /** ID */
    private int code;
    /** 对象 */
    private String msg;
    /** 模型 */
    private boolean success;

    /** 数据 */
    private ResponseData data;

    @Data
    public static class ResponseData implements Serializable {
        private List<ChatGlmChoice> choices;

        @JsonProperty("request_id")
        private String requestId;

        @JsonProperty("task_id")
        private String taskId;

        @JsonProperty("task_status")
        private String taskStatus;

        private Usage usage;



    }

    @Data
    public static class Usage implements Serializable{
        /**输入token **/
        @JsonProperty("prompt_tokens")
        private int promptTokens;

        /**输出token **/
        @JsonProperty("completion_tokens")
        private int completionTokens;

        /**输出token **/
        @JsonProperty("total_tokens")
        private int totalTokens;
    }

}
