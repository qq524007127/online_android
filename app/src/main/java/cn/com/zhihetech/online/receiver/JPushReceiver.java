package cn.com.zhihetech.online.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import java.util.Date;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.JPushNotification;
import cn.com.zhihetech.online.core.common.ActivityStack;
import cn.com.zhihetech.online.ui.activity.MainActivity;
import cn.com.zhihetech.online.ui.activity.MerchantHomeActivity;
import cn.com.zhihetech.online.ui.activity.PushNotificationDetailActivity;
import cn.com.zhihetech.online.ui.activity.SearchActivity;
import cn.com.zhihetech.online.ui.activity.StartActivity;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by ShenYunjie on 2016/3/24.
 */
public class JPushReceiver extends BroadcastReceiver {

    public final static String JPUSH_NOTIFICATION_OPENED_ACTION = "JPUSH_NOTIFICATION_OPENED_ACTION";

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case "cn.jpush.android.intent.REGISTRATION":
                //Toast.makeText(context, "极光推送已注册", Toast.LENGTH_SHORT).show();
                break;
            case "cn.jpush.android.intent.UNREGISTRATION":
                //Toast.makeText(context, "极光推送已取消注册", Toast.LENGTH_SHORT).show();
                break;
            case "cn.jpush.android.intent.MESSAGE_RECEIVED":
                String title = intent.getStringExtra(JPushInterface.EXTRA_TITLE);
                String msg = intent.getStringExtra(JPushInterface.EXTRA_MESSAGE);
                String msgId = intent.getStringExtra(JPushInterface.EXTRA_MSG_ID);
                showNotification(context, msgId, title, msg);
                break;
            case "cn.jpush.android.intent.NOTIFICATION_RECEIVED":

                break;
            case "cn.jpush.android.intent.NOTIFICATION_OPENED":
                onNotificationOpened(context, intent);
                break;
            case "cn.jpush.android.intent.ACTION_RICHPUSH_CALLBACK":

                break;
            case "cn.jpush.android.intent.CONNECTION":

                break;
        }
    }

    /**
     * 当通知打开时回调
     *
     * @param context
     * @param intent
     */
    private void onNotificationOpened(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
        String alert = bundle.getString(JPushInterface.EXTRA_ALERT);
        String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
        int notificationId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
        String msgId = bundle.getString(JPushInterface.EXTRA_MSG_ID);

        JPushNotification notification = new JPushNotification(msgId, title, alert, extra, notificationId);
        bundle.putSerializable(JPushNotification.EXTRA_JPUSH_NOTIFICATION, notification);
        Intent targetIntent = new Intent(context, PushNotificationDetailActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .setAction(JPUSH_NOTIFICATION_OPENED_ACTION).putExtras(bundle);
        if (ActivityStack.getInstance().getActivities().isEmpty()) {
            Intent startIntent = new Intent(context, StartActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivities(new Intent[]{startIntent, targetIntent});
        } else {
            context.startActivity(targetIntent);
        }
    }

    /**
     * 收到自定义消息后自定义显示通知
     *
     * @param context
     * @param msgId
     * @param title
     * @param msg
     */
    protected void showNotification(Context context, String msgId, String title, String msg) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);

        // 创建一个Notification
        Notification notification = new Notification();
        // 设置显示在手机最上边的状态栏的图标
        notification.icon = R.mipmap.ic_launcher;
        // 当当前的notification被放到状态栏上的时候，提示内容
        notification.tickerText = title;
        notification.flags = Notification.FLAG_AUTO_CANCEL;

        /***
         * notification.vibrate 当手机震动时，震动周期设置
         */
        // 添加声音提示
        notification.defaults = Notification.DEFAULT_ALL;
        // audioStreamType的值必须AudioManager中的值，代表着响铃的模式
        notification.audioStreamType = android.media.AudioManager.ADJUST_LOWER;

        //下边的两个方式可以添加音乐


        Intent intent = new Intent(context, StartActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.content_notification);
        contentView.setTextViewText(R.id.notification_title_tv, title);
        contentView.setTextViewText(R.id.notification_content_tv, msg);
        notification.contentView = contentView;
        notification.contentIntent = pendingIntent;
        int notifyId = (int) new Date().getTime();
        notificationManager.notify(notifyId, notification);
    }
}
