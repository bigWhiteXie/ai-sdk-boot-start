# ai-sdk-boot-start
## 配置文件
```
ai-sdk:
  models:
      - { code: zhipu, apikey: xxxx, apisecret: xxx, appId: xxx }
      - { code: spark, apikey: xxx, apisecret: xxx, appId: xxx }
```
> 目前仅支持清华智谱和科大讯飞的星火模型

## 使用示例
```
@SpringBootTest(classes = GptApiApplication.class)
@Slf4j
class GptApiApplicationTests {
    //注入SessionContext
    @Autowired
    SessionContext sessionContext;
    @Test
    void contextLoads() throws IOException {
        //通过枚举类Manufacturer得到对应厂商的session
        Session session = sessionContext.getSession(Constants.Manufacturer.ZHIPUAI.getCode());

        //构建chat请求
        ChatCompletionRequest request = ChatCompletionRequest.builder().
                prompt(Collections.singletonList(Message.builder().content("你好").role("user").build())).build();

        //发送请求得到响应
        ChatCompletionResponse response = session.completions(request);
        log.info("{}",response);
    }

}
```

### Chat请求参数
```
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatCompletionRequest {
    /**
     * 服务厂商(非必填)
     */
    private String company;

    /**
     * 调用方式(非必填)，不填默认同步调用
     */
    private String invoke;

    /**
     * 请求内容(必填)
     * role string user|assitant  必填
     * content string 对话内容 必填
     * name string 对话名称
     */
    private List<Message> prompt;

    /**
     * 请求模型(非必填)，可以设置为Constants.Model的code，不设置则默认每个厂商自己的默认模型
     */
    private String model;

    /**
     * 用户id(非必填)
     */
    private String uid;

}
```

### 响应结果
```
public class ChatCompletionResponse {
    /** 模型 */
    private String model;

    /**
     * 响应内容
     */
    private String data;

    /**
     * 对话id
     */
    private String taskId;

    /**
    *响应状态：200为成功，其它均为失败
    */
    private String status;

    /**
    * 该次对话总tokens
    */
    private int totalTokens;
}
```
