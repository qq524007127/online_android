package cn.com.zhihetech.online.model;

import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

import cn.com.zhihetech.online.core.common.Pager;

/**
 * Created by ShenYunjie on 2016/1/15.
 */
public class ModelParams {
    public final static String PAGE_KEY = "page";
    public final static String ROWS_KEY = "rows";
    public final static String SEARCH_NAME_KEY = "searchName";
    public final static String SEARCH_VALUE_KEY = "searchValue";
    private Map<String, String> params;

    public ModelParams() {
        params = new HashMap<>();
    }

    public Map<String, String> getParams() {
        return this.params;
    }

    public ModelParams addParam(@NonNull String paramKey, String paramValue) {
        params.put(paramKey, paramValue);
        return this;
    }

    public ModelParams addPager(@NonNull Pager pager) {
        if (pager == null) {
            throw new RuntimeException("pager param is not able null");
        }
        params.put(PAGE_KEY, String.valueOf(pager.getPage()));
        params.put(ROWS_KEY, String.valueOf(pager.getRows()));
        return this;
    }
}
