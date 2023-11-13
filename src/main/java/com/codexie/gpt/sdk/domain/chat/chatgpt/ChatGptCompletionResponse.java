package com.codexie.gpt.sdk.domain.chat.chatgpt;


import com.codexie.gpt.sdk.domain.chat.ChatCompletionResponse;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
public class ChatGptCompletionResponse  implements Serializable {

    /** ID */
    private String id;
    /** 对象 */
    private String object;
    /** 模型 */
    private String model;
    /** 对话 */
    private List<ChatGptChoice> choices;
    /** 创建 */
    private long created;
    /** 耗材 */


}
