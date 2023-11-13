package com.codexie.gpt.sdk.session.chatglm;


import com.codexie.gpt.sdk.common.Constants;
import com.codexie.gpt.sdk.interceptor.ZhipuaiInterceptor;
import com.codexie.gpt.sdk.config.Configuration;
import com.codexie.gpt.sdk.session.SessionFactory;
import com.codexie.gpt.sdk.strategy.auth.impl.ChatglmJwtStrategy;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.sse.EventSources;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.concurrent.TimeUnit;


public class ZhipuAiSessionFactory extends SessionFactory {



    public ZhipuAiSessionFactory(Configuration configuration) {
        super(configuration);

        // 日志配置
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);

        // http客户端配置
//        this.okHttpClient = new OkHttpClient
//                .Builder()
//                .addInterceptor(httpLoggingInterceptor)
//                .connectTimeout(450, TimeUnit.SECONDS)
//                .writeTimeout(450, TimeUnit.SECONDS)
//                .readTimeout(450, TimeUnit.SECONDS)
//                .build();

        super.factory = EventSources.createFactory(this.okHttpClient);
    }

    @Override
    public ZhipuAiSession openSession() {


        return new ZhipuAiSession(configuration,factory,okHttpClient,new ChatglmJwtStrategy(configuration));
    }

}
