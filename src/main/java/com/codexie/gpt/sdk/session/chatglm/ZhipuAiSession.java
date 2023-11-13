package com.codexie.gpt.sdk.session.chatglm;


import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import com.alibaba.fastjson.JSON;
import com.codexie.gpt.sdk.common.Constants;
import com.codexie.gpt.sdk.domain.chat.ChatCompletionRequest;
import com.codexie.gpt.sdk.domain.chat.ChatCompletionResponse;
import com.codexie.gpt.sdk.domain.chat.chatglm.ChatGlmCompletionRequest;
import com.codexie.gpt.sdk.domain.chat.chatglm.ChatGlmCompletionResponse;
import com.codexie.gpt.sdk.listener.Meta;
import com.codexie.gpt.sdk.listener.ModelEventSourceListener;
import com.codexie.gpt.sdk.config.Configuration;
import com.codexie.gpt.sdk.session.Session;
import com.codexie.gpt.sdk.strategy.auth.AuthStrategy;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;

import java.io.IOException;


public class ZhipuAiSession implements Session {

    private OkHttpClient okHttpClient;

    private Configuration configuration;

    private EventSource.Factory factory;

    private AuthStrategy authStrategy;
    public ZhipuAiSession(Configuration configuration, EventSource.Factory factory, OkHttpClient okHttpClient, AuthStrategy authStrategy)
    {
        this.configuration = configuration;
        this.factory = factory;
        this.okHttpClient = okHttpClient;
        this.authStrategy = authStrategy;
    }



    @Override
    public ChatCompletionResponse completions(ChatCompletionRequest chatCompletionRequest) throws IOException {

        ChatGlmCompletionRequest glmCompletionRequest = buildRequest(chatCompletionRequest);

        Request request = new Request.Builder()
                // url: https://api.openai.com/v1/chat/completions - 通过 IOpenAiApi 配置的 POST 接口，用这样的方式从统一的地方获取配置信息
                .url(Constants.Model.CHATGLM.getUri())
                // 封装请求参数信息，如果使用了 Fastjson 也可以替换 ObjectMapper 转换对象
                .post(RequestBody.create(MediaType.parse(ContentType.JSON.getValue()), new ObjectMapper().writeValueAsString(glmCompletionRequest)))
                .build();

        request = auth(request);

        Response resp = okHttpClient.newCall(request).execute();
        if(resp.code() != 200){
            throw new RuntimeException("向chatglm发送聊天请求失败");
        }
        System.out.println(resp);
        return buildResponse(JSON.parseObject(resp.body().string(), ChatGlmCompletionResponse.class));

    }





    @Override
    public ChatCompletionResponse completionsByStream(ChatCompletionRequest chatCompletionRequest, EventSourceListener listener) throws InterruptedException {
        // 构建请求信息
        Request request = null;
        try {
            ChatGlmCompletionRequest glmCompletionRequest = buildRequest(chatCompletionRequest);
            request = new Request.Builder()
                    // url: https://api.openai.com/v1/chat/completions - 通过 IOpenAiApi 配置的 POST 接口，用这样的方式从统一的地方获取配置信息
                    .url(Constants.Model.CHATGLM_SSE.getUri())
                    // 封装请求参数信息，如果使用了 Fastjson 也可以替换 ObjectMapper 转换对象
                    .post(RequestBody.create(MediaType.parse(ContentType.JSON.getValue()), new ObjectMapper().writeValueAsString(glmCompletionRequest)))
                    .build();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        // 返回结果信息；EventSource 对象可以取消应答
        factory.newEventSource(request, listener);
        if(listener instanceof ModelEventSourceListener) {
            ModelEventSourceListener modelEventSourceListener = (ModelEventSourceListener) listener;
            modelEventSourceListener.getCountDownLatch().await();

            Meta meta = modelEventSourceListener.getMeta();
            String text = modelEventSourceListener.getOutputText();
            return ChatCompletionResponse.builder().data(text).build();
        }
        return null;
    }

    private ChatGlmCompletionRequest buildRequest(ChatCompletionRequest request){
        ChatGlmCompletionRequest glmCompletionRequest = ChatGlmCompletionRequest.builder().
                prompt(request.getPrompt()).
                build();
        return glmCompletionRequest;
    }

    private ChatCompletionResponse buildResponse(ChatGlmCompletionResponse response){
        return ChatCompletionResponse.builder().
                data(response.getData().getChoices().get(0).getContent()).
                taskId(response.getData().getTaskId()).
                totalTokens(response.getData().getUsage().getTotalTokens()).
                status("200").build();
    }

    @Override
    public Request auth(Request original) {
        // 创建请求
        return original.newBuilder()
                .url(original.url())
                .header(Header.AUTHORIZATION.getValue(), authStrategy.getToken(Constants.Manufacturer.ZHIPUAI))
                .header(Header.CONTENT_TYPE.getValue(), ContentType.JSON.getValue())
                .method(original.method(), original.body())
                .build();
    }

}