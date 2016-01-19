package cn.com.zhihetech.online.core.view;

import android.app.Application;

import org.xutils.x;

import cn.com.zhihetech.online.core.common.Constant;

/**
 * Created by ShenYunjie on 2016/1/15.
 */
public class ZhiheApplication extends Application implements Thread.UncaughtExceptionHandler {

    private static ZhiheApplication instance;
    private String userId = "647b184e-e45d-40c1-aa35-518cb118f479";

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(Constant.DEBUG);
        instance = this;
    }

    public static ZhiheApplication getInstandce() {
        return instance;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {

    }
}
