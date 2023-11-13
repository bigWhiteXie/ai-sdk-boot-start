package com.codexie.gpt.sdk.config;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;



@ConfigurationProperties(prefix = "ai-sdk")
@Data
public class AiSdkProperties {

    private ModelProperties[] models ;

    public ModelProperties getModel(String code) {
        if(models == null){
            throw new RuntimeException("models加载失败");
        }
        for (ModelProperties model : models) {
            if(model.getCode().equals(code)){
                return model;
            }
        }
        return null;
    }




    public static class ModelProperties {
        private String apikey;
        private String apisecret;
        private String appId;
        private String code;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getAppId() {return appId;}

        public void setAppId(String appId) {this.appId = appId;}

        public String getApikey() {
            return apikey;
        }

        public void setApikey(String apikey) {
            this.apikey = apikey;
        }

        public String getApisecret() {
            return apisecret;
        }

        public void setApisecret(String apisecret) {
            this.apisecret = apisecret;
        }
    }
}
