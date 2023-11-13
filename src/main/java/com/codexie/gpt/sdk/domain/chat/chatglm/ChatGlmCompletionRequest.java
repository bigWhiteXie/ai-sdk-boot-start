package com.codexie.gpt.sdk.domain.chat.chatglm;

import com.codexie.gpt.sdk.domain.chat.ChatCompletionRequest;
import com.codexie.gpt.sdk.domain.chat.Message;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Data
@Builder
@Slf4j
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class ChatGlmCompletionRequest  {
    /** 调用对话模型时，将当前对话信息列表作为提示输入给模型;
     * 按照 {"role": "user", "content": "你好"} 的键值对形式进行传参;
     * 总长度超过模型最长输入限制后会自动截断，需按时间由旧到新排序*/
    @NonNull
    private List<Message> prompt;


    /** 控制温度【随机性】；0到1之间。较低的值(如0.2)将使输出更加随机，而较高的值(如0.8)将使输出更加集中和确定 */
    private double temperature = 0.95;
    /** 多样性控制；使用温度采样的替代方法称为核心采样，其中模型考虑具有top_p概率质量的令牌的结果。因此，0.1 意味着只考虑包含前 10% 概率质量的代币 */
    @JsonProperty("top_p")
    private Double topP = 0.7d;

    /**
     * 由用户端传参，需保证唯一性；用于区分每次请求的唯一标识，用户端不传时平台会默认生成
     */
    @JsonProperty("request_id")
    private String requestId;

    /**
     * 用于控制每次返回内容的类型，空或者没有此字段时默认按照json_string返回
     * - json_string 返回标准的 JSON 字符串
     * - text 返回原始的文本内容
     */
    @JsonProperty("return_type")
    private String returnType;


}
