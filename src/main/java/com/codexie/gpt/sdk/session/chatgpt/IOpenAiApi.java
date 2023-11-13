package com.codexie.gpt.sdk.session.chatgpt;


import com.codexie.gpt.sdk.domain.chat.chatgpt.ChatGptCompletionRequest;
import com.codexie.gpt.sdk.domain.chat.chatgpt.ChatGptCompletionResponse;
import com.codexie.gpt.sdk.domain.qa.chatgpt.QAGptCompletionRequest;
import com.codexie.gpt.sdk.domain.qa.chatgpt.QAGptCompletionResponse;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * chatgpt访问代理
 */
public interface IOpenAiApi {

    /**
     * 文本问答
     * @param qaCompletionRequest 请求信息
     * @return                    返回结果
     */
    @POST("v1/completions")
    Single<QAGptCompletionResponse> completions(@Body QAGptCompletionRequest qaCompletionRequest);

    /**
     * 默认 GPT-3.5 问答模型
     * @param chatGptCompletionRequest 请求信息
     * @return                      返回结果
     */
    @POST("v1/chat/completions")
    Single<ChatGptCompletionResponse> completions(@Body ChatGptCompletionRequest chatGptCompletionRequest);

}
