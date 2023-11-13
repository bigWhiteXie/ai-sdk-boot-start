package com.codexie.gpt.sdk.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSources;
import org.jetbrains.annotations.NotNull;



@Data
@NoArgsConstructor
@AllArgsConstructor
public class Configuration {

    private String apiKey;


    private String apiSecret;


    private long expired;

    private String appId;





}
