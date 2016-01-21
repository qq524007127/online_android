package cn.com.zhihetech.online.model;

import android.support.annotation.NonNull;

import org.xutils.common.Callback;

import java.text.MessageFormat;

import cn.com.zhihetech.online.bean.Goods;
import cn.com.zhihetech.online.core.common.Constant;
import cn.com.zhihetech.online.core.http.PageDataCallback;
import cn.com.zhihetech.online.core.http.ResponseMessageCallback;

/**
 * Created by ShenYunjie on 2016/1/19.
 */
public class GoodsModel extends BaseModel<Goods> {
    /**
     * 获取指定类别的商品
     *
     * @param callback
     * @param params
     * @param categoryId 商品类别ID
     */
    public Callback.Cancelable getGoodsesByCategoryId(PageDataCallback<Goods> callback, ModelParams params, @NonNull String categoryId) {
        String url = MessageFormat.format(Constant.CATEGORY_GOODSES_URL, categoryId);
        return getPageData(url, params, callback);
    }

    /**
     * 获取指定商家的商品
     *
     * @param callback
     * @param params
     * @param merchantId 商家ID
     */
    public Callback.Cancelable getGoodsesByMerchantId(PageDataCallback<Goods> callback, ModelParams params, @NonNull String merchantId) {
        String url = MessageFormat.format(Constant.MERCHANT_GOODSES_URL, merchantId);
        return getPageData(url, params, callback);
    }

    /**
     * 获取指定ID的商品
     *
     * @param callback
     * @param goodsId
     * @return
     */
    public Callback.Cancelable getGoodsByGoodsId(ResponseMessageCallback<Goods> callback, @NonNull String goodsId) {
        String url = MessageFormat.format(Constant.GOODS_URL, goodsId);
        return getResponseMessage(url, null, callback);
    }
}
