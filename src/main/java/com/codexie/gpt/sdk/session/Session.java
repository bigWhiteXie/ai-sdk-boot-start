package com.codexie.gpt.sdk.session;


import com.codexie.gpt.sdk.domain.chat.ChatCompletionRequest;
import com.codexie.gpt.sdk.domain.chat.ChatCompletionResponse;
import com.codexie.gpt.sdk.listener.impl.StandardEventSourceListener;
import com.fasterxml.jackson.core.JsonProcessingException;
import okhttp3.Request;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;

import java.io.IOException;

public interface  Session {


    /**
     * 问答模型——普通问答
     * @param chatCompletionRequest 请求信息
     * @return                      返回结果
     */
      ChatCompletionResponse completions(ChatCompletionRequest chatCompletionRequest) throws IOException;


    /**
     * 问答模型——流式问答
     * @param chatCompletionRequest 请求信息
     * @return                      返回结果
     */
    default   ChatCompletionResponse completionsByStream(ChatCompletionRequest chatCompletionRequest) throws InterruptedException {
        return completionsByStream(chatCompletionRequest,new StandardEventSourceListener());
    }
    /**
     * 问答模型——流式问答
     * @param chatCompletionRequest 请求信息
     * @return                      返回结果
     */
    ChatCompletionResponse completionsByStream(ChatCompletionRequest chatCompletionRequest, EventSourceListener listener) throws InterruptedException;

    Request auth(Request original);

}
