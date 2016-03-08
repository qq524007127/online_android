package cn.com.zhihetech.online.core.chatrow;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.core.ZhiheApplication;
import cn.com.zhihetech.online.core.util.ImageLoader;
import cn.com.zhihetech.online.ui.activity.SeckillGoodsInfoActivity;

/**
 * Created by ShenYunjie on 2016/3/7.
 */
public class SeckillGoodsChatRow extends GoodsLinkChatRow {

    protected TextView goodsSeckillPriceTv;

    public SeckillGoodsChatRow(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

    @Override
    protected void onInflatView() {
        inflater.inflate(message.direct == EMMessage.Direct.RECEIVE ?
                R.layout.chat_row_received_seckill_goods : R.layout.chat_row_send_seckill_goods, this);
    }

    @Override
    protected void onFindViewById() {
        super.onFindViewById();
        goodsSeckillPriceTv = (TextView) findViewById(R.id.chat_row_goods_seckill_price_tv);
        this.goodsPriceTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
    }

    @Override
    public void onSetUpView() {
        String txtMsg = ((TextMessageBody) message.getBody()).getMessage();
        JSONObject jsonObject = JSONObject.parseObject(txtMsg);
        ImageLoader.disPlayImage(this.goodsCoverImg, jsonObject.getString("goodsCoverImg"));
        this.goodsNameTv.setText(jsonObject.getString("goodsName"));
        this.goodsPriceTv.setText(String.valueOf(jsonObject.getFloatValue("goodsPrice")));
        this.goodsSeckillPriceTv.setText("现价:" + jsonObject.getFloatValue("activityPrice"));
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
                Intent intent = new Intent(getContext(), SeckillGoodsInfoActivity.class);
                intent.putExtra(SeckillGoodsInfoActivity.SECKILL_GOODS_ID_KEY, jsonObject.getString("agId"));
                intent.putExtra(SeckillGoodsInfoActivity.GOODS_NAME_KEY, jsonObject.getString("goodsName"));
                getContext().startActivity(intent);
        }
    }
}
