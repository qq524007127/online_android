package cn.com.zhihetech.online.ui.fragment;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.easemob.EMNotifierEvent;
import com.easemob.chat.EMMessage;

import org.xutils.ex.DbException;

import cn.com.zhihetech.online.bean.EMUserInfo;
import cn.com.zhihetech.online.core.db.DBUtils;
import cn.com.zhihetech.online.core.emchat.EMMessageHelper;
import cn.com.zhihetech.online.core.emchat.helpers.EMChatHelper;
import cn.com.zhihetech.online.core.emchat.helpers.EMEventHandle;
import cn.com.zhihetech.online.core.util.NotificationHelper;
import cn.com.zhihetech.online.ui.activity.SingleChatActivity;

/**
 * 聊天界面（添加自定义显示通知）
 * <p/>
 * Created by ShenYunjie on 2016/4/7.
 */
public class UpgradeChatFragment extends ChatFragment {

    protected EMEventHandle.OnEMEventListener eventListener =
            new EMEventHandle.AbstractEventHandle() {

                @Override
                public int getLevel() {
                    return 10;
                }

                @Override
                public boolean onNewCMDMessage(EMMessage message) {
                    onReceiveNewCMDMessage(message);
                    return true;
                }

                @Override
                public boolean onNewMessage(EMMessage message) {
                    EMUserInfo userInfo = EMUserInfo.createEMUserInfo(message);
                    saveUserInfo(userInfo);
                    saveUserInfo(userInfo);
                    if (message.getFrom().equals(toChatUsername)) {
                        return true;
                    }
                    onReceiveNewMessage(message);
                    return true;
                }
            };

    private void onReceiveNewCMDMessage(EMMessage message) {
        EMUserInfo userInfo = EMUserInfo.createEMUserInfo(message);
        saveUserInfo(userInfo);
    }

    private void onReceiveNewMessage(EMMessage message) {
        EMUserInfo userInfo = EMUserInfo.createEMUserInfo(message);
        saveUserInfo(userInfo);
        Intent intent = new Intent(getContext(), SingleChatActivity.class);
        /*EMMessage.ChatType chatType = message.getChatType();
        if (chatType == EMMessage.ChatType.ChatRoom) {
            intent = new Intent(getContext(), ActivityChatRoomActivity.class);
            intent.putExtra(EaseConstant.EXTRA_USER_ID, this.activity.getChatRoomId());
            intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_CHATROOM);
            intent.putExtra(ActivityChatRoomActivity.CHAT_ROOM_NAME, activity.getActivitName());
            intent.putExtra(ActivityChatRoomActivity.ACTIVITY_ID, activity.getActivitId());
        }*/

        String toUserName = message.getChatType() == EMMessage.ChatType.Chat ? message.getFrom() :
                message.getTo();
        intent.putExtra(SingleChatActivity.USER_NAME_KEY, toUserName);
        PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        NotificationHelper.showNotification(getContext(), EMChatHelper.EMCHAT_NEW_MESSAGE_NOTIFYID,
                userInfo.getUserNick() + "发来一条新信息",
                EMMessageHelper.getMessageBody(message), pendingIntent);
    }

    protected void saveUserInfo(final EMUserInfo userInfo) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                try {
                    new DBUtils().saveUserInfo(userInfo);
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EMChatHelper.getInstance().addEventListener(eventListener);
    }

    @Override
    public void onDestroy() {
        EMChatHelper.getInstance().removeEventListener(eventListener);
        super.onDestroy();
    }

    /**
     * 事件监听,registerEventListener后的回调事件
     * <p/>
     * see {@link EMNotifierEvent}
     */
    @Override
    public void onEvent(EMNotifierEvent event) {
        switch (event.getEvent()) {
            case EventNewMessage:
                // 获取到message
                EMMessage message = (EMMessage) event.getData();

                String username = null;
                // 群组消息
                if (message.getChatType() == EMMessage.ChatType.GroupChat || message.getChatType() == EMMessage.ChatType.ChatRoom) {
                    username = message.getTo();
                } else {
                    // 单聊消息
                    username = message.getFrom();
                }

                // 如果是当前会话的消息，刷新聊天页面
                if (username.equals(toChatUsername)) {
                    messageList.refreshSelectLast();
                    // 声音和震动提示有新消息
                    //EaseUI.getInstance().getNotifier().viberateAndPlayTone(message);
                } else {
                    // 如果消息不是和当前聊天ID的消息
                    //EaseUI.getInstance().getNotifier().onNewMsg(message);
                }

                break;
            case EventDeliveryAck:
            case EventReadAck:
                // 获取到message
                messageList.refresh();
                break;
            case EventOfflineMessage:
                // a list of offline messages
                // List<EMMessage> offlineMessages = (List<EMMessage>)
                // event.getData();
                messageList.refresh();
                break;
            default:
                break;
        }

    }
}
