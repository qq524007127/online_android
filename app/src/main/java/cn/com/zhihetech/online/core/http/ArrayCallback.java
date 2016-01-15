package cn.com.zhihetech.online.core.http;

import java.util.List;

/**
 * Created by ShenYunjie on 2016/1/15.
 */
public abstract class ArrayCallback<T> extends SimpleCallback {
    public abstract void onArray(List<T> datas);
}
