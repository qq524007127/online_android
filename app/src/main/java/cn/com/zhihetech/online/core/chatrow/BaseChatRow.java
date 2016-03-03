package cn.com.zhihetech.online.core.chatrow;

import android.content.Context;
import android.widget.BaseAdapter;

import com.alibaba.fastjson.JSONObject;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.easemob.easeui.widget.chatrow.EaseChatRowText;

import cn.com.zhihetech.online.core.ZhiheApplication;

/**
 * Created by ShenYunjie on 2016/3/3.
 */
public class BaseChatRow extends EaseChatRowText {


    protected JSONObject jsonObject;

    public BaseChatRow(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
        String txtMsg = ((TextMessageBody) message.getBody()).getMessage();
        jsonObject = JSONObject.parseObject(txtMsg);
    }

    /**
     * 获取当前登录用户的类型
     *
     * @return
     */
    protected int getUserType() {
        return ZhiheApplication.getInstance().getUserType();
    }
}
