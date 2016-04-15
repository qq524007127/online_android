package cn.com.zhihetech.online.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import cn.com.zhihetech.online.core.common.ServiceStack;

/**
 * Created by ShenYunjie on 2016/4/6.
 */
public class BaseService extends Service {

    protected BaseService self;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ServiceStack.getInstance().addService(this);
        this.self = this;
    }

    @Override
    public void onDestroy() {
        ServiceStack.getInstance().removeService(this);
        super.onDestroy();
    }

    protected void showMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    protected void showMsg(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_LONG).show();
    }

    /**
     * 是否是后台服务，如果是后台服务则退出程序后依然在后台运行
     *
     * @return boolean true:后台服务；false:不是后台服务
     */
    public boolean isBackService() {
        return false;
    }
}
