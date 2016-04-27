package cn.com.zhihetech.online.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by ShenYunjie on 2016/4/25.
 */
public class MessageExt extends BaseBean {
    private String extId;
    private ChatMessage message;
    private String key;
    private String value;

    public String getExtId() {
        return extId;
    }

    public void setExtId(String extId) {
        this.extId = extId;
    }

    @JSONField(serialize = false)
    public ChatMessage getMessage() {
        return message;
    }

    public void setMessage(ChatMessage message) {
        this.message = message;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
