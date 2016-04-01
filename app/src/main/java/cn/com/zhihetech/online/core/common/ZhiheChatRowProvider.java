package cn.com.zhihetech.online.core.common;

import android.content.Context;
import android.widget.BaseAdapter;

import com.easemob.chat.EMMessage;
import com.easemob.easeui.widget.chatrow.EaseChatRow;
import com.easemob.easeui.widget.chatrow.EaseCustomChatRowProvider;

import cn.com.zhihetech.online.bean.BaseBean;
import cn.com.zhihetech.online.core.chatrow.GoodsLinkChatRow;
import cn.com.zhihetech.online.core.chatrow.RedEnvelopChatRow;
import cn.com.zhihetech.online.core.chatrow.SeckillGoodsChatRow;
import cn.com.zhihetech.online.core.chatrow.ShopLinkChatRow;

/**
 * Created by ShenYunjie on 2016/3/2.
 */
public class ZhiheChatRowProvider extends BaseBean implements EaseCustomChatRowProvider {

    protected final int MESSAGE_TYPE_RECV_SHOP_LINK = 1;    //接收店铺链接
    protected final int MESSAGE_TYPE_SEND_SHOP_LINK = 2;    //发送店铺链接
    protected final int MESSAGE_TYPE_RECV_GOODS_LINK = 3;   //接收商品链接
    protected final int MESSAGE_TYPE_SEND_GOODS_LINK = 4;   //发送商品链接
    protected final int MESSAGE_TYPE_RECV_RED_ENVELOPE = 5;    //接收红包
    protected final int MESSAGE_TYPE_SEND_RED_ENVELOPE = 6;    //发送红包
    protected final int MESSAGE_TYPE_RECV_SECKILL_GOODS = 7;   //接收秒杀商品
    protected final int MESSAGE_TYPE_SEND_SECKILL_GOODS = 8;   //发送秒杀商品

    protected Context mContext;

    public ZhiheChatRowProvider(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getCustomChatRowTypeCount() {
        return 8;
    }

    @Override
    public int getCustomChatRowType(EMMessage message) {

        if (message.getType() == EMMessage.Type.TXT) {
            int msgType = 0;
            try {
                msgType = Integer.parseInt(message.getStringAttribute(Constant.EXTEND_MESSAGE_TYPE, "0"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            switch (msgType) {
                case Constant.EXTEND_MESSAGE_SHOP_LINK:
                    int value = message.direct == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_SHOP_LINK : MESSAGE_TYPE_SEND_SHOP_LINK;
                    return value;
                case Constant.EXTEND_MESSAGE_GOODS_LINK:
                    return message.direct == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_GOODS_LINK : MESSAGE_TYPE_SEND_GOODS_LINK;
                case Constant.EXTEND_MESSAGE_RED_ENVELOP:
                    return message.direct == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_RED_ENVELOPE : MESSAGE_TYPE_SEND_RED_ENVELOPE;
                case Constant.EXTEND_MESSAGE_SECKILL_GOODS:
                    return message.direct == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_SECKILL_GOODS : MESSAGE_TYPE_SEND_SECKILL_GOODS;
            }
        }
        return 0;
    }

    @Override
    public EaseChatRow getCustomChatRow(EMMessage message, int position, BaseAdapter adapter) {
        int msgType = 0;
        try {
            msgType = Integer.parseInt(message.getStringAttribute(Constant.EXTEND_MESSAGE_TYPE, "0"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        switch (msgType) {
            case Constant.EXTEND_MESSAGE_SHOP_LINK:
                return new ShopLinkChatRow(mContext, message, position, adapter);
            case Constant.EXTEND_MESSAGE_GOODS_LINK:
                return new GoodsLinkChatRow(mContext, message, position, adapter);
            case Constant.EXTEND_MESSAGE_RED_ENVELOP:
                return new RedEnvelopChatRow(mContext, message, position, adapter);
            case Constant.EXTEND_MESSAGE_SECKILL_GOODS:
                return new SeckillGoodsChatRow(mContext, message, position, adapter);
        }
        return null;
    }
}
