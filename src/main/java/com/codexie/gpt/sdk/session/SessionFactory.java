package com.codexie.gpt.sdk.session;


import com.codexie.gpt.sdk.config.Configuration;
import okhttp3.OkHttpClient;
import okhttp3.sse.EventSource;

public abstract class SessionFactory {
    protected final Configuration configuration;
    /** 工厂事件 */
    protected  EventSource.Factory factory;

    protected  OkHttpClient okHttpClient;

    public SessionFactory(Configuration configuration) {

        this.configuration = configuration;

    }
    public abstract Session openSession();

    public void initOkHttpClient(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }
}