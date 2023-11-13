package com.codexie.gpt.sdk.test;

import com.codexie.gpt.sdk.domain.chat.ChatCompletionRequest;
import com.codexie.gpt.sdk.domain.chat.ChatCompletionResponse;
import com.codexie.gpt.sdk.domain.chat.Message;
import com.codexie.gpt.sdk.config.Configuration;
import com.codexie.gpt.sdk.session.chatglm.ZhipuAiSession;
import com.codexie.gpt.sdk.session.chatglm.ZhipuAiSessionFactory;
import lombok.extern.slf4j.Slf4j;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class ChatglmTest {
    private ZhipuAiSession session;

    @Before
    public void test_OpenAiSessionFactory() {
        //1.配置configuration
        Configuration configuration = new Configuration();
        configuration.setApiKey("b6ddebfe0af182f2a015e81448b09d71");
        configuration.setApiSecret("thjX2dtaj8XvAJ8d");
        configuration.setExpired(500 * 1000);
        configuration.setExpired(500 * 1000);

        //2.根据配置文件创建会话工厂并得到会话
        ZhipuAiSessionFactory aiSessionFactory = new ZhipuAiSessionFactory(configuration);
        session = aiSessionFactory.openSession();
    }

    @Test
    public void test_chat_completion() throws IOException {

        //3.根据会话向大模型发送请求
        for(int i=1;i<10;i++) {
            ChatCompletionRequest request = ChatCompletionRequest.builder().
                    prompt(Collections.singletonList(Message.builder().content(i + "+" + i + "等于多少").role("user").build())).build();
            ChatCompletionResponse response = session.completions(request);
            log.info("{}",response);
        }
    }

    @Test
    public void test_chat_stream_completion() throws InterruptedException {
        ChatCompletionRequest request = ChatCompletionRequest.builder().
                prompt(Collections.singletonList(Message.builder().content("你好").role("user").build())).build();


        /**
         * type:add、finnish
         * id:reuestId
         * data:msg
         */
       session.completionsByStream(request, new EventSourceListener() {
            @Override
            public void onEvent(EventSource eventSource, String id, String type, String data) {
                log.info("eventSource：{}", eventSource);
                log.info("type：{}", type);
                log.info("id：{}", id);
                log.info("测试结果：{}", data);

            }
        });


        CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await();
    }
}
