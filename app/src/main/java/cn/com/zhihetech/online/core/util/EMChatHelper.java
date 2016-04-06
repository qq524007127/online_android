package cn.com.zhihetech.online.core.util;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.easemob.EMConnectionListener;
import com.easemob.EMError;
import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.EMMessage;
import com.easemob.util.NetUtils;

/**
 * Created by ShenYunjie on 2016/4/6.
 */
public class EMChatHelper {

    private Context context;

    private OnEMConnectionListener connectionListener;

    public EMChatHelper(Context context) {
        this.context = context;
    }

    public void setConnectionListener(OnEMConnectionListener connectionListener) {
        this.connectionListener = connectionListener;
    }

    final Handler connectionHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (connectionListener == null) {
                return;
            }
            switch (msg.what) {
                case EMError.USER_REMOVED:
                    //showMsg("当前登录账号已被删除！");
                    connectionListener.onUserRemoved();
                    break;
                case EMError.CONNECTION_CONFLICT:
                    //showMsg("当前登录账号已从其他设备登录！");
                    connectionListener.onLoginConflict();
                    break;
                default:
                    if (NetUtils.hasNetwork(context)) {
                        Toast.makeText(context, "网络连接服务器失败！", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(context, "当前网络不可用！", Toast.LENGTH_LONG).show();
                    }
            }
        }
    };

    public class EMConnectionHandle implements EMConnectionListener {

        @Override
        public void onConnected() {

        }

        @Override
        public void onDisconnected(int code) {
            Message msg = new Message();
            msg.what = code;
            connectionHandler.sendMessage(msg);
        }
    }

    private Handler enventhandler = new Handler() {


        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case EMEventHandle.NEW_MESSAGE_CODE:

                    break;
            }
        }
    };

    public static class EMEventHandle implements EMEventListener {

        /*public static final int NEW_MESSAGE_CODE = EMNotifierEvent.Event.EventNewMessage.ordinal();
        public static final int NEW_CMD_MESSAGE_CODE = EMNotifierEvent.Event.EventNewCMDMessage.ordinal();
        public static final int NEW_READED_ACK_CODE = EMNotifierEvent.Event.EventReadAck.ordinal();
        public static final int NEW_DEL_ACK_CODE = EMNotifierEvent.Event.EventDeliveryAck.ordinal();
        public static final int OFF_LINE_MESSAGES_CODE = EMNotifierEvent.Event.EventOfflineMessage.ordinal();
        public static final int NEW_CONVERS_CHANGE_CODE = EMNotifierEvent.Event.EventConversationListChanged.ordinal();
        public static final int MESSAGE_CHANGE_CODE = EMNotifierEvent.Event.EventMessageChanged.ordinal();
        public static final int LOGOUT_CODE = EMNotifierEvent.Event.EventLogout.ordinal();
        public static final int OTHERS_CODE = EMNotifierEvent.Event.EventNewMessage.ordinal();*/

        public static final int NEW_MESSAGE_CODE = 0;   // 接收新消息
        public static final int NEW_CMD_MESSAGE_CODE = 1;   //接收透传消息
        public static final int NEW_READED_ACK_CODE = 2;    //信息已读回执
        public static final int NEW_DEL_ACK_CODE = 3;   //信息已发送回执
        public static final int OFF_LINE_MESSAGES_CODE = 4; //接收离线消息
        public static final int NEW_CONVERS_CHANGE_CODE = 5;//通知会话列表通知event注册（在某些特殊情况，SDK去删除会话的时候会收到回调监听）
        public static final int MESSAGE_CHANGE_CODE = 6; //消息内容改变
        public static final int LOGOUT_CODE = 7;    //退出登录
        public static final int OTHERS_CODE = -1;

        @Override
        public void onEvent(EMNotifierEvent emNotifierEvent) {
            Message msg = new Message();
            switch (emNotifierEvent.getEvent()) {
                case EventNewMessage: // 接收新消息
                {
                    EMMessage message = (EMMessage) emNotifierEvent.getData();
                    break;
                }
                case EventDeliveryAck: {//接收已发送回执

                    break;
                }

                case EventNewCMDMessage: {//接收透传消息

                    break;
                }

                case EventReadAck: {//接收已读回执

                    break;
                }

                case EventOfflineMessage: {//接收离线消息
                    //List<EMMessage> messages = (List<EMMessage>) emNotifierEvent.getData();
                    break;
                }

                case EventConversationListChanged: {//通知会话列表通知event注册（在某些特殊情况，SDK去删除会话的时候会收到回调监听）

                    break;
                }

                default:
                    break;
            }
        }
    }

    public interface OnEMConnectionListener {
        /**
         * 用户被删除
         */
        void onUserRemoved();

        /**
         * 用户在其他设备登录
         */
        void onLoginConflict();
    }
}
