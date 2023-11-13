package com.codexie.gpt.sdk.strategy.auth.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.codexie.gpt.sdk.config.Configuration;
import com.codexie.gpt.sdk.strategy.auth.AuthStrategy;


import java.net.MalformedURLException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ChatglmJwtStrategy extends AuthStrategy {

    public ChatglmJwtStrategy(Configuration configuration) {
        super(configuration);
    }

    @Override
    public String generatorAuthUrl() {
        String apiKey = configuration.getApiKey();
        String apiSecret = configuration.getApiSecret();
        long mills = configuration.getExpired();

        Algorithm alg = Algorithm.HMAC256(apiSecret);


        Map<String, Object> payload = new HashMap<>();
        payload.put("api_key", apiKey);
        payload.put("exp", System.currentTimeMillis() + mills);
        payload.put("timestamp", Calendar.getInstance().getTimeInMillis());
        Map<String, Object> headerClaims = new HashMap<>();
        headerClaims.put("alg", "HS256");
        headerClaims.put("sign_type", "SIGN");
        String token = JWT.create().withPayload(payload).withHeader(headerClaims).sign(alg);
        return token;
    }

    @Override
    public String generatorAuthUrl(String hostUrl) throws MalformedURLException, NoSuchAlgorithmException, InvalidKeyException {
        throw new RuntimeException("ChatGlm模型通过jwt令牌认证，无需构造认证地址");
    }
}
