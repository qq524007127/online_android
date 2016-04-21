package cn.com.zhihetech.online.ui.fragment;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.controller.EaseUI;
import com.easemob.easeui.domain.EaseUser;
import com.easemob.easeui.ui.EaseConversationListFragment;

import org.xutils.ex.DbException;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.EMUserInfo;
import cn.com.zhihetech.online.core.db.DBUtils;
import cn.com.zhihetech.online.core.emchat.EMMessageHelper;
import cn.com.zhihetech.online.core.emchat.helpers.EMChatHelper;
import cn.com.zhihetech.online.core.util.NotificationHelper;
import cn.com.zhihetech.online.ui.activity.SingleChatActivity;

/**
 * Created by ShenYunjie on 2016/2/16.
 */
public class MyContactListFragment extends EaseConversationListFragment {

    private EMEventListener eventListener = new EMEventListener() {
        @Override
        public void onEvent(EMNotifierEvent emNotifierEvent) {
            switch (emNotifierEvent.getEvent()) {
                case EventNewMessage:
                    EMMessage message = (EMMessage) emNotifierEvent.getData();
                    if (message.getChatType() != EMMessage.ChatType.ChatRoom) {
                        onReceiveNewMessage(message);
                        refresh();
                    }
                    break;
            }
        }
    };

    private boolean isRegisterEventListener = false;    //是否已注册事件监听

    private boolean isNotify = true;

    @Override
    protected void initView() {
        super.initView();
        getView().findViewById(R.id.conversation_list_search_bar).setVisibility(View.GONE);
        initConversationListItemClickListener();
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
                }
                return easeUser;
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        registerEventListener();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterEventListener();
    }

    @Override
    protected void setUpView() {
        super.setUpView();
    }

    @Override
    public void onPause() {
        super.onPause();
        isNotify = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        isNotify = false;
        refresh();
    }

    /**
     * 收到新信息时回调
     *
     * @param message
     */
    private void onReceiveNewMessage(EMMessage message) {
        EMUserInfo userInfo = EMUserInfo.createEMUserInfo(message);
        saveUserInfo(userInfo);
        if (isNotify) {
            notifyNewMessage(message);
        } else {
            refresh();
            NotificationHelper.playRingtoneAndVibrator(getContext());//如果不显示通知栏则通过提示音提示
        }
    }

    private void notifyNewMessage(EMMessage message) {
        String toUserName = message.getChatType() == EMMessage.ChatType.Chat ? message.getFrom() :
                message.getTo();
        EMUserInfo userInfo = EMUserInfo.createEMUserInfo(message);
        Intent intent = new Intent(getContext(), SingleChatActivity.class);
            /*EMMessage.ChatType chatType = message.getChatType();
            if (chatType == EMMessage.ChatType.ChatRoom) {
                intent = new Intent(getContext(), ActivityChatRoomActivity.class);
                intent.putExtra(EaseConstant.EXTRA_USER_ID, this.activity.getChatRoomId());
                intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_CHATROOM);
                intent.putExtra(ActivityChatRoomActivity.CHAT_ROOM_NAME, activity.getActivitName());
                intent.putExtra(ActivityChatRoomActivity.ACTIVITY_ID, activity.getActivitId());
            }*/
        intent.putExtra(SingleChatActivity.USER_NICK_NAME_KEY, toUserName);
        PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        NotificationHelper.showNotification(getContext(), EMChatHelper.EMCHAT_NEW_MESSAGE_NOTIFY_ID,
                userInfo.getUserNick() + "发来一条新信息",
                EMMessageHelper.getMessageBody(message), null);
    }

    protected void saveUserInfo(final EMUserInfo userInfo) {
        try {
            new DBUtils().saveUserInfo(userInfo);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置会话点击事件
     */
    private void initConversationListItemClickListener() {
        setConversationListItemClickListener(new EaseConversationListItemClickListener() {
            @Override
            public void onListItemClicked(final EMConversation conversation) {
                Intent intent = new Intent(getActivity(), SingleChatActivity.class);
                intent.putExtra(EaseConstant.EXTRA_USER_ID, conversation.getUserName());
                getActivity().startActivity(intent);
                isNotify = false;
            }
        });
    }

    private void registerEventListener() {
        if (!isRegisterEventListener) {
            EMChatManager.getInstance().registerEventListener(eventListener);
            isRegisterEventListener = true;
        }
    }

    private void unregisterEventListener() {
        if (isRegisterEventListener) {
            EMChatManager.getInstance().unregisterEventListener(eventListener);
            isRegisterEventListener = false;
        }
    }
}
