package com.codexie.gpt.sdk.interceptor;

import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import com.codexie.gpt.sdk.common.Constants;
import com.codexie.gpt.sdk.strategy.auth.AuthStrategy;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
* openai自定义拦截器
 */
public class OpenAiInterceptor extends AiInterceptor {

    public OpenAiInterceptor(String apiKey, String apiSecret, AuthStrategy authStrategy) {
        super(apiKey, apiSecret, authStrategy);
    }

    @NotNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        return chain.proceed(this.auth(chain.request()));
    }

    @Override
    public Request auth( Request original) {
        // 设置Token信息；如果没有此类限制，是不需要设置的。
        HttpUrl url = original.url().newBuilder()
                .addQueryParameter("token", authStrategy.getToken(Constants.Manufacturer.OPENAI))
                .build();

        // 创建请求
        return original.newBuilder()
                .url(url)
                .header(Header.AUTHORIZATION.getValue(), "Bearer " + apiKey)
                .header(Header.CONTENT_TYPE.getValue(), ContentType.JSON.getValue())
                .method(original.method(), original.body())
                .build();
    }

}
