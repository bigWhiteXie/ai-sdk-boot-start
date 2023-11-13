package com.codexie.gpt.sdk.session.spark;


import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import com.alibaba.fastjson.JSON;
import com.codexie.gpt.sdk.common.Constants;
import com.codexie.gpt.sdk.domain.chat.ChatCompletionRequest;
import com.codexie.gpt.sdk.domain.chat.ChatCompletionResponse;
import com.codexie.gpt.sdk.domain.chat.spark.SparkCompletionRequest;
import com.codexie.gpt.sdk.domain.chat.spark.SparkCompletionResponse;
import com.codexie.gpt.sdk.config.Configuration;
import com.codexie.gpt.sdk.session.Session;
import com.codexie.gpt.sdk.strategy.auth.AuthStrategy;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okhttp3.sse.EventSourceListener;

import javax.annotation.Nullable;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class SparkSession implements Session {

    private OkHttpClient okHttpClient;

    private String defaultModel = "spark_v2";

    private Configuration configuration;

    private AuthStrategy authStrategy;

    public SparkSession(Configuration configuration, OkHttpClient okHttpClient,AuthStrategy authStrategy) {
        this.configuration = configuration;

        this.okHttpClient = okHttpClient;

        this.authStrategy = authStrategy;
    }





    @Override
    public ChatCompletionResponse completions(ChatCompletionRequest chatCompletionRequest) {
        SparkCompletionRequest sparkCompletionRequest = buildRequest(chatCompletionRequest);
        String uid = chatCompletionRequest.getUid() == null ? RandomUtil.randomString(10): chatCompletionRequest.getUid();
        String authUrl = null;
        try {

            //根据请求的模型类型找到合适的url
            String model = chatCompletionRequest.getModel();
            String domain = StrUtil.isEmpty(model) ? defaultModel:model ;

            Constants.Model instance = Constants.Model.getInstance(domain.toUpperCase());

            sparkCompletionRequest.getParameter().getChat().setDomain(instance.getDomain());
            //生成待令牌的url
            authUrl = getAuthUrl(instance.getUri(), configuration.getApiKey(), configuration.getApiSecret());
            String url = authUrl.toString().replace("http://", "ws://").replace("https://", "wss://");

            Request request = new Request.Builder().url(url).build();
            DefaultWebSocketListener listener =  new DefaultWebSocketListener(uid);
            WebSocket webSocket =  okHttpClient.newWebSocket(request, listener);

            //websocket连接无法并发发送消息
            //测试任务：同一个uid多线程并发查看是否出错
            //不同的uid并发查看性能
            CountDownLatch latch = new CountDownLatch(1);
            listener.setCountDownLatch(latch);
            String jsonString = JSON.toJSONString(sparkCompletionRequest);
            System.out.println(jsonString);
            boolean res = webSocket.send(jsonString);

            if(!res){
                throw new RuntimeException("websocket 发送消息失败");
            }
            latch.await();
            SparkCompletionResponse response = listener.getFinalRes();
            if(response.getHeader().getCode() != 0){
                throw new RuntimeException("spark模型请求异常，状态码为：" + response.getHeader().getCode());
            }
            return buildResponse(response);

        } catch (Exception e) {
            log.error(e.getMessage());

            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }


    }





    @Override
    public ChatCompletionResponse completionsByStream(ChatCompletionRequest chatCompletionRequest, EventSourceListener listener) throws InterruptedException {
       throw new RuntimeException("星火模型没有流式调用");
    }

    @Override
    public Request auth(Request original) {
        // 创建请求
        try {
            String authUrl = authStrategy.generatorAuthUrl(original.url().toString());
            return original.newBuilder().url(authUrl).
                    header(Header.CONTENT_TYPE.getValue(), ContentType.JSON.getValue())
                    .method(original.method(), original.body())
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private SparkCompletionRequest buildRequest(ChatCompletionRequest request){
        SparkCompletionRequest.Header header = SparkCompletionRequest.Header.builder().app_id(configuration.getAppId()).build();
        if(request.getUid() != null){
            header.setUid(request.getUid());
        }

        //装配parameter中的domain，也就是模型类型
        SparkCompletionRequest.Parameter parameter = SparkCompletionRequest.Parameter.builder().chat(
                SparkCompletionRequest.Chat.builder().
                        max_tokens(2048).
                        temperature(0.5f).
                        top_k(4).
                        build()).
                build();



        //装配payload，也就是封装消息
        SparkCompletionRequest.Payload payload = SparkCompletionRequest.Payload.builder().message(SparkCompletionRequest.Messages.builder().text(request.getPrompt()).build()).build();

        SparkCompletionRequest sparkCompletionRequest = SparkCompletionRequest.builder().
                header(header).
                parameter(parameter).
                payload(payload).
                build();
        return sparkCompletionRequest;
    }

    private ChatCompletionResponse buildResponse(SparkCompletionResponse response){
        ChatCompletionResponse completionResponse = ChatCompletionResponse.builder().
                data(response.getPayload().getChoices().getText().get(0).getContent()).
                taskId(response.getHeader().getSid()).
                totalTokens(response.getPayload().getUsage().getText().getTotal_tokens()).
                build();
        completionResponse.setStatus("200");
        return completionResponse;
    }


    class DefaultWebSocketListener extends WebSocketListener {

        private String uid;

        private StringBuilder totalAnswer = new StringBuilder();


        private SparkCompletionResponse finalRes;

        public void setCountDownLatch(CountDownLatch countDownLatch) {
            this.countDownLatch = countDownLatch;
        }

        public SparkCompletionResponse getFinalRes() {
            return finalRes;
        }

        private CountDownLatch countDownLatch;
        public DefaultWebSocketListener(String uid) {
            this.uid = uid;
        }

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            System.out.println("websocket open");
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            SparkCompletionResponse response = JSON.parseObject(text, SparkCompletionResponse.class);
            if (response.getHeader().getCode() != 0) {
                System.out.println("发生错误，错误码为：" + response.getHeader().getCode());
                System.out.println("本次请求的sid为：" + response.getHeader().getSid());
                finalRes = response;
                countDownLatch.countDown();
                webSocket.close(1000, "");
                return;
            }
            List<SparkCompletionResponse.Content> textList = response.getPayload().getChoices().getText();
            for (SparkCompletionResponse.Content temp : textList) {
                System.out.print(temp.getContent());
                totalAnswer.append(temp.getContent());
            }
            if (response.getHeader().getStatus() == 2) {
                finalRes = response;
                finalRes.getPayload().getChoices().getText().get(0).setContent(totalAnswer.toString());
                totalAnswer = new StringBuilder();
                countDownLatch.countDown();
            }

        }


        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            System.out.println("websocket关闭，原因：" + reason);

        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, @Nullable Response response) {
            System.out.println("websocket 处理消息异常，" + t);

        }
    }
    private  String getAuthUrl(String hostUrl, String apiKey, String apiSecret) throws Exception {
        URL url = new URL(hostUrl);
        // 时间
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        String date = format.format(new Date());
        // 拼接
        String preStr = "host: " + url.getHost() + "\n" +
                "date: " + date + "\n" +
                "GET " + url.getPath() + " HTTP/1.1";

        // SHA256加密
        Mac mac = Mac.getInstance("hmacsha256");
        SecretKeySpec spec = new SecretKeySpec(apiSecret.getBytes(StandardCharsets.UTF_8), "hmacsha256");
        mac.init(spec);

        byte[] hexDigits = mac.doFinal(preStr.getBytes(StandardCharsets.UTF_8));
        // Base64加密
        String sha = Base64.getEncoder().encodeToString(hexDigits);
        // System.err.println(sha);
        // 拼接
        String authorization = String.format("api_key=\"%s\", algorithm=\"%s\", headers=\"%s\", signature=\"%s\"", apiKey, "hmac-sha256", "host date request-line", sha);
        // 拼接地址
        HttpUrl httpUrl = Objects.requireNonNull(HttpUrl.parse("https://" + url.getHost() + url.getPath())).newBuilder().//
                addQueryParameter("authorization", Base64.getEncoder().encodeToString(authorization.getBytes(StandardCharsets.UTF_8))).//
                addQueryParameter("date", date).//
                addQueryParameter("host", url.getHost()).//
                build();

        // System.err.println(httpUrl.toString());
        return httpUrl.toString();
    }
}