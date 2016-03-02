package cn.com.zhihetech.online.core.chatrow;

import android.content.Context;
import android.content.Intent;
import android.widget.BaseAdapter;

import com.alibaba.fastjson.JSONObject;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.easemob.easeui.widget.chatrow.EaseChatRowText;

import cn.com.zhihetech.online.core.ZhiheApplication;
import cn.com.zhihetech.online.ui.activity.MerchantHomeActivity;

/**
 * Created by ShenYunjie on 2016/3/2.
 */
public class ShopLinkChatRow extends EaseChatRowText {
    public ShopLinkChatRow(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

    @Override
    public void onSetUpView() {
        TextMessageBody txtBody = (TextMessageBody) message.getBody();
        JSONObject json = JSONObject.parseObject(txtBody.getMessage());
        String txtMsg = json.getString("merchName");
        txtMsg = "[店铺链接]" + txtMsg;
        this.contentView.setText(txtMsg);
        handleTextMessage();
    }

    @Override
    protected void onBubbleClick() {
        if (ZhiheApplication.getInstance().getUserType() == ZhiheApplication.MERCHANT_USER_TYPE) {
            return;
        }
        TextMessageBody txtBody = (TextMessageBody) message.getBody();
        JSONObject json = JSONObject.parseObject(txtBody.getMessage());
        Intent intent = new Intent(getContext(), MerchantHomeActivity.class);
        intent.putExtra(MerchantHomeActivity.MERCHANT_ID_KEY, json.getString("merchantId"));
        intent.putExtra(MerchantHomeActivity.MERCHANT_NAME_KEY, json.getString("merchantName"));
        getContext().startActivity(intent);
    }
}
