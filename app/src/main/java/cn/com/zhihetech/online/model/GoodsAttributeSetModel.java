package cn.com.zhihetech.online.model;

import org.xutils.common.Callback;

import cn.com.zhihetech.online.bean.GoodsAttributeSet;
import cn.com.zhihetech.online.core.common.Constant;
import cn.com.zhihetech.online.core.common.Pager;
import cn.com.zhihetech.online.core.http.PageDataCallback;

/**
 * Created by ShenYunjie on 2016/1/21.
 */
public class GoodsAttributeSetModel extends BaseModel<GoodsAttributeSet> {
    public Callback.Cancelable getCategories(PageDataCallback<GoodsAttributeSet> callback, Pager pager) {
        ModelParams params = new ModelParams().addPager(pager);
        return getPageData(Constant.CATEGORIES_URL, params, callback);
    }
}
