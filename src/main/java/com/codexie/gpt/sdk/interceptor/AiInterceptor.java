package com.codexie.gpt.sdk.interceptor;

import com.codexie.gpt.sdk.strategy.auth.AuthStrategy;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public abstract class AiInterceptor implements Interceptor {
    /** OpenAi apiKey 需要在官网申请 */
    protected String apiKey;
    /** 访问授权接口的认证 Token */
    protected String apiSecret;

    protected AuthStrategy authStrategy;

    public AiInterceptor(String apiKey, String apiSecret, AuthStrategy authStrategy) {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.authStrategy = authStrategy;
    }

    @NotNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        return chain.proceed(this.auth(chain.request()));
    }

    public abstract Request auth( Request original);
}
