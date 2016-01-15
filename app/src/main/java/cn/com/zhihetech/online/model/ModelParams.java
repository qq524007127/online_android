package cn.com.zhihetech.online.model;

import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ShenYunjie on 2016/1/15.
 */
public class ModelParams {
    public final static String PAGE_KEY = "page";
    public final static String ROWS_KEY = "rows";
    public final static String SEARCH_NAME_KEY = "searchName";
    public final static String SEARCH_VALUE_KEY = "searchValue";
    private Map<String, Object> params;

    public ModelParams() {
        params = new HashMap<>();
    }

    public Map<String, Object> getParams() {
        return this.getParams();
    }

    public void addParam(@NonNull String paramKey, Object paramValue) {
        params.put(paramKey, paramValue);
    }
}
