package com.codexie.gpt.sdk.listener.impl;



import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.codexie.gpt.sdk.listener.Meta;
import com.codexie.gpt.sdk.listener.ModelEventSourceListener;
import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.sse.EventSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * 监听模型结果，等到所有事件完成后统一返回。效果类似同步调用，如您有其他Listener需求可以联系我们，或自己对Listener进行扩展
 */

public class StandardEventSourceListener extends ModelEventSourceListener {

    private static final Logger logger = LoggerFactory.getLogger(StandardEventSourceListener.class);

    private String outputText = "";

    private boolean incremental = true;

    /*    private EventStatus status = new EventStatus();*/

    protected CountDownLatch countDownLatch = new CountDownLatch(1);

    private Gson gson = new Gson();

    private Meta meta;

    @Override
    public void onOpen(EventSource eventSource, Response response) {
        logger.info("server start sending events");
    }

    @Override
    public void onEvent(EventSource eventSource, String id, String type, String data) {

        if ("finish".equals(type)) {
            logger.info("对话结束");
            logger.info("data:{}",data);
            JSONObject jsonObject = JSON.parseObject(data);
            String meta = jsonObject.getString("meta");
            this.meta = gson.fromJson(meta, Meta.class);
        }
        if (this.isIncremental()) {
            outputText += data;
        } else {
            outputText = data;
        }
    }

    @Override
    public void onClosed(EventSource eventSource) {
        logger.info("server stream closed");
        try {

            eventSource.cancel();
        } finally {
            countDownLatch.countDown();
        }
    }

    @Override
    public void onFailure(EventSource eventSource, Throwable t, Response response) {
        try {
            logger.error("sse connection fail");
            logger.error("response:{}",response);
/*            synchronized (status) {
                status.setStatus(EventStatus.StatusFailed);
                status.notify();
            }*/
            eventSource.cancel();
        } finally {
            countDownLatch.countDown();
        }
    }

    @Override
    public String getOutputText() {
        return outputText;
    }

    @Override
    public Meta getMeta() {
        return this.meta;
    }

    @Override
    public CountDownLatch getCountDownLatch() {
        return countDownLatch;
    }

    public boolean isIncremental() {
        return incremental;
    }

    public void setIncremental(boolean incremental) {
        this.incremental = incremental;
    }

}