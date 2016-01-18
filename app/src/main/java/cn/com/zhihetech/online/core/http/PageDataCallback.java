package cn.com.zhihetech.online.core.http;

import java.util.List;

import cn.com.zhihetech.online.core.common.PageData;

/**
 * Created by Administrator on 2016-01-16.
 */
public abstract class PageDataCallback<T> extends SimpleCallback {
    public abstract void onPageData(PageData<T> result, List<T> rows);
}
