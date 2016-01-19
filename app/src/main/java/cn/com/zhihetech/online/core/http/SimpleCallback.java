package cn.com.zhihetech.online.core.http;

import android.util.Log;

import org.xutils.common.Callback;

import cn.com.zhihetech.online.core.common.Constant;

/**
 * Created by ShenYunjie on 2016/1/15.
 */
public abstract class SimpleCallback implements RequestCallback {
    @Override
    public void onSuccess(String result) {
        if (Constant.DEBUG) {
            Log.d("SimpleCallback", result);
        }
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {

    }

    @Override
    public void onCancelled(Callback.CancelledException cex) {

    }

    @Override
    public void onFinished() {

    }
}
