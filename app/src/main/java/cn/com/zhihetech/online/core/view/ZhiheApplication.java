package cn.com.zhihetech.online.core.view;

import android.app.ActivityManager;
import android.app.Application;
import android.content.pm.PackageManager;
import android.util.Log;

import com.easemob.chat.EMChat;

import org.xutils.x;

import java.util.Iterator;
import java.util.List;

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
        initEMChat();
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

    /**
     * 如果app启用了远程的service，此application:onCreate会被调用2次
     * 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
     * 默认的app会在以包名为默认的process name下运行，如果查到的process name不是app的process name就立即返回
     */
    private void initEMChat() {
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        if (processAppName == null || !processAppName.equalsIgnoreCase("cn.com.zhihetech.online")) {
            Log.e("ZhiheApplication", "enter the service process!");
            return;
        }
        EMChat.getInstance().init(this);
        EMChat.getInstance().setDebugMode(Constant.DEBUG);
        EMChat.getInstance().setAutoLogin(true);
    }

    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(info.processName, PackageManager.GET_META_DATA));
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }
}
