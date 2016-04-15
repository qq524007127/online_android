package cn.com.zhihetech.online.ui.fragment;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.easemob.EMNotifierEvent;
import com.easemob.chat.EMContact;
import com.easemob.chat.EMMessage;
import com.easemob.easeui.EaseConstant;

import org.xutils.ex.DbException;

import cn.com.zhihetech.online.bean.EMUserInfo;
import cn.com.zhihetech.online.core.db.DBUtils;
import cn.com.zhihetech.online.core.emchat.EMMessageHelper;
import cn.com.zhihetech.online.core.emchat.helpers.AbstractEventHandle;
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
            new AbstractEventHandle() {

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
        String toUserName = message.getChatType() == EMMessage.ChatType.Chat ? message.getFrom() :
                message.getTo();
        EMUserInfo userInfo = EMUserInfo.createEMUserInfo(message);
        saveUserInfo(userInfo);
        if (toUserName.equals(toChatUsername)) {
            messageList.refreshSelectLast();
            NotificationHelper.playRingtoneAndVibrator(getContext());
            return;
        } else {
            // 如果消息不是和当前聊天ID的消息
            Intent intent = new Intent(getContext(), SingleChatActivity.class)
                    .putExtra(EaseConstant.EXTRA_USER_ID, toUserName);
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
                    EMMessageHelper.getMessageBody(message), pendingIntent);
        }
    }

    protected void saveUserInfo(final EMUserInfo userInfo) {
        try {
            new DBUtils().saveUserInfo(userInfo);
        } catch (DbException e) {
            e.printStackTrace();
        }
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
                onReceiveNewMessage(message);
                break;
            case EventDeliveryAck:
            case EventReadAck:
                // 获取到message
                messageList.refresh();
                break;
            case EventOfflineMessage:
                messageList.refresh();
                break;
            default:
                break;
        }

    }
}
