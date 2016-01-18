package cn.com.zhihetech.online.core.http;

import cn.com.zhihetech.online.core.common.ResponseMessage;

/**
 * Created by Administrator on 2016-01-16.
 */
public abstract class ReponseMessageCallback<T> extends SimpleCallback {
    public abstract void onResponseMessage(ResponseMessage<T> responseMessage);
}
