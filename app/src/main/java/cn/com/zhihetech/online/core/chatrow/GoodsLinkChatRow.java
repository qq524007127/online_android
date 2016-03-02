package cn.com.zhihetech.online.core.chatrow;

import android.content.Context;
import android.content.Intent;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.easemob.easeui.widget.chatrow.EaseChatRowText;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.core.ZhiheApplication;
import cn.com.zhihetech.online.core.common.Constant;
import cn.com.zhihetech.online.core.util.ImageLoader;
import cn.com.zhihetech.online.ui.activity.GoodsInfoActivity;

/**
 * Created by ShenYunjie on 2016/3/2.
 */
public class GoodsLinkChatRow extends EaseChatRowText {

    private ImageView goodsCoverImg;
    private TextView goodsNameTv;
    private TextView goodsPriceTv;

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
        TextMessageBody txtBody = (TextMessageBody) message.getBody();
        bindingView(txtBody.getMessage());

        handleTextMessage();
    }

    /**
     * 绑定数据
     *
     * @param result
     */
    private void bindingView(String result) {
        JSONObject json = JSONObject.parseObject(result);
        ImageLoader.disPlayImage(this.goodsCoverImg, json.getString("coverImg"));
        this.goodsNameTv.setText(json.getString("goodsName"));
        this.goodsPriceTv.setText("￥:" + json.getFloatValue("price"));
    }

    @Override
    protected void onBubbleClick() {
        if (ZhiheApplication.getInstance().getUserType() == ZhiheApplication.MERCHANT_USER_TYPE) {
            return;
        }
        TextMessageBody txtBody = (TextMessageBody) message.getBody();
        JSONObject json = JSONObject.parseObject(txtBody.getMessage());
        Intent intent = new Intent(getContext(), GoodsInfoActivity.class);
        intent.putExtra(GoodsInfoActivity.GOODS_ID_KEY, json.getString("goodsId"));
        intent.putExtra(GoodsInfoActivity.GOODS_NAME_KEY, json.getString("goodsName"));
        getContext().startActivity(intent);
    }
}
