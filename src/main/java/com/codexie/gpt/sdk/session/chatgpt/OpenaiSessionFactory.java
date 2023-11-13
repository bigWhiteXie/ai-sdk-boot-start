package com.codexie.gpt.sdk.session.chatgpt;


import com.codexie.gpt.sdk.interceptor.OpenAiInterceptor;

import com.codexie.gpt.sdk.config.Configuration;
import com.codexie.gpt.sdk.session.SessionFactory;
import com.codexie.gpt.sdk.strategy.auth.impl.ChatglmJwtStrategy;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.concurrent.TimeUnit;


public class OpenaiSessionFactory extends SessionFactory {



    public OpenaiSessionFactory(Configuration configuration) {
        super(configuration);

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);

        // http客户端配置
//        this.okHttpClient = new OkHttpClient
//                .Builder()
//                .addInterceptor(httpLoggingInterceptor)
//                .addInterceptor(new OpenAiInterceptor(configuration.getApiKey(), configuration.getApiSecret(), new ChatglmJwtStrategy(configuration)))
//                .connectTimeout(450, TimeUnit.SECONDS)
//                .writeTimeout(450, TimeUnit.SECONDS)
//                .readTimeout(450, TimeUnit.SECONDS)
//                .build();
    }

    @Override
    public OpenAiSession openSession() {


        // 3. 创建 API 服务
        IOpenAiApi openAiApi = new Retrofit.Builder()
//                .baseUrl(configuration.getApiHost())
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create())
                .build().create(IOpenAiApi.class);

        return new OpenAiSession(configuration,factory,openAiApi);
    }

}
