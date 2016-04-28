package cn.com.zhihetech.online.core.emchat.helpers;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatOptions;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.OnMessageNotifyListener;
import com.easemob.chat.OnNotificationClickListener;
import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.controller.EaseUI;
import com.easemob.easeui.domain.EaseUser;

import org.xutils.ex.DbException;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.EMUserInfo;
import cn.com.zhihetech.online.core.ZhiheApplication;
import cn.com.zhihetech.online.core.common.Constant;
import cn.com.zhihetech.online.core.db.DBUtils;
import cn.com.zhihetech.online.ui.activity.SingleChatActivity;

/**
 * Created by ShenYunjie on 2016/4/6.
 */
public class EMChatHelper {

    public final static int EMCHAT_NEW_MESSAGE_NOTIFY_ID = 0X111;

    private static EMChatHelper instance;

    private EMConnectionHandle connectionHandle;
    private EMEventHandle eventHandle;

    private EMChatHelper() {
        this.connectionHandle = EMConnectionHandle.getInstance();
        this.eventHandle = EMEventHandle.getInstance();
        this.eventHandle = EMEventHandle.getInstance();
    }

    public static EMChatHelper getInstance() {
        if (instance == null) {
            EMChat.getInstance().init(ZhiheApplication.getInstance());
            instance = new EMChatHelper();
        }
        return instance;
    }

    /**
     * 添加连接监听
     *
     * @param connectionListener
     */
    public void addConnectionListener(EMConnectionHandle.OnEMConnectionListener connectionListener) {
        this.connectionHandle.addConnectionListener(connectionListener);
    }

    /**
     * 移除连接监听
     *
     * @param connectionListener
     */
    public void removeConnectionListener(EMConnectionHandle.OnEMConnectionListener connectionListener) {
        this.connectionHandle.removeConnectionListener(connectionListener);
    }

    /**
     * 添加环信事件监听
     *
     * @param eventListener
     */
    public void addEventListener(EMEventHandle.OnEMEventListener eventListener) {
        this.eventHandle.addConnectionListener(eventListener);
    }

    /**
     * 移除环信事件监听
     *
     * @param eventListener
     */
    public void removeEventListener(EMEventHandle.OnEMEventListener eventListener) {
        this.eventHandle.removeConnectionListener(eventListener);
    }



    /*=====================================初始化环信=========================================*/

    /**
     * 如果app启用了远程的service，此application:onCreate会被调用2次
     * 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
     * 默认的app会在以包名为默认的process name下运行，如果查到的process name不是app的process name就立即返回
     */
    public void initEMChat(Application application) {
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(application, pid);
        if (processAppName == null || !processAppName.equalsIgnoreCase("cn.com.zhihetech.online")) {
            Log.e("ZhiheApplication", "enter the service process!");
            return;
        }
        //EMChat.getInstance().init(application);
        EMChat.getInstance().setDebugMode(Constant.DEBUG);
        EMChat.getInstance().setAutoLogin(false);
        settingEMChatOptions(application);
        initUserProfileProvider();
        EaseUI.getInstance().init(application);
    }

    private void initUserProfileProvider() {
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
                } else {
                    EMConversation conversation = EMChatManager.getInstance().getConversation(username);
                    List<EMMessage> messages = conversation.getAllMessages();
                    if (messages != null && !messages.isEmpty()) {
                        for (int i = messages.size() - 1; i < 0; i--) {
                            EMMessage message = messages.get(i);
                            if (message.getFrom().equals(username)) {
                                EMUserInfo tmp = EMUserInfo.createEMUserInfo(message);
                                easeUser.setNick(tmp.getUserNick());
                                easeUser.setAvatar(tmp.getAvatarUrl());
                                saveUserInfo(tmp);
                                break;
                            }
                        }
                    }
                }
                return easeUser;
            }
        });
    }

    protected void saveUserInfo(final EMUserInfo userInfo) {
        try {
            new DBUtils().saveUserInfo(userInfo);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    private String getAppName(Application application, int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) application.getSystemService(Application.ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = application.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(info.processName, PackageManager.GET_META_DATA));
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }

    /**
     * 设置环信配置选项
     */
    private void settingEMChatOptions(final Context context) {
        //获取到配置options对象
        EMChatOptions options = EMChatManager.getInstance().getChatOptions();

        /**
         * 设置后台收到新消息后通知
         */
        options.setNotifyText(new OnMessageNotifyListener() {

            //设置自定义的文字提示
            @Override
            public String onNewMessageNotify(EMMessage message) {
                String msg = "{0}发来一条新消息！";
                EMUserInfo userInfo = EMUserInfo.createEMUserInfo(message);
                //可以根据message的类型提示不同文字，这里为一个简单的示例
                return MessageFormat.format(msg, userInfo.getUserNick());
            }

            @Override
            public String onLatestMessageNotify(EMMessage message, int fromUsersNum, int messageNum) {
                EMUserInfo userInfo = EMUserInfo.createEMUserInfo(message);
                return fromUsersNum + "个好友，发来了" + messageNum + "条消息！";
            }

            @Override
            public String onSetNotificationTitle(EMMessage emMessage) {
                return context.getString(R.string.you_have_new_message);
            }

            @Override
            public int onSetSmallIcon(EMMessage emMessage) {
                return R.mipmap.ic_launcher;
            }
        });

        /**
         * 设置点击新消息通知跳转界面
         */
        options.setOnNotificationClickListener(new OnNotificationClickListener() {

            @Override
            public Intent onNotificationClick(EMMessage message) {
                Intent intent = new Intent(context, SingleChatActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                EMMessage.ChatType chatType = message.getChatType();
                String toUserName;
                if (chatType == EMMessage.ChatType.Chat) { //单聊信息
                    toUserName = message.getFrom();
                } else { //群聊信息
                    toUserName = message.getTo();
                }
                intent.putExtra(EaseConstant.EXTRA_USER_ID, toUserName);
                return intent;
            }
        });
    }
}
