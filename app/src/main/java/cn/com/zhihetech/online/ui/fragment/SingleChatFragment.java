package cn.com.zhihetech.online.ui.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.easemob.chat.EMMessage;
import com.easemob.easeui.ui.EaseBaiduMapActivity;
import com.easemob.easeui.ui.EaseChatFragment;
import com.easemob.easeui.widget.EaseChatExtendMenu;
import com.easemob.easeui.widget.chatrow.EaseCustomChatRowProvider;

import java.util.HashMap;
import java.util.Map;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.ImgInfo;
import cn.com.zhihetech.online.bean.Merchant;
import cn.com.zhihetech.online.bean.User;
import cn.com.zhihetech.online.core.common.Constant;
import cn.com.zhihetech.online.core.ZhiheApplication;
import cn.com.zhihetech.online.core.util.StringUtils;


/**
 * 单人聊天界面
 * Created by ShenYunjie on 2016/2/2.
 */
public class SingleChatFragment extends EaseChatFragment {

    public final static int REQUEST_RED_ENVELOPE_CODE = 101;
    public final static int REQUEST_GOODS_LINK_CODE = 102;
    public final static int REQUEST_SECKILL_GOODS_CODE = 103;

    protected final static int ITEM_RED_ENVELOPE = 10;
    protected final static int ITEM_SHOP_LINK = 11;
    protected final static int ITEM_GOODS_LINK = 12;
    protected final static int ITEM_RED_SECKILL_GOODS = 13;

    protected int[] itemStrings = {com.easemob.easeui.R.string.attach_take_pic,
            com.easemob.easeui.R.string.attach_picture, R.string.red_envelope, R.string.shop_link,
            R.string.goods_link, R.string.seckill_goods};
    protected int[] itemdrawables = {com.easemob.easeui.R.drawable.ease_chat_takepic_selector,
            com.easemob.easeui.R.drawable.ease_chat_image_selector, R.drawable.red_envelope_icon,
            R.drawable.chat_shop_icon, R.drawable.goods_icon, R.drawable.seckill_goods_icon};
    protected int[] itemIds = {ITEM_TAKE_PICTURE, ITEM_PICTURE, ITEM_RED_ENVELOPE, ITEM_SHOP_LINK, ITEM_GOODS_LINK, ITEM_RED_SECKILL_GOODS,};

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
                return null;
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
                for (int i = 0; i < itemStrings.length; i++) {
                    inputMenu.registerExtendMenuItem(itemStrings[i], itemdrawables[i], itemIds[i], new MyItemClickListener() {
                        @Override
                        public void onClick(int itemId, View view) {
                            switch (itemId) {
                                case ITEM_SHOP_LINK:
                                    sendShopLink(merchant);
                                    return;
                            }
                            super.onClick(itemId, view);
                        }
                    });
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

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
        message.setAttribute(Constant.EXTEND_MESSAGE_TYPE, Constant.EXTEND_MESSAGE_RED_ENVELOPE);
        sendMessage(message);
    }

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
}
