package cn.com.zhihetech.online.core.common;

import android.app.Activity;

import java.util.Stack;

/**
 * Created by ShenYunjie on 2016/1/15.
 */
public class ActivityStack {
    private Stack<Activity> activityStack;
    private static ActivityStack instance;

    private ActivityStack() {
        activityStack = new Stack<>();
    }

    /**
     * 获取单例
     *
     * @return
     */
    public static ActivityStack getInstance() {
        if (instance == null) {
            instance = new ActivityStack();
        }
        return instance;
    }

    /**
     * 加入Activity
     *
     * @return
     */
    public void addActivity(Activity activity) {
        activityStack.push(activity);
    }

    /**
     * 结束所有的Activity
     */
    public void clearActivity() {
        for (Activity activity : activityStack) {
            activity.finish();
        }
        activityStack.clear();
    }

    /**
     * 移除指定的Activity
     *
     * @param activity
     */
    public void removeActivity(Activity activity) {
        activityStack.remove(activity);
    }
}
