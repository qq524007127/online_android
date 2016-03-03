package cn.com.zhihetech.online.core.chatrow;

import android.content.Context;
import android.content.Intent;
import android.widget.BaseAdapter;

import com.easemob.chat.EMMessage;

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
        String txtMsg = jsonObject.getString("merchName");
        txtMsg = "[店铺链接]" + txtMsg;
        this.contentView.setText(txtMsg);
        handleTextMessage();
    }

    @Override
    protected void onBubbleClick() {
        switch (getUserType()) {
            case ZhiheApplication.MERCHANT_USER_TYPE:
                return;
            default:
                Intent intent = new Intent(getContext(), MerchantHomeActivity.class);
                intent.putExtra(MerchantHomeActivity.MERCHANT_ID_KEY, jsonObject.getString("merchantId"));
                intent.putExtra(MerchantHomeActivity.MERCHANT_NAME_KEY, jsonObject.getString("merchantName"));
                getContext().startActivity(intent);
        }
    }
}
