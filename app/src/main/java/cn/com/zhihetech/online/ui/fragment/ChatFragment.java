package cn.com.zhihetech.online.ui.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.controller.EaseUI;
import com.easemob.easeui.domain.EaseUser;
import com.easemob.easeui.ui.EaseChatFragment;
import com.easemob.easeui.widget.chatrow.EaseCustomChatRowProvider;
import com.easemob.exceptions.EaseMobException;

import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.ActivityGoods;
import cn.com.zhihetech.online.bean.ChatMessage;
import cn.com.zhihetech.online.bean.ChatUserInfo;
import cn.com.zhihetech.online.bean.EMUserInfo;
import cn.com.zhihetech.online.bean.Goods;
import cn.com.zhihetech.online.bean.ImgInfo;
import cn.com.zhihetech.online.bean.Merchant;
import cn.com.zhihetech.online.bean.RedEnvelop;
import cn.com.zhihetech.online.bean.User;
import cn.com.zhihetech.online.core.common.Constant;
import cn.com.zhihetech.online.core.ZhiheApplication;
import cn.com.zhihetech.online.core.common.PageData;
import cn.com.zhihetech.online.core.common.ResponseMessage;
import cn.com.zhihetech.online.core.common.ResponseStateCode;
import cn.com.zhihetech.online.core.db.DBUtils;
import cn.com.zhihetech.online.core.emchat.ZhiheChatRowProvider;
import cn.com.zhihetech.online.core.http.ObjectCallback;
import cn.com.zhihetech.online.core.http.PageDataCallback;
import cn.com.zhihetech.online.core.util.StringUtils;
import cn.com.zhihetech.online.model.ChatMessageModel;
import cn.com.zhihetech.online.model.RedEnvelopModel;
import cn.com.zhihetech.online.ui.activity.SeckillGoodsListActivity;
import cn.com.zhihetech.online.ui.activity.GoodsListActivity;
import cn.com.zhihetech.online.ui.activity.RedEnvelopeListActivity;


/**
 * 聊天界面
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
    protected int[] itemdrawables = {R.drawable.pick_camera_icon,
            R.drawable.pick_picture_icon, R.drawable.chat_shop_icon, R.drawable.goods_icon,
            R.drawable.red_envelop_icon, R.drawable.seckill_goods_icon};
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
                //Toast.makeText(getContext(), username, Toast.LENGTH_LONG).show();
            }

            @Override
            public boolean onMessageBubbleClick(EMMessage message) {
                return false;
            }

            @Override
            public void onMessageBubbleLongClick(EMMessage message) {
                Log.d("ChatFragment", message.toString());
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

    @Override
    protected void setUpView() {
        super.setUpView();
        /**
         * 自定义用户别名和用户头像
         */
        EaseUI.getInstance().setUserProfileProvider(new EaseUI.EaseUserProfileProvider() {
            @Override
            public EaseUser getUser(String username) {
                EaseUser easeUser = new EaseUser(username);
                EMUserInfo userInfo = null;
                try {
                    userInfo = new DBUtils().getUserInfoByUserName(username);
                } catch (DbException e) {
                    e.printStackTrace();
                }
                if (userInfo != null) {
                    easeUser.setNick(userInfo.getUserNick());
                    easeUser.setAvatar(userInfo.getAvatarUrl());
                    return easeUser;
                }
                List<EMMessage> messages = conversation.getAllMessages();
                if (messages != null && !messages.isEmpty()) {
                    for (int i = messages.size() - 1; i >= 0; i--) {
                        EMMessage message = messages.get(i);
                        String target = message.getFrom();
                        if (target.equals(username)) {
                            userInfo = EMUserInfo.createEMUserInfo(message);
                            break;
                        }
                    }
                }
                if (userInfo != null) {
                    easeUser.setNick(userInfo.getUserNick());
                    easeUser.setAvatar(userInfo.getAvatarUrl());
                    final EMUserInfo finalUserInfo = userInfo;
                    /**
                     * 另起一个线程来保存用户信息，避免引起卡顿
                     */
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            saveUserInfo(finalUserInfo);
                        }
                    }).start();
                }
                return easeUser;
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
                User user = ZhiheApplication.getInstance().getLogedUser();
                message.setAttribute(Constant.EXTEND_USER_NICK_NAME, user.getUserName());
                message.setAttribute(Constant.EXTEND_USER_ID, user.getUserId());
                ImgInfo headerImg = user.getPortrait();
                if (headerImg != null && !StringUtils.isEmpty(headerImg.getUrl())) {
                    message.setAttribute(Constant.EXTEND_USER_HEAD_IMG, headerImg.getUrl());
                }
                message.setAttribute(Constant.EXTEND_USER_TYPE, String.valueOf(Constant.EXTEND_NORMAL_USER));
                break;
            case Constant.MERCHANT_USER:
                ChatUserInfo userInfo = ZhiheApplication.getInstance().getLogedMerchant().getChatUserInfo();
                message.setAttribute(Constant.EXTEND_USER_NICK_NAME, userInfo.getNickName());
                message.setAttribute(Constant.EXTEND_USER_ID, userInfo.getAppUserId());
                if (!StringUtils.isEmpty(userInfo.getPortraitUrl())) {
                    message.setAttribute(Constant.EXTEND_USER_HEAD_IMG, userInfo.getPortraitUrl());
                }
                message.setAttribute(Constant.EXTEND_USER_TYPE, String.valueOf(Constant.EXTEND_MERCHANT_USER));
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
                case REQUEST_SECKILL_GOODS_CODE:
                    Object result2 = data.getSerializableExtra(SeckillGoodsListActivity.RESULT_ACTIVITY_GOODS_KEY);
                    if (result2 != null) {
                        sendSeckillGoods((ActivityGoods) result2);
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
        message.setAttribute(Constant.EXTEND_MESSAGE_TYPE, String.valueOf(Constant.EXTEND_MESSAGE_SHOP_LINK));
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
        message.setAttribute(Constant.EXTEND_MESSAGE_TYPE, String.valueOf(Constant.EXTEND_MESSAGE_GOODS_LINK));
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
        message.setAttribute(Constant.EXTEND_MESSAGE_TYPE, String.valueOf(Constant.EXTEND_MESSAGE_RED_ENVELOP));
        sendMessage(message);
    }

    /**
     * 发送活动（秒杀）商品链接
     *
     * @param activityGoods
     */
    protected void sendSeckillGoods(ActivityGoods activityGoods) {
        Map<String, Object> map = new HashMap<>();
        map.put("agId", activityGoods.getAgId());
        map.put("activityPrice", activityGoods.getActivityPrice());
        Goods goods = activityGoods.getGoods();
        map.put("goodsId", goods.getGoodsId());
        map.put("goodsCoverImg", goods.getCoverImg().getUrl());
        map.put("goodsPrice", goods.getPrice());
        map.put("goodsName", goods.getGoodsName());
        EMMessage message = EMMessage.createTxtSendMessage(JSONObject.toJSONString(map), toChatUsername);
        message.setAttribute(Constant.EXTEND_MESSAGE_TYPE, String.valueOf(Constant.EXTEND_MESSAGE_SECKILL_GOODS));
        sendMessage(message);
    }

    class ExtendMenuItemClickListener extends MyItemClickListener {
        private Merchant merchant;

        public ExtendMenuItemClickListener(Merchant merchant) {
            this.merchant = merchant;
        }

        @Override
        public void onClick(int itemId, View view) {
            String activityId = fragmentArgs.getString(ACTIVITY_ID_KEY);
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
                    Intent seckillGoodsIntent = new Intent(getContext(), SeckillGoodsListActivity.class);
                    seckillGoodsIntent.putExtra(SeckillGoodsListActivity.ACTIVITY_ID_KEY, activityId);
                    startActivityForResult(seckillGoodsIntent, REQUEST_SECKILL_GOODS_CODE);
                    return;
                case ITEM_RED_ENVELOPE:
                    Intent redEnvelopeIntent = new Intent(getContext(), RedEnvelopeListActivity.class);
                    redEnvelopeIntent.putExtra(RedEnvelopeListActivity.MERCHANT_ID_KEY, merchant.getMerchantId());
                    redEnvelopeIntent.putExtra(RedEnvelopeListActivity.ACTIVITY_ID_KEY, activityId);
                    startActivityForResult(redEnvelopeIntent, REQUEST_RED_ENVELOPE_CODE);
                    return;
            }
            super.onClick(itemId, view);
        }
    }

    /*====================处理加载更多消息，在原有加载本地聊天记录的基础上添加从服务器加载========================*/

    /**
     * 重写下拉加载更多聊天记录
     */
    @Override
    protected void setRefreshLayoutListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        if (listView.getFirstVisiblePosition() == 0 && !isloading && haveMoreData) {
                            if (messageList.getItem(0) == null) {
                                //loadMoreMessagesFromServer(null);
                                return;
                            }
                            List<EMMessage> messages;
                            try {
                                if (chatType == EaseConstant.CHATTYPE_SINGLE) {
                                    messages = conversation.loadMoreMsgFromDB(messageList.getItem(0).getMsgId(),
                                            pagesize);
                                } else {
                                    messages = conversation.loadMoreGroupMsgFromDB(messageList.getItem(0).getMsgId(),
                                            pagesize);
                                }
                            } catch (Exception e1) {
                                swipeRefreshLayout.setRefreshing(false);
                                return;
                            }
                            if (messages.size() > 0) {
                                messageList.refreshSeekTo(messages.size() - 1);
                                //TODO:预加载更多消息（暂时不支持,下个版本提供此功能）
                                if (messages.size() < pagesize) {
                                    //haveMoreData = false;
                                    //preLoadChatMessages();
                                }
                            } else {
                                //TODO:如果是群聊（包括聊天室）则远程加载数据;暂时不支持单聊查看离线消息(单聊导入聊天记录会出现两个会话；环信的bug）
                                if (chatType != EaseConstant.CHATTYPE_SINGLE) {
                                    loadMoreMessagesFromServer(messageList.getItem(0).getMsgId());
                                    return;
                                }
                                haveMoreData = false;
                            }

                            isloading = false;

                        } else {
                            Toast.makeText(getActivity(), getResources().getString(com.easemob.easeui.R.string.no_more_messages),
                                    Toast.LENGTH_SHORT).show();
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 600);
            }
        });
    }

    /**
     * 预加载聊天记录
     */
    private void preLoadChatMessages() {
        EMMessage message = messageList.getItem(0);
        long timestamp = message.getMsgTime();
        String from = chatType == EaseConstant.CHATTYPE_SINGLE ? ZhiheApplication.getInstance().getChatUserId() : null;
        new ChatMessageModel().getChatMessages(new PageDataCallback<ChatMessage>() {
            @Override
            public void onPageData(PageData<ChatMessage> result, List<ChatMessage> rows) {
                if (rows == null || rows.isEmpty()) {
                    haveMoreData = false;
                }
                final List<EMMessage> messages = new ArrayList<>(rows.size());
                String userId = ZhiheApplication.getInstance().getChatUserId();
                for (ChatMessage msg : rows) {
                    messages.add(msg.createEMMessage(userId));
                }
                EMChatManager.getInstance().importMessages(messages);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
            }
        }, null, toChatUsername, from);
    }

    /**
     * 从服务器加载指定消息之前更多的消息
     *
     * @param msgId 指定消息ID
     */
    private void loadMoreMessagesFromServer(String msgId) {
        String from = chatType == EaseConstant.CHATTYPE_SINGLE ? ZhiheApplication.getInstance().getChatUserId() : null;
        new ChatMessageModel().getChatMessages(new PageDataCallback<ChatMessage>() {
            @Override
            public void onPageData(PageData<ChatMessage> result, List<ChatMessage> rows) {
                if (rows == null || rows.isEmpty()) {
                    haveMoreData = false;
                    Toast.makeText(getActivity(), getResources().getString(com.easemob.easeui.R.string.no_more_messages),
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                onLoadMessageSuccess(rows);
                if (!result.hasNextPage()) {
                    haveMoreData = false;
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
            }

            @Override
            public void onFinished() {
                swipeRefreshLayout.setRefreshing(false);
                isloading = false;
            }
        }, msgId, toChatUsername, from);
    }

    /**
     * 从服务器加载更多消息成功后回调
     *
     * @param chatMessages
     */
    protected void onLoadMessageSuccess(final List<ChatMessage> chatMessages) {
        final String userId = ZhiheApplication.getInstance().getChatUserId();
        List<EMMessage> messages = new ArrayList<>(chatMessages.size());
        for (ChatMessage msg : chatMessages) {
            EMMessage message = msg.createEMMessage(userId);
            if (message != null) {
                messages.add(message);
            }
            //saveMessageExt2DB(message);
        }
        EMChatManager.getInstance().importMessages(messages);
        int messageSize;
        if (chatType == EaseConstant.CHATTYPE_SINGLE) {
            messageSize = conversation.loadMoreMsgFromDB(messageList.getItem(0).getMsgId(),
                    pagesize).size();
        } else {
            messageSize = conversation.loadMoreGroupMsgFromDB(messageList.getItem(0).getMsgId(),
                    pagesize).size();
        }
        if (messageSize > 0) {
            messageList.refreshSeekTo(messages.size() - 1);
        }
    }

    protected void saveMessageExt2DB(EMMessage message) {
        EMUserInfo userInfo = EMUserInfo.createEMUserInfo(message);
        saveUserInfo(userInfo);
    }

    protected void saveUserInfo(EMUserInfo userInfo) {
        try {
            new DBUtils().saveUserInfo(userInfo);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送消息
     *
     * @param message
     */
    @Override
    protected void sendMessage(EMMessage message) {
        if (chatFragmentListener != null) {
            //设置扩展属性
            chatFragmentListener.onSetMessageAttributes(message);
        }
        // 如果是群聊，设置chattype,默认是单聊
        if (chatType == EaseConstant.CHATTYPE_GROUP) {
            message.setChatType(EMMessage.ChatType.GroupChat);
        } else if (chatType == EaseConstant.CHATTYPE_CHATROOM) {
            message.setChatType(EMMessage.ChatType.ChatRoom);
        }
        //发送消息
        EMChatManager.getInstance().sendMessage(message, new SendMessageCallBack(message));
        //刷新ui
        messageList.refreshSelectLast();
    }

    /**
     * 信息发送失败后重发消息
     *
     * @param message
     */
    @Override
    public void resendMessage(EMMessage message) {
        message.status = EMMessage.Status.CREATE;
        EMChatManager.getInstance().sendMessage(message, new SendMessageCallBack(message));
        messageList.refresh();
    }

    /**
     * 发送消息回调
     */
    class SendMessageCallBack implements EMCallBack {
        private EMMessage message;
        private boolean isReEnvelop = false;
        private String redEnvelopId;

        public SendMessageCallBack(EMMessage message) {
            this.message = message;
            try {
                String messageType = message.getStringAttribute(Constant.EXTEND_MESSAGE_TYPE);
                if (StringUtils.isEmpty(messageType)) {
                    return;
                }
                isReEnvelop = messageType.equals(String.valueOf(Constant.EXTEND_MESSAGE_RED_ENVELOP));
                if (isReEnvelop) {
                    TextMessageBody textMessageBody = (TextMessageBody) message.getBody();
                    JSONObject jsonObject = JSONObject.parseObject(textMessageBody.getMessage());
                    redEnvelopId = jsonObject.getString("redEnvelopId");
                    isReEnvelop = StringUtils.isEmpty(redEnvelopId) ? false : true;
                }
            } catch (EaseMobException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onSuccess() {
            if (isReEnvelop) {
                onRedEnvelopSended(redEnvelopId, message);
            }
        }

        @Override
        public void onError(int i, String s) {
            if (isReEnvelop) {
                onRedEnvelopSendFailed(redEnvelopId, message);
            }
        }

        @Override
        public void onProgress(int i, String s) {

        }
    }

    /**
     * 红包信息发送成功回调
     *
     * @param redEnvelopId
     * @param message
     */
    protected void onRedEnvelopSended(String redEnvelopId, EMMessage message) {
        new RedEnvelopModel().updateSendState(new ObjectCallback<ResponseMessage>() {
            @Override
            public void onObject(ResponseMessage data) {
                if (data.getCode() == ResponseStateCode.SUCCESS) {
                    Toast.makeText(getContext(), "红包发送成功！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), data.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(getContext(), "有一个红包发送失败，用户无法领取，请重发！", Toast.LENGTH_SHORT).show();
            }
        }, redEnvelopId);
    }

    /**
     * 红包信息发送失败
     *
     * @param redEnvelopId
     * @param message
     */
    protected void onRedEnvelopSendFailed(String redEnvelopId, final EMMessage message) {
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.tip)
                .setMessage("红包发送失败，是否重发？")
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        resendMessage(message);
                    }
                }).setNegativeButton(R.string.cancel, null).show();
    }
}
