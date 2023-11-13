package com.codexie.gpt.sdk.domain.chat.spark;

import com.codexie.gpt.sdk.domain.chat.Message;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SparkCompletionRequest {

    Header header;

    Parameter parameter;

    Payload payload;


    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @AllArgsConstructor
    @Builder
    public static class Header{
        @JsonProperty("app_id")
        private String app_id;

        private String uid;
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @AllArgsConstructor
    @Builder
    public static class Chat{
        /**
         * 取值为[general,generalv2,generalv3]
         * 注意：不同的取值对应的url也不一样！
         */
        private String domain = "general";

        /**
         * 取值为[0,1],默认为0.5	核采样阈值。用于决定结果随机性，取值越高随机性越强即相同的问题得到的不同答案的可能性越高
         */
        private float temperature = 0.5f;

        @JsonProperty("max_tokens")
        private int max_tokens ;

        /**
         * 取值为[1，6],默认为4
         * 从k个候选中随机选择⼀个（⾮等概率）
         */
        @JsonProperty("top_k")
        private int top_k ;

        /**
         * 否需要保障用户下的唯一性	用于关联用户会话
         */
        @JsonProperty("chat_id")
        private String chat_id;
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Parameter{
        Chat chat;
    }

    @Data
    @Builder
    public static class Payload{
        Messages message;
    }

    @Data
    @Builder
    public static class Messages{
        private List<Message> text;
    }

}
