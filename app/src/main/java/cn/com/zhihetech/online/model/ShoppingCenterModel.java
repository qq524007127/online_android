package cn.com.zhihetech.online.model;

import org.xutils.common.Callback;

import cn.com.zhihetech.online.bean.ShoppingCenter;
import cn.com.zhihetech.online.core.common.Constant;
import cn.com.zhihetech.online.core.common.Pager;
import cn.com.zhihetech.online.core.http.PageDataCallback;

/**
 * Created by ShenYunjie on 2016/3/10.
 */
public class ShoppingCenterModel extends BaseModel<ShoppingCenter> {

    /**
     * 分页获取购物中心数据列表
     *
     * @param callback
     * @param pager
     * @return
     */
    public Callback.Cancelable getShoppingCenters(PageDataCallback<ShoppingCenter> callback, Pager pager) {
        return getPageData(Constant.SHOPPING_CENTERS_URL, new ModelParams().addPager(pager), callback);
    }
}
