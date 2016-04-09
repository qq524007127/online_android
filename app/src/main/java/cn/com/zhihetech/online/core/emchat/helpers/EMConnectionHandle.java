package cn.com.zhihetech.online.core.emchat.helpers;

import android.os.Handler;
import android.os.Message;

import com.easemob.EMConnectionListener;
import com.easemob.EMError;
import com.easemob.chat.EMChatManager;
import com.easemob.util.NetUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.com.zhihetech.online.core.ZhiheApplication;

/**
 * Created by ShenYunjie on 2016/4/7.
 */
public class EMConnectionHandle implements EMConnectionListener {

    public final static int CONNECTED_CODE = 0x200;

    private List<OnEMConnectionListener> connectionListeners = new ArrayList<>();
    private static EMConnectionHandle instance;

    private EMConnectionHandle() {
        EMChatManager.getInstance().addConnectionListener(this);
    }

    public static EMConnectionHandle getInstance() {
        if (instance == null) {
            instance = new EMConnectionHandle();
        }
        return instance;
    }

    private final Handler connectionHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (isEmpty()) {
                return;
            }
            switch (msg.what) {
                case CONNECTED_CODE:
                    onConnectoinSuccess();
                    break;
                case EMError.USER_REMOVED:
                    onUserRemoved();
                    break;
                case EMError.CONNECTION_CONFLICT:
                    onLoginConflict();
                    break;
                default:
                    if (NetUtils.hasNetwork(ZhiheApplication.getInstance())) {
                        onConnctionError(true);
                    } else {
                        onConnctionError(true);
                    }
            }
        }
    };

    /**
     * 连接失败
     *
     * @param hasNetwork 网络是否可用
     */
    private void onConnctionError(boolean hasNetwork) {
        int currentMaxLevel = -1;
        for (OnEMConnectionListener listener : this.connectionListeners) {
            if (currentMaxLevel > listener.getLevel()) {
                break;
            }
            if (listener.onConnectionError(hasNetwork)) {
                currentMaxLevel = listener.getLevel();
            }
        }
    }

    /**
     * 连接登录成功
     */
    private void onConnectoinSuccess() {
        int currentMaxLevel = -1;
        for (OnEMConnectionListener listener : this.connectionListeners) {
            if (currentMaxLevel > listener.getLevel()) {
                break;
            }
            if (listener.onConnected()) {
                currentMaxLevel = listener.getLevel();
            }
        }
    }

    /**
     * 从其它设备登录
     */
    private void onLoginConflict() {
        int currentMaxLevel = -1;
        for (OnEMConnectionListener listener : this.connectionListeners) {
            if (currentMaxLevel > listener.getLevel()) {
                break;
            }
            if (listener.onLoginConflict()) {
                currentMaxLevel = listener.getLevel();
            }
        }
    }

    /**
     * 用户被删除
     */
    private void onUserRemoved() {
        int currentMaxLevel = -1;
        for (OnEMConnectionListener listener : this.connectionListeners) {
            if (currentMaxLevel > listener.getLevel()) {
                break;
            }
            if (listener.onUserRemoved()) {
                currentMaxLevel = listener.getLevel();
            }
        }
    }

    protected boolean isEmpty() {
        return this.connectionListeners == null || this.connectionListeners.isEmpty();
    }

    /**
     * 添加监听
     *
     * @param connectionListener
     */
    public void addConnectionListener(OnEMConnectionListener connectionListener) {
        if (connectionListener != null) {
            if (connectionListener.getLevel() < 0) {
                throw new RuntimeException("level must be more than zero"); //级别必须大于或等于0
            }
            this.connectionListeners.add(connectionListener);
        }
        sortListener();
    }

    /**
     * 移除（取消）监听
     *
     * @param connectionListener
     */
    public void removeConnectionListener(OnEMConnectionListener connectionListener) {
        if (connectionListener != null) {
            this.connectionListeners.remove(connectionListener);
        }
        sortListener();
    }

    protected void sortListener() {
        if (this.connectionListeners == null || this.connectionListeners.size() <= 1) {
            return;
        }
        Collections.sort(this.connectionListeners, new Comparator<OnEMConnectionListener>() {
            @Override
            public int compare(OnEMConnectionListener lhs, OnEMConnectionListener rhs) {
                return rhs.getLevel() - lhs.getLevel();
            }
        });
    }

    @Override
    public void onConnected() {
        Message msg = new Message();
        msg.what = CONNECTED_CODE;
        connectionHandler.sendMessage(msg);
    }

    @Override
    public void onDisconnected(int code) {
        Message msg = new Message();
        msg.what = code;
        connectionHandler.sendMessage(msg);
    }

    public interface OnEMConnectionListener {

        /**
         * 监听器等级，等级越大就最先执行
         *
         * @return Integer 不能小于0
         */
        int getLevel();

        /**
         * 连接服务器成功之后回调
         *
         * @return
         */
        boolean onConnected();

        /**
         * 用户被删除
         *
         * @return boolean 是否消耗，true:已消耗，比他小的等级不会执行
         */
        boolean onUserRemoved();

        /**
         * 用户在其他设备登录
         */
        boolean onLoginConflict();

        /**
         * 连接失败
         *
         * @param hasNetwork 是否已连接网络
         */
        boolean onConnectionError(boolean hasNetwork);
    }

    public static abstract class AbstractConnectionHandle implements OnEMConnectionListener {

        @Override
        public abstract int getLevel();

        @Override
        public boolean onConnected() {
            return true;
        }

        @Override
        public abstract boolean onUserRemoved();

        @Override
        public abstract boolean onLoginConflict();

        @Override
        public boolean onConnectionError(boolean hasNetwork) {
            return true;
        }
    }
}
