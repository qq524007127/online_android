package cn.com.zhihetech.online.core.emchat.helpers;

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
    }

    public static EMChatHelper getInstance() {
        if (instance == null) {

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
}
