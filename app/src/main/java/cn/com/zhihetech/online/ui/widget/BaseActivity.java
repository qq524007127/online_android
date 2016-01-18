package cn.com.zhihetech.online.ui.widget;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.xutils.x;

import cn.com.zhihetech.online.core.common.ActivityStack;
import cn.com.zhihetech.online.core.common.Constant;

/**
 * Created by ShenYunjie on 2016/1/15.
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityStack.getInstance().addActivity(this);
        x.view().inject(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityStack.getInstance().removeActivity(this);
    }

    protected String getTag() {
        return "test";
        //return this.getClass().getName();
    }

    protected void log(String msg) {
        if (Constant.DEBUG) {
            log(getTag(), msg);
        }
    }

    protected void log(String tag, String msg) {
        if (Constant.DEBUG) {
            Log.d(tag, msg);
        }
    }

    protected void onHttpError(Throwable ex, boolean isOnCallback) {
        if (Constant.DEBUG) {
            if (ex != null && !isOnCallback) {
                ex.printStackTrace();
            }
        }
    }
}
