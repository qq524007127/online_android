package cn.com.zhihetech.online.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.JPushNotification;

/**
 * Created by ShenYunjie on 2016/3/25.
 */
@ContentView(R.layout.activity_push_notification_detail)
public class PushNotificationDetailActivity extends BaseActivity {

    @ViewInject(R.id.notification_detail_title_tv)
    private TextView titleTv;
    @ViewInject(R.id.notification_detail_alert__tv)
    private TextView alertTv;

    private boolean opened = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(getIntent());
        opened = true;
    }

    protected void init(Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle == null) {
            finish();
            return;
        }
        JPushNotification notification =
                (JPushNotification) bundle.getSerializable(JPushNotification.EXTRA_JPUSH_NOTIFICATION);
        if (notification == null) {
            finish();
            return;
        }
        showNotification(notification);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        init(intent);
    }

    private void showNotification(JPushNotification notification) {
        this.titleTv.setText(notification.getTitle());
        this.alertTv.setText(notification.getAlert());
    }
}
