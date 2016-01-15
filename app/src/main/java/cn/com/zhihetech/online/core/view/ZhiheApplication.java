package cn.com.zhihetech.online.core.view;

import android.app.Application;

import org.xutils.x;

import cn.com.zhihetech.online.core.common.Constant;

/**
 * Created by ShenYunjie on 2016/1/15.
 */
public class ZhiheApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(Constant.DEBUG);
    }

}
