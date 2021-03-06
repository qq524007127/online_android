package cn.com.zhihetech.online.core.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.widget.RemoteViews;

import cn.com.zhihetech.online.R;

/**
 * Created by ShenYunjie on 2016/4/7.
 */
public class NotificationHelper {
    /**
     * 收到自定义消息后自定义显示通知
     *
     * @param context
     * @param notifyId 通知编号
     * @param title    通知标题
     * @param msg      通知内容
     */
    public static void showNotification(Context context, int notifyId, String title, String msg, PendingIntent pendingIntent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);

        // 创建一个Notification
        Notification notification = new Notification();
        // 设置显示在手机最上边的状态栏的图标
        notification.icon = R.mipmap.ic_launcher;
        // 当前的notification被放到状态栏上的时候，提示内容
        notification.tickerText = title;
        notification.flags = Notification.FLAG_AUTO_CANCEL;

        /***
         * notification.vibrate 当手机震动时，震动周期设置
         */
        // 添加声音提示
        notification.defaults = Notification.DEFAULT_ALL;
        // audioStreamType的值必须AudioManager中的值，代表着响铃的模式
        notification.audioStreamType = android.media.AudioManager.ADJUST_LOWER;

       /* Intent intent = new Intent(context, StartActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);*/

        RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.content_notification);
        contentView.setTextViewText(R.id.notification_title_tv, title);
        contentView.setTextViewText(R.id.notification_content_tv, msg);
        notification.contentView = contentView;
        notification.contentIntent = pendingIntent;
        notificationManager.notify(notifyId, notification);
    }

    /**
     * 播放提示应和振动
     *
     * @param context
     */
    public static void playRingtoneAndVibrator(Context context) {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone tone = RingtoneManager.getRingtone(context.getApplicationContext(), notification);
        tone.play();
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(new long[]{100, 400, 100, 400}, 10);
    }
}
