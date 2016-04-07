package cn.com.zhihetech.online.core.emchat.chatrow;

import android.content.Context;
import android.content.Intent;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.core.ZhiheApplication;
import cn.com.zhihetech.online.core.util.ImageLoader;
import cn.com.zhihetech.online.ui.activity.GoodsInfoActivity;

/**
 * Created by ShenYunjie on 2016/3/2.
 */
public class GoodsLinkChatRow extends BaseChatRow {

    protected ImageView goodsCoverImg;
    protected TextView goodsNameTv;
    protected TextView goodsPriceTv;

    public GoodsLinkChatRow(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

    @Override
    protected void onInflatView() {
        inflater.inflate(message.direct == EMMessage.Direct.RECEIVE ?
                R.layout.chat_row_received_shop_link : R.layout.chat_row_send_shop_link, this);
    }

    @Override
    protected void onFindViewById() {
        this.goodsCoverImg = (ImageView) findViewById(R.id.chat_row_goods_cover_iv);
        this.goodsNameTv = (TextView) findViewById(R.id.chat_row_goods_name_tv);
        this.goodsPriceTv = (TextView) findViewById(R.id.chat_row_goods_price_tv);
    }


    @Override
    public void onSetUpView() {
        String txtMsg = ((TextMessageBody) message.getBody()).getMessage();
        JSONObject jsonObject = JSONObject.parseObject(txtMsg);
        ImageLoader.disPlayImage(this.goodsCoverImg, jsonObject.getString("coverImg"));
        this.goodsNameTv.setText(jsonObject.getString("goodsName"));
        this.goodsPriceTv.setText("￥:" + jsonObject.getFloatValue("price"));

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
                Intent intent = new Intent(getContext(), GoodsInfoActivity.class);
                intent.putExtra(GoodsInfoActivity.GOODS_ID_KEY, jsonObject.getString("goodsId"));
                intent.putExtra(GoodsInfoActivity.GOODS_NAME_KEY, jsonObject.getString("goodsName"));
                getContext().startActivity(intent);
        }
    }
}
