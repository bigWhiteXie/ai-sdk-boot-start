package com.codexie.gpt.sdk.config;

import com.codexie.gpt.sdk.common.Constants;
import com.codexie.gpt.sdk.context.SessionContext;
import com.codexie.gpt.sdk.session.SessionFactory;
import com.codexie.gpt.sdk.session.chatglm.ZhipuAiSessionFactory;
import com.codexie.gpt.sdk.session.spark.SparkSessionFactory;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableConfigurationProperties(AiSdkProperties.class)
@ConditionalOnProperty(prefix = "ai-sdk")
public class AiSdkAutoConfiguration {



    @Bean
    public OkHttpClient okHttpClient() {
      return  new OkHttpClient
                .Builder()
                .connectTimeout(450, TimeUnit.SECONDS)
                .writeTimeout(450, TimeUnit.SECONDS)
                .readTimeout(450, TimeUnit.SECONDS)
                .build();
    }


    @Bean
    public SessionContext sessionContext(AiSdkProperties aiSdkProperties){
        SessionContext sessionContext = new SessionContext();

        for (Constants.Manufacturer manufacturer : Constants.Manufacturer.values()) {
            SessionFactory sessionFactory = null;
            com.codexie.gpt.sdk.config.Configuration configuation = null;

            switch (manufacturer.getCode()){
                case "zhipu":
                    configuation = getConfiguation("zhipu", aiSdkProperties);
                    if(configuation != null) {
                        sessionFactory = new ZhipuAiSessionFactory(configuation);
                    }
                    break;
                case "spark":
                     configuation = getConfiguation("spark", aiSdkProperties);
                    if(configuation != null) {
                        sessionFactory = new SparkSessionFactory(configuation);
                    }
                    break;
                default:
                    break;
            }
            if(sessionFactory != null){
                sessionFactory.initOkHttpClient(okHttpClient());
                sessionContext.addSession(manufacturer.getCode(),sessionFactory.openSession());
            }
        }
        return sessionContext;
    }

    private com.codexie.gpt.sdk.config.Configuration getConfiguation(String code,AiSdkProperties aiSdkProperties){
        AiSdkProperties.ModelProperties properties = aiSdkProperties.getModel(code);
        if(properties == null){
            return null;
        }
        com.codexie.gpt.sdk.config.Configuration configuration = new com.codexie.gpt.sdk.config.Configuration();
        configuration.setAppId(properties.getAppId());
        configuration.setApiKey(properties.getApikey());
        configuration.setApiSecret(properties.getApisecret());
        configuration.setExpired(30*60*1000);
        return configuration;
    }
}
