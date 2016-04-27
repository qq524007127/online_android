package cn.com.zhihetech.online.bean;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.com.zhihetech.online.core.util.StringUtils;

/**
 * Created by ShenYunjie on 2016/4/25.
 */
public class MessagePayload extends BaseBean {
    private List<MessageBody> bodies;
    private Map<String, String> ext;

    public List<MessageBody> getBodies() {
        return bodies;
    }

    public void setBodies(List<MessageBody> bodies) {
        this.bodies = bodies;
    }

    public Map<String, String> getExt() {
        return ext;
    }

    public void setExt(Map<String, String> ext) {
        this.ext = ext;
    }


    public void setMessageExt(MessageExt... exts) {
        if (exts == null || exts.length < 1) {
            return;
        }
        if (this.ext == null) {
            this.ext = new LinkedHashMap<>();
        }
        for (MessageExt msgExt : exts) {
            this.ext.put(msgExt.getKey(), msgExt.getValue());
        }
    }

    public List<MessageExt> getMsgExts(ChatMessage message) {
        if (this.ext == null || this.ext.isEmpty()) {
            return null;
        }
        List<MessageExt> exts = new ArrayList<>();
        for (String key : this.ext.keySet()) {
            if (StringUtils.isEmpty(key)) {
                continue;
            }
            MessageExt msgExt = new MessageExt();
            msgExt.setKey(key);
            msgExt.setValue(this.ext.get(key));
            msgExt.setMessage(message);
            exts.add(msgExt);
        }
        return exts;
    }
}
