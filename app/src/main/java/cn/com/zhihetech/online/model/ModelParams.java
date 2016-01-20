package cn.com.zhihetech.online.model;

import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

import cn.com.zhihetech.online.core.common.Pager;
import cn.com.zhihetech.online.core.util.StringUtils;
import cn.com.zhihetech.online.core.view.SortTabView;

/**
 * Created by ShenYunjie on 2016/1/15.
 */
public class ModelParams {
    public final static String PAGE_KEY = "page";
    public final static String ROWS_KEY = "rows";
    public final static String SEARCH_NAME_KEY = "searchName";
    public final static String SEARCH_VALUE_KEY = "searchValue";
    public final static String SORT_NAME_KEY = "sort";
    public final static String SORT_TYPE_KEY = "order";
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

    public ModelParams clearPager() {
        params.remove(PAGE_KEY);
        params.remove(ROWS_KEY);
        return this;
    }

    public ModelParams addSearcher(@NonNull String searchName, String searchValue) {
        params.put(SEARCH_NAME_KEY, searchName);
        params.put(SEARCH_VALUE_KEY, searchValue);
        return this;
    }

    public ModelParams clearSearcher() {
        params.remove(SEARCH_NAME_KEY);
        params.remove(SEARCH_VALUE_KEY);
        return this;
    }

    public ModelParams addSort(@NonNull String sortName, SortTabView.OrderType order) {
        params.put(SORT_NAME_KEY, sortName);
        params.put(SORT_TYPE_KEY, StringUtils.Object2String(order));
        return this;
    }

    public ModelParams clearSort() {
        params.remove(SORT_NAME_KEY);
        params.remove(SORT_TYPE_KEY);
        return this;
    }
}
