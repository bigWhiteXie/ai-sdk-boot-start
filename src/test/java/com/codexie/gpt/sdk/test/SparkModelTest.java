package com.codexie.gpt.sdk.test;

import com.codexie.gpt.sdk.domain.chat.ChatCompletionRequest;
import com.codexie.gpt.sdk.domain.chat.ChatCompletionResponse;
import com.codexie.gpt.sdk.domain.chat.Message;
import com.codexie.gpt.sdk.config.Configuration;
import com.codexie.gpt.sdk.session.spark.SparkSession;
import com.codexie.gpt.sdk.session.spark.SparkSessionFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class SparkModelTest {
    private SparkSession session;

    @Before
    public void test_OpenAiSessionFactory() {
        //1.配置configuration
        Configuration configuration = new Configuration();

        configuration.setApiKey("b27443b6a9446e0067805fb88d6aaf23");
        configuration.setApiSecret("MzM4MWQ0MjJmYzBlNWI3NDAwNmM3YmQ2");
        configuration.setAppId("3ce7723d");

        //2.根据配置文件创建会话工厂并得到会话
        SparkSessionFactory aiSessionFactory = new SparkSessionFactory(configuration);
        session = aiSessionFactory.openSession();
    }

    /**
     * 同一线程连续调用十次
     */
    @Test
    public void test_chat_completion() {
        String[] questions = {"你现在是经验丰富的java专家，请协助我编写java程序","我是小神仙","你是产品经理"};
    //3.根据会话向大模型发送请求
        for(int i=1;i<10;i++) {
            ChatCompletionRequest request = ChatCompletionRequest.builder().
                    prompt(Collections.singletonList(Message.builder().content(questions[i% questions.length]).role("user").build())).build();
            request.setModel("spark_v3");
            ChatCompletionResponse response = session.completions(request);
            log.info("{}",response);
        }
    }


    /**
     * 多线程调用十次
     */
    @Test
    public void test_chat_async_completion() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(10);
        //3.根据会话向大模型发送请求
        for(int i=1;i<10;i++) {
            ChatCompletionRequest request = ChatCompletionRequest.builder().
                    prompt(Collections.singletonList(Message.builder().content(i + "+" + i + "等于多少").role("user").build())).build();
            new Thread(() -> {
                ChatCompletionResponse response = session.completions(request);
                log.info("{}",response);
            countDownLatch.countDown();
            }).start();
        }
        countDownLatch.await();
    }


}
