package cn.com.zhihetech.online.core.http;

import org.xutils.common.Callback;

/**
 * Created by ShenYunjie on 2016/1/15.
 */
public interface RequestCallback {
    void onSuccess(String result);

    void onError(Throwable ex, boolean isOnCallback);

    void onCancelled(Callback.CancelledException cex);

    void onFinished();
}
