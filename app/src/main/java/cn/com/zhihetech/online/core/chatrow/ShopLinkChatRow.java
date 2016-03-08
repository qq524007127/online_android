package cn.com.zhihetech.online.core.chatrow;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.widget.BaseAdapter;

import com.alibaba.fastjson.JSONObject;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;

import cn.com.zhihetech.online.core.ZhiheApplication;
import cn.com.zhihetech.online.ui.activity.MerchantHomeActivity;

/**
 * Created by ShenYunjie on 2016/3/2.
 */
public class ShopLinkChatRow extends BaseChatRow {

    public ShopLinkChatRow(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

    @Override
    public void onSetUpView() {
        String txtMsg = ((TextMessageBody) message.getBody()).getMessage();
        JSONObject jsonObject = JSONObject.parseObject(txtMsg);
        String txt = jsonObject.getString("merchName");
        txt = "[店铺链接]" + txt;
        this.contentView.setText(txt);
        this.contentView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        handleTextMessage();
    }

    @Override
    protected void onBubbleClick() {
        switch (getUserType()) {
            case ZhiheApplication.MERCHANT_USER_TYPE:
                return;
            default:
                String txtMsg = ((TextMessageBody) message.getBody()).getMessage();
                JSONObject jsonObject = JSONObject.parseObject(txtMsg);
                Intent intent = new Intent(getContext(), MerchantHomeActivity.class);
                intent.putExtra(MerchantHomeActivity.MERCHANT_ID_KEY, jsonObject.getString("merchantId"));
                intent.putExtra(MerchantHomeActivity.MERCHANT_NAME_KEY, jsonObject.getString("merchantName"));
                getContext().startActivity(intent);
        }
    }
}
