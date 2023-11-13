package com.codexie.gpt.sdk.common;


public class Constants {

    /**
     * 对接的ai厂商
     */
    public enum Manufacturer{
        OPENAI("openai"),
        SPARK("spark"),
        ZHIPUAI("zhipu");

        private String code;


        Manufacturer(String code)
        {
            this.code = code;

        }

        public String getCode() {
            return code;
        }
    }

    public enum Model{
        SPARK_V1("general","http://spark-api.xf-yun.com/v1.1/chat"),
        SPARK_V2("generalv2","http://spark-api.xf-yun.com/v2.1/chat"),
        SPARK_V3("generalv3","http://spark-api.xf-yun.com/v3.1/chat"),
        CHATGLM_SSE("chatglm_turbo","https://open.bigmodel.cn/api/paas/v3/model-api/chatglm_turbo/invoke"),
        CHATGLM("chatglm_turbo","https://open.bigmodel.cn/api/paas/v3/model-api/chatglm_turbo/invoke");



        private String domain;
        private String uri;

        Model(String domain, String uri) {
            this.domain = domain;
            this.uri = uri;
        }

        public String getDomain() {
            return domain;
        }

        public String getUri() {
            return uri;
        }

        public static Model getInstance(String model){
            for (Model value : Model.values()) {
                if(value.name().equals(model)){
                    return value;
                }
            }
            return null;
        }

        @Override
        public String toString() {
            return "Model{" +
                    "code='" + domain + '\'' +
                    ", uri='" + uri + '\'' +
                    '}';
        }
    }

    public static void main(String[] args) {
        System.out.println(Model.getInstance("SPARK_V3"));
    }

    /**
     * 官网支持的请求角色类型；system、user、assistant
     * https://platform.openai.com/docs/guides/chat/introduction
     */
    public enum Role {
        SYSTEM("system"),
        USER("user"),
        ASSISTANT("assistant"),
        ;

        private String code;

        Role(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }

    }

}
