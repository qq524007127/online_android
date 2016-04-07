package cn.com.zhihetech.online.core.emchat.helpers;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.EMMessage;

import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.com.zhihetech.online.bean.EMUserInfo;
import cn.com.zhihetech.online.core.db.DBUtils;

/**
 * Created by ShenYunjie on 2016/4/7.
 */
public class EMEventHandle implements EMEventListener {

    public static final int NEW_MESSAGE_CODE = 0;   // 接收新消息
    public static final int NEW_CMD_MESSAGE_CODE = 1;   //接收透传消息
    public static final int NEW_READED_ACK_CODE = 2;    //信息已读回执
    public static final int NEW_DEL_ACK_CODE = 3;   //信息已发送回执
    public static final int OFF_LINE_MESSAGES_CODE = 4; //接收离线消息
    public static final int NEW_CONVERS_CHANGE_CODE = 5;//通知会话列表通知event注册（在某些特殊情况，SDK去删除会话的时候会收到回调监听）
    public static final int MESSAGE_CHANGE_CODE = 6; //消息内容改变
    public static final int LOGOUT_CODE = 7;    //退出登录
    public static final int OTHERS_CODE = -0x400;

    private List<OnEMEventListener> eventListeners = new ArrayList<>();
    private static EMEventHandle instance;

    private EMEventHandle() {
    }

    public static EMEventHandle getInstance() {
        if (instance == null) {
            instance = new EMEventHandle();
        }
        return instance;
    }

    private final Handler eventHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (isEmpty()) {
                return;
            }
            switch (msg.what) {
                case EMEventHandle.NEW_MESSAGE_CODE:
                    onNewMessage((EMMessage) msg.obj);
                    break;
                case EMEventHandle.NEW_CMD_MESSAGE_CODE:
                    onNewCMDMessage((EMMessage) msg.obj);
                    break;
                case EMEventHandle.NEW_READED_ACK_CODE:

                    break;
                case EMEventHandle.NEW_DEL_ACK_CODE:

                    break;
                case EMEventHandle.OFF_LINE_MESSAGES_CODE:

                    break;
                case EMEventHandle.NEW_CONVERS_CHANGE_CODE:

                    break;
                case EMEventHandle.MESSAGE_CHANGE_CODE:

                    break;
                case EMEventHandle.LOGOUT_CODE:

                    break;
                case EMEventHandle.OTHERS_CODE:

                    break;
            }
        }
    };

    /**
     * 收到新透传信息
     *
     * @param message
     */
    private void onNewCMDMessage(EMMessage message) {
        int currentMaxLevel = -1;
        saveEMUserInfo(message);
        for (OnEMEventListener listener : this.eventListeners) {
            if (currentMaxLevel > listener.getLevel()) {
                break;
            }
            if (listener.onNewCMDMessage(message)) {
                currentMaxLevel = listener.getLevel();
            }
        }
    }

    /**
     * 接收到新信息
     *
     * @param message
     */
    private void onNewMessage(EMMessage message) {
        int currentMaxLevel = -1;
        saveEMUserInfo(message);
        for (OnEMEventListener listener : this.eventListeners) {
            if (currentMaxLevel > listener.getLevel()) {
                break;
            }
            if (listener.onNewMessage(message)) {
                currentMaxLevel = listener.getLevel();
            }
        }
    }

    private void saveEMUserInfo(EMMessage message) {
        final EMUserInfo userInfo = EMUserInfo.createEMUserInfo(message);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                DBUtils dbUtils = new DBUtils();
                try {
                    dbUtils.saveUserInfo(userInfo);
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onEvent(EMNotifierEvent emNotifierEvent) {
        Message msg = new Message();
        switch (emNotifierEvent.getEvent()) {
            case EventNewMessage: // 接收新消息
            {
                EMMessage message = (EMMessage) emNotifierEvent.getData();
                msg.what = NEW_MESSAGE_CODE;
                msg.obj = message;
                break;
            }
            case EventDeliveryAck: {//接收已发送回执

                break;
            }

            case EventNewCMDMessage: {//接收透传消息

                break;
            }

            case EventReadAck: {//接收已读回执
                EMMessage message = (EMMessage) emNotifierEvent.getData();
                msg.what = NEW_CMD_MESSAGE_CODE;
                msg.obj = message;
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
        eventHandler.sendMessage(msg);
    }

    protected boolean isEmpty() {
        return this.eventListeners == null || this.eventListeners.isEmpty();
    }

    /**
     * 添加监听
     *
     * @param eventListener
     */
    public void addConnectionListener(OnEMEventListener eventListener) {
        if (eventListener != null) {
            if (eventListener.getLevel() < 0) {
                throw new RuntimeException("level must be more than zero"); //级别必须大于或等于0
            }
            this.eventListeners.add(eventListener);
        }
        sortListener();
    }

    /**
     * 移除（取消）监听
     *
     * @param eventListener
     */
    public void removeConnectionListener(OnEMEventListener eventListener) {
        if (eventListener != null) {
            this.eventListeners.remove(eventListener);
        }
        sortListener();
    }

    protected void sortListener() {
        if (this.eventListeners == null || this.eventListeners.size() <= 1) {
            return;
        }
        Collections.sort(this.eventListeners, new Comparator<OnEMEventListener>() {
            @Override
            public int compare(OnEMEventListener lhs, OnEMEventListener rhs) {
                return rhs.getLevel() - lhs.getLevel();
            }
        });
    }

    public interface OnEMEventListener {

        /**
         * 监听器等级，等级越大就最先执行
         *
         * @return Integer 不能小于0
         */
        int getLevel();

        /**
         * 接收到新信息
         *
         * @param message
         * @return
         */
        boolean onNewMessage(EMMessage message);

        /**
         * 接收到新的透传消息
         *
         * @param message
         * @return
         */
        boolean onNewCMDMessage(EMMessage message);
    }

    public static abstract class AbstractEventHandle implements OnEMEventListener {

        @Override
        public abstract int getLevel();

        @Override
        public boolean onNewMessage(EMMessage message) {
            return true;
        }

        @Override
        public boolean onNewCMDMessage(EMMessage message) {
            return true;
        }
    }
}
