package cn.com.zhihetech.online.model;

import android.support.annotation.NonNull;

import org.xutils.common.Callback;

import java.text.MessageFormat;

import cn.com.zhihetech.online.bean.ActivityGoods;
import cn.com.zhihetech.online.core.common.Constant;
import cn.com.zhihetech.online.core.common.Pager;
import cn.com.zhihetech.online.core.http.PageDataCallback;
import cn.com.zhihetech.online.core.http.ResponseMessageCallback;

/**
 * Created by ShenYunjie on 2016/3/7.
 */
public class ActivityGoodsModel extends BaseModel<ActivityGoods> {

    /**
     * 分页获取指定商家的指定活动商品（秒杀商品）
     *
     * @param callback
     * @param pager
     * @param merchantId 商家ID
     * @param activityId 活动ID
     * @return
     */
    public Callback.Cancelable getActivityGoodsByMerchantId(PageDataCallback<ActivityGoods> callback,
                                                            @NonNull Pager pager, @NonNull String merchantId, @NonNull String activityId) {
        ModelParams params = new ModelParams().addParam("acitivitId", activityId).addParam("merchantId", merchantId)
                .addPager(pager);
        return getPageData(Constant.MERCHANT_SECKILL_GOODSES_URL, params, callback);
    }


    public Callback.Cancelable getActivityGoodsById(ResponseMessageCallback<ActivityGoods> callback, @NonNull String acitivityGoodsId) {
        String url = MessageFormat.format(Constant.ACTIVITY_GOODS_URL, acitivityGoodsId);
        return getResponseMessage(url, null, callback);
    }
}
