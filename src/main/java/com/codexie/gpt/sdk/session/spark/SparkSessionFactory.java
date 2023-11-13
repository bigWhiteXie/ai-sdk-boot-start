package com.codexie.gpt.sdk.session.spark;


import com.codexie.gpt.sdk.config.Configuration;
import com.codexie.gpt.sdk.session.SessionFactory;
import com.codexie.gpt.sdk.strategy.auth.impl.SparkAuthStrategy;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.sse.EventSources;


public class SparkSessionFactory extends SessionFactory {


    public SparkSessionFactory(Configuration configuration) {
        super(configuration);

        // 日志配置
//        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
//        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
//
//        // http客户端配置
//        this.okHttpClient = new OkHttpClient
//                .Builder()
//                .build();

        super.factory = EventSources.createFactory(this.okHttpClient);
    }

    @Override
    public SparkSession openSession() {

        try {

            return new SparkSession(configuration,okHttpClient,new SparkAuthStrategy(configuration));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("spark模型 签名失败");
        }
        // 创建 API 服务


    }




}
