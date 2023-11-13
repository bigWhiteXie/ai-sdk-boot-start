package com.codexie.gpt.sdk.domain.chat.spark;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SparkCompletionResponse {
    Header header;

    Payload payload;


    @Data
    @AllArgsConstructor
    public static class Header{
        private int code;
        private String message;
        private String sid;
        private int status;
    }

    @Data
    public static class Payload{
        private Choice choices;
        private Usage usage;
    }

    @Data
    public static class Choice{
        private int status;
        private int seq;
        private List<Content> text;
    }

    @Data
    public static class Content{
        private String content;
        private String role;
        private int index;
    }

    @Data
    public static class Usage {
        Text text;
    }

    @Data
    public static class Text {
        private int question_tokens;
        private int prompt_tokens;
        private int completion_tokens;
        private int total_tokens;
    }


}
