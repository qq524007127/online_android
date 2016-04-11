package cn.com.zhihetech.online.core.service;

import android.app.PendingIntent;
import android.content.Intent;

import com.easemob.chat.EMMessage;

import cn.com.zhihetech.online.bean.EMUserInfo;
import cn.com.zhihetech.online.core.emchat.EMMessageHelper;
import cn.com.zhihetech.online.core.emchat.helpers.AbstractEventHandle;
import cn.com.zhihetech.online.core.emchat.helpers.EMChatHelper;
import cn.com.zhihetech.online.core.emchat.helpers.EMEventHandle;
import cn.com.zhihetech.online.core.util.NotificationHelper;
import cn.com.zhihetech.online.ui.activity.SingleChatActivity;

/**
 * Created by ShenYunjie on 2016/4/7.
 */
public class EMChatEventService extends BaseService {

    private static boolean stoped = false;

    private EMEventHandle.OnEMEventListener eventListener = new AbstractEventHandle() {
        @Override
        public int getLevel() {
            return 1;
        }

        @Override
        public boolean onNewMessage(EMMessage message) {
            if (stoped) {
                return false;
            }
            EMUserInfo userinfo = EMUserInfo.createEMUserInfo(message);
            PendingIntent pendingIntent = null;
            if (message.getChatType() == EMMessage.ChatType.Chat) {
                String toUserName = message.getChatType() == EMMessage.ChatType.Chat ? message.getFrom() :
                        message.getTo();
                Intent intent = new Intent(self, SingleChatActivity.class)
                        .putExtra(SingleChatActivity.USER_NAME_KEY, toUserName);
                pendingIntent = PendingIntent.getActivity(self, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            }
            NotificationHelper.showNotification(self, EMChatHelper.EMCHAT_NEW_MESSAGE_NOTIFY_ID,
                    userinfo.getUserNick() + "发来一条新信息",
                    EMMessageHelper.getMessageBody(message), pendingIntent);
            return true;
        }

        @Override
        public boolean onNewCMDMessage(EMMessage message) {
            return false;
        }
    };

    public static void setStoped(boolean stop) {
        stoped = stop;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initEMChatEvent();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EMChatHelper.getInstance().removeEventListener(eventListener);
    }

    private void initEMChatEvent() {
        EMChatHelper helper = EMChatHelper.getInstance();
        helper.addEventListener(eventListener);
    }

    @Override
    public boolean isBackService() {
        return super.isBackService();
    }
}
