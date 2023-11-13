package com.codexie.gpt.sdk.session.chatgpt;


import com.codexie.gpt.sdk.domain.chat.ChatCompletionRequest;
import com.codexie.gpt.sdk.domain.chat.ChatCompletionResponse;
import com.codexie.gpt.sdk.domain.chat.chatgpt.ChatGptCompletionRequest;
import com.codexie.gpt.sdk.domain.chat.chatgpt.ChatGptCompletionResponse;
import com.codexie.gpt.sdk.config.Configuration;
import com.codexie.gpt.sdk.session.Session;
import okhttp3.Request;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;


public class OpenAiSession implements Session {

    private IOpenAiApi openAiApi;

    private Configuration configuration;

    private EventSource.Factory factory;

    public OpenAiSession(Configuration configuration, EventSource.Factory factory, IOpenAiApi openAiApi)
    {
        this.configuration = configuration;
        this.factory = factory;
        this.openAiApi = openAiApi;
    }



    @Override
    public ChatCompletionResponse completions(ChatCompletionRequest chatCompletionRequest) {
        ChatGptCompletionResponse response = this.openAiApi.completions(buildRequest(chatCompletionRequest)).blockingGet();
        return buildResponse(response);
    }

    @Override
    public ChatCompletionResponse completionsByStream(ChatCompletionRequest chatCompletionRequest, EventSourceListener listener) {
        return null;
    }

    @Override
    public Request auth(Request original) {
        return null;
    }

    /**
     * 调整模型、内容
     * @param request
     * @return
     */
    private ChatGptCompletionRequest buildRequest(ChatCompletionRequest request){
        ChatGptCompletionRequest chatCompletion = ChatGptCompletionRequest
                .builder()
                .messages(request.getPrompt())
                .model(ChatGptCompletionRequest.Model.GPT_3_5_TURBO.getCode())
                .build();
        if(request.getModel() != null){
            chatCompletion.setModel(request.getModel());
        }
        return chatCompletion;
    }
    private ChatCompletionResponse buildResponse(ChatGptCompletionResponse response){
        return ChatCompletionResponse.builder().model(response.getModel()).
                data(response.getChoices().get(0).getMessage().getContent()).
                taskId(response.getId()).build();
    }

}