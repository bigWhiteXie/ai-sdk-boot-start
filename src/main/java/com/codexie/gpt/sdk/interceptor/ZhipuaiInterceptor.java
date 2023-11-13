package com.codexie.gpt.sdk.interceptor;

import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import com.codexie.gpt.sdk.common.Constants;
import com.codexie.gpt.sdk.strategy.auth.AuthStrategy;
import okhttp3.Request;

public class ZhipuaiInterceptor extends AiInterceptor{


    public ZhipuaiInterceptor(String apiKey, String apiSecret, AuthStrategy authStrategy) {
        super(apiKey, apiSecret, authStrategy);
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
