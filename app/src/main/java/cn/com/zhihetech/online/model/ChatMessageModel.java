package cn.com.zhihetech.online.model;

import android.support.annotation.NonNull;

import org.xutils.common.Callback;

import cn.com.zhihetech.online.bean.ChatMessage;
import cn.com.zhihetech.online.core.common.Constant;
import cn.com.zhihetech.online.core.http.PageDataCallback;
import cn.com.zhihetech.online.core.util.StringUtils;

/**
 * Created by ShenYunjie on 2016/4/27.
 */
public class ChatMessageModel extends BaseModel<ChatMessage> {

    /**
     * 获取ID消息之前的消息
     *
     * @param callback 回调
     * @param msgId    //消息ID
     * @param to       //消息接收着
     * @param from     //消息发送者
     * @return
     */
    public Callback.Cancelable getChatMessages(PageDataCallback<ChatMessage> callback, String msgId, @NonNull String to, String from) {
        ModelParams params = new ModelParams().addParam("to", to);
        if (msgId != null) {
            params.addParam("msgId", String.valueOf(msgId));
        }
        if (!StringUtils.isEmpty(from)) {
            params.addParam("from", from);
        }
        return getPageData(Constant.CHAT_MESSAGES_URL, params, callback);
    }
}
