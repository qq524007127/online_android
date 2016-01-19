package cn.com.zhihetech.online.core.http;


/**
 * Created by ShenYunjie on 2016/1/15.
 */
public abstract class ObjectCallback<T> extends SimpleCallback {
    public abstract void onObject(T data);
}
