package com.codexie.gpt.sdk.strategy.auth;

import com.codexie.gpt.sdk.common.Constants;
import com.codexie.gpt.sdk.config.Configuration;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;

import java.net.MalformedURLException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
@Slf4j
public abstract class AuthStrategy {

    static Cache<String, String> cache = CacheBuilder.newBuilder()
            .initialCapacity(5)  // 初始容量
            .maximumSize(10)     // 最大缓存数，超出淘汰
            .expireAfterWrite(30, TimeUnit.MINUTES) // 过期时间
            .build();

    protected Configuration configuration;

    public AuthStrategy(Configuration configuration) {
        this.configuration = configuration;
    }

    public String getToken(Constants.Manufacturer manufacturer){
        String token = null;
        try {
            token = cache.get(manufacturer.getCode(), ()-> generatorAuthUrl());
        } catch (ExecutionException e) {
            log.error("刷新token失败");
            e.printStackTrace();
        }
        return token;
    }

    public abstract String generatorAuthUrl();

    public abstract String generatorAuthUrl(String hostUrl) throws MalformedURLException, NoSuchAlgorithmException, InvalidKeyException;
}
