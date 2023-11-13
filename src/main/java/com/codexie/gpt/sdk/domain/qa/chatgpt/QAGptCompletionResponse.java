package com.codexie.gpt.sdk.domain.qa.chatgpt;


import lombok.Data;

import java.io.Serializable;


@Data
public class QAGptCompletionResponse implements Serializable {

    /** ID */
    private String id;
    /** 对象 */
    private String object;
    /** 模型 */
    private String model;
    /** 对话 */
    private QAGptChoice[] choices;
    /** 创建 */
    private long created;
    /** 耗材 */


}
