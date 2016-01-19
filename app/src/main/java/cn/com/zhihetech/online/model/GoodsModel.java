package cn.com.zhihetech.online.model;

import android.support.annotation.NonNull;

import java.text.MessageFormat;

import cn.com.zhihetech.online.bean.Goods;
import cn.com.zhihetech.online.core.common.Constant;
import cn.com.zhihetech.online.core.common.Pager;
import cn.com.zhihetech.online.core.http.PageDataCallback;

/**
 * Created by ShenYunjie on 2016/1/19.
 */
public class GoodsModel extends BaseModel<Goods> {
    /**
     * 获取指定商家的商品
     *
     * @param callback
     * @param pager
     * @param merchantId 商家ID
     */
    public void getGoodsesByMerchantId(PageDataCallback<Goods> callback, Pager pager, @NonNull String merchantId) {
        String url = MessageFormat.format(Constant.MERCHANT_GOODSES_URL, merchantId);
        ModelParams params = new ModelParams().addPager(pager);
        getPageData(url, params, callback);
    }
}
