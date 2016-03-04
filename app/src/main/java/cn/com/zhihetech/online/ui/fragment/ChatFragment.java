package cn.com.zhihetech.online.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.easemob.chat.EMMessage;
import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.ui.EaseChatFragment;
import com.easemob.easeui.widget.chatrow.EaseCustomChatRowProvider;

import java.util.HashMap;
import java.util.Map;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.Goods;
import cn.com.zhihetech.online.bean.ImgInfo;
import cn.com.zhihetech.online.bean.Merchant;
import cn.com.zhihetech.online.bean.RedEnvelop;
import cn.com.zhihetech.online.bean.User;
import cn.com.zhihetech.online.core.common.Constant;
import cn.com.zhihetech.online.core.ZhiheApplication;
import cn.com.zhihetech.online.core.common.ZhiheChatRowProvider;
import cn.com.zhihetech.online.core.util.StringUtils;
import cn.com.zhihetech.online.ui.activity.GoodsListActivity;
import cn.com.zhihetech.online.ui.activity.RedEnvelopeListActivity;


/**
 * 单人聊天界面
 * Created by ShenYunjie on 2016/2/2.
 */
public class ChatFragment extends EaseChatFragment {

    public final static String ACTIVITY_ID_KEY = "_ACTIVITY_ID";

    public final static int REQUEST_GOODS_LINK_CODE = 101;
    public final static int REQUEST_RED_ENVELOPE_CODE = 102;
    public final static int REQUEST_SECKILL_GOODS_CODE = 103;

    protected final static int ITEM_RED_ENVELOPE = 10;
    protected final static int ITEM_SHOP_LINK = 11;
    protected final static int ITEM_GOODS_LINK = 12;
    protected final static int ITEM_SECKILL_GOODS = 13;

    protected int[] itemStrings = {com.easemob.easeui.R.string.attach_take_pic, com.easemob.easeui.R.string.attach_picture,
            R.string.shop_link, R.string.goods_link, R.string.red_envelope, R.string.seckill_goods};
    protected int[] itemdrawables = {com.easemob.easeui.R.drawable.ease_chat_takepic_selector,
            R.drawable.pick_picture, R.drawable.chat_shop_icon, R.drawable.goods_icon,
            R.drawable.red_envelope_icon, R.drawable.seckill_goods_icon};
    protected int[] itemIds = {ITEM_TAKE_PICTURE, ITEM_PICTURE, ITEM_SHOP_LINK, ITEM_GOODS_LINK,
            ITEM_RED_ENVELOPE, ITEM_SECKILL_GOODS};

    @Override
    protected void initView() {
        super.initView();
        hideTitleBar();
        messageList.setShowUserNick(true);
        setChatFragmentListener(new EaseChatFragmentListener() {
            @Override
            public void onSetMessageAttributes(EMMessage message) {
                setMessageAttributes(message);
            }

            @Override
            public void onEnterToChatDetails() {

            }

            @Override
            public void onAvatarClick(String username) {
                Toast.makeText(getContext(), username, Toast.LENGTH_LONG).show();
            }

            @Override
            public boolean onMessageBubbleClick(EMMessage message) {
                return false;
            }

            @Override
            public void onMessageBubbleLongClick(EMMessage message) {

            }

            @Override
            public boolean onExtendMenuItemClick(int itemId, View view) {
                return false;
            }

            @Override
            public EaseCustomChatRowProvider onSetCustomChatRowProvider() {
                return new ZhiheChatRowProvider(getContext());
            }
        });
    }

    /**
     * 设置自定扩展信息
     *
     * @param message
     */
    private void setMessageAttributes(EMMessage message) {
        switch (ZhiheApplication.getInstance().getUserType()) {
            case Constant.COMMON_USER:
                User user = ZhiheApplication.getInstance().getUser();
                message.setAttribute(Constant.EXTEND_USER_NICK_NAME, user.getUserName());
                message.setAttribute(Constant.EXTEND_USER_ID, user.getUserId());
                ImgInfo headerImg = user.getHeaderImg();
                if (headerImg != null && !StringUtils.isEmpty(headerImg.getUrl())) {
                    message.setAttribute(Constant.EXTEND_USER_HEAD_IMG, headerImg.getUrl());
                }
                message.setAttribute(Constant.EXTEND_USER_TYPE, Constant.EXTEND_NORMAL_USER);
                break;
            case Constant.MERCHANT_USER:
                Merchant merchant = ZhiheApplication.getInstance().getLogedMerchant();
                message.setAttribute(Constant.EXTEND_USER_NICK_NAME, merchant.getMerchName());
                message.setAttribute(Constant.EXTEND_USER_ID, merchant.getMerchantId());
                ImgInfo coverImg = merchant.getCoverImg();
                if (coverImg != null && !StringUtils.isEmpty(coverImg.getUrl())) {
                    message.setAttribute(Constant.EXTEND_USER_HEAD_IMG, coverImg.getUrl());
                }
                message.setAttribute(Constant.EXTEND_USER_TYPE, Constant.EXTEND_MERCHANT_USER);
                break;
        }
    }

    @Override
    protected void registerExtendMenuItem() {
        switch (ZhiheApplication.getInstance().getUserType()) {
            case Constant.COMMON_USER:
                for (int i = 0; i < itemStrings.length - 4; i++) {
                    inputMenu.registerExtendMenuItem(itemStrings[i], itemdrawables[i], itemIds[i], extendMenuItemClickListener);
                }
                break;
            case Constant.MERCHANT_USER:
                final Merchant merchant = ZhiheApplication.getInstance().getLogedMerchant();
                if (this.chatType == EaseConstant.CHATTYPE_SINGLE) {
                    for (int i = 0; i < itemStrings.length - 2; i++) {
                        inputMenu.registerExtendMenuItem(itemStrings[i], itemdrawables[i], itemIds[i], new ExtendMenuItemClickListener(merchant));
                    }
                } else if (this.chatType == EaseConstant.CHATTYPE_CHATROOM) {
                    for (int i = 0; i < itemStrings.length; i++) {
                        inputMenu.registerExtendMenuItem(itemStrings[i], itemdrawables[i], itemIds[i], new ExtendMenuItemClickListener(merchant));
                    }
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_GOODS_LINK_CODE:
                    Object result = data.getSerializableExtra(GoodsListActivity.RESULT_GOODS_KEY);
                    if (result != null) {
                        sendGoodsLink((Goods) result);
                    }
                    return;
                case REQUEST_RED_ENVELOPE_CODE:
                    Object result1 = data.getSerializableExtra(RedEnvelopeListActivity.RESULT_RED_ENVELOP_KEY);
                    if (result1 != null) {
                        sendRedEnvelop((RedEnvelop) result1);
                    }
                    return;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 清空聊天记录
     */
    public void clearChatHistory() {
        emptyHistory();
    }

    /*=================发送信息=======================*/
    protected void sendRedEnvelope() {
        EMMessage message = EMMessage.createTxtSendMessage("", toChatUsername);
        message.setAttribute(Constant.EXTEND_MESSAGE_TYPE, Constant.EXTEND_MESSAGE_RED_ENVELOP);
        sendMessage(message);
    }

    /*=====================自定义发送信息========================*/

    /**
     * 发送店铺链接
     *
     * @param merchant
     */
    protected void sendShopLink(Merchant merchant) {
        Map<String, Object> map = new HashMap<>();
        map.put("merchantId", merchant.getMerchantId());
        map.put("merchName", merchant.getMerchName());
        if (merchant.getCoverImg() != null) {
            map.put("coverImg", merchant.getCoverImg().getUrl());
        }
        EMMessage message = EMMessage.createTxtSendMessage(JSONObject.toJSONString(map), toChatUsername);
        message.setAttribute(Constant.EXTEND_MESSAGE_TYPE, Constant.EXTEND_MESSAGE_SHOP_LINK);
        sendMessage(message);
    }

    /**
     * 发送商品链接
     *
     * @param goods
     */
    protected void sendGoodsLink(Goods goods) {
        Map<String, Object> map = new HashMap<>();
        map.put("goodsName", goods.getGoodsName());
        map.put("goodsId", goods.getGoodsId());
        map.put("price", goods.getPrice());
        map.put("stock", goods.getCurrentStock());
        if (goods.getCoverImg() != null && !StringUtils.isEmpty(goods.getCoverImg().getUrl())) {
            map.put("coverImg", goods.getCoverImg().getUrl());
        }
        EMMessage message = EMMessage.createTxtSendMessage(JSONObject.toJSONString(map), toChatUsername);
        message.setAttribute(Constant.EXTEND_MESSAGE_TYPE, Constant.EXTEND_MESSAGE_GOODS_LINK);
        sendMessage(message);
    }

    /**
     * 发送红包
     *
     * @param envelop
     */
    protected void sendRedEnvelop(RedEnvelop envelop) {
        Map<String, Object> map = new HashMap<>();
        map.put("redEnvelopId", envelop.getEnvelopId());
        map.put("envelopMsg", envelop.getEnvelopMsg());
        map.put("merchantName", envelop.getMerchant().getMerchName());
        EMMessage message = EMMessage.createTxtSendMessage(JSONObject.toJSONString(map), toChatUsername);
        message.setAttribute(Constant.EXTEND_MESSAGE_TYPE, Constant.EXTEND_MESSAGE_RED_ENVELOP);
        sendMessage(message);
    }

    class ExtendMenuItemClickListener extends MyItemClickListener {
        private Merchant merchant;

        public ExtendMenuItemClickListener(Merchant merchant) {
            this.merchant = merchant;
        }

        @Override
        public void onClick(int itemId, View view) {
            switch (itemId) {
                case ITEM_SHOP_LINK:
                    sendShopLink(merchant);
                    return;
                case ITEM_GOODS_LINK:
                    Intent intent = new Intent(getContext(), GoodsListActivity.class);
                    intent.putExtra(GoodsListActivity.MERCHANT_ID_KEY, merchant.getMerchantId());
                    startActivityForResult(intent, REQUEST_GOODS_LINK_CODE);
                    return;
                case ITEM_SECKILL_GOODS:

                    return;
                case ITEM_RED_ENVELOPE:
                    Intent redEnvelopeIntent = new Intent(getContext(), RedEnvelopeListActivity.class);
                    redEnvelopeIntent.putExtra(RedEnvelopeListActivity.MERCHANT_ID_KEY, merchant.getMerchantId());
                    redEnvelopeIntent.putExtra(RedEnvelopeListActivity.ACTIVITY_ID_KEY, fragmentArgs.getString(ACTIVITY_ID_KEY));
                    startActivityForResult(redEnvelopeIntent, REQUEST_RED_ENVELOPE_CODE);
                    return;
            }
            super.onClick(itemId, view);
        }
    }
}
