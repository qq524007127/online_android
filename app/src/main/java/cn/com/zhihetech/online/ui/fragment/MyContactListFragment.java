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

import java.util.List;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.Conversation;
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

    public enum PromptNotifyType {
        promptNotifiy,    //提示并显示通知
        onlyNotify, //仅通知不提示
        onlyPrompt, //仅提示，不显示通知
        none    //不提示并且不通知
    }

    private EMEventListener eventListener = new EMEventListener() {
        @Override
        public void onEvent(EMNotifierEvent emNotifierEvent) {
            switch (emNotifierEvent.getEvent()) {
                case EventNewMessage:
                    EMMessage message = (EMMessage) emNotifierEvent.getData();
                    if (prompt == PromptNotifyType.onlyPrompt) {
                        refresh();
                    }
                    onReceiveNewMessage(message);
                    break;
            }
        }
    };

    private boolean isRegisterEventListener = false;    //是否已注册事件监听

    private PromptNotifyType prompt = PromptNotifyType.promptNotifiy;   //默认显示通知提示

    @Override
    protected void initView() {
        super.initView();
        getView().findViewById(R.id.conversation_list_search_bar).setVisibility(View.GONE);
        initConversationListItemClickListener();
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
    public void onPause() {
        super.onPause();
        prompt = PromptNotifyType.promptNotifiy;
    }

    @Override
    public void onResume() {
        super.onResume();
        prompt = PromptNotifyType.onlyPrompt;
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
        if (message.getChatType() == EMMessage.ChatType.ChatRoom) {
            return;
        }
        if (prompt == PromptNotifyType.promptNotifiy) {
            notifyNewMessage(message);
        } else if (prompt == PromptNotifyType.onlyPrompt) {
            NotificationHelper.playRingtoneAndVibrator(getContext());//如果不显示通知栏则通过提示音提示
        }
    }

    private void notifyNewMessage(EMMessage message) {
        String toUserName = message.getChatType() == EMMessage.ChatType.Chat ? message.getFrom() :
                message.getTo();
        EMUserInfo userInfo = EMUserInfo.createEMUserInfo(message);
        Intent intent = new Intent(getContext(), SingleChatActivity.class)
                //.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .putExtra(EaseConstant.EXTRA_USER_ID, toUserName);
        PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        NotificationHelper.showNotification(getContext(), EMChatHelper.EMCHAT_NEW_MESSAGE_NOTIFY_ID,
                userInfo.getUserNick() + "发来一条新信息",
                EMMessageHelper.getMessageBody(message), pendingIntent);
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
                prompt = PromptNotifyType.none;
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
