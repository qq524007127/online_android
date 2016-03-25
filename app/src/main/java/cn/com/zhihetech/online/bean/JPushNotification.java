package cn.com.zhihetech.online.bean;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by ShenYunjie on 2016/3/25.
 */
public class JPushNotification extends BaseBean {

    public final static String EXTRA_JPUSH_NOTIFICATION = "_EXTRA_JPUSH_NOTIFICATION";

    private String msgId;
    private String title;
    private String alert;
    private JSONObject extra;
    private int notificationId;

    public JPushNotification() {
    }

    public JPushNotification(String msgId, String title, String alert, String extra, int notificationId) {
        this.msgId = msgId;
        this.title = title;
        this.alert = alert;
        try {
            this.extra = JSONObject.parseObject(extra);
        } catch (Exception e) {
        }
        this.notificationId = notificationId;
    }

    public JPushNotification(String msgId, String title, String alert, JSONObject extra, int notificationId) {
        this.msgId = msgId;
        this.title = title;
        this.alert = alert;
        this.extra = extra;
        this.notificationId = notificationId;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlert() {
        return alert;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }

    public JSONObject getExtra() {
        return extra;
    }

    public void setExtra(JSONObject extra) {
        this.extra = extra;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }
}
