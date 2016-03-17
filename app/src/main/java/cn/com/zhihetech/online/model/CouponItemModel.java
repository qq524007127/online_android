package cn.com.zhihetech.online.model;

import android.support.annotation.NonNull;

import org.xutils.common.Callback;

import java.text.MessageFormat;

import cn.com.zhihetech.online.bean.CouponItem;
import cn.com.zhihetech.online.core.common.Constant;
import cn.com.zhihetech.online.core.common.Pager;
import cn.com.zhihetech.online.core.http.PageDataCallback;
import cn.com.zhihetech.online.core.http.ResponseMessageCallback;

/**
 * Created by ShenYunjie on 2016/3/17.
 */
public class CouponItemModel extends BaseModel<CouponItem> {

    /**
     * 获取指定用户的优惠券
     *
     * @param callback
     * @param userId
     * @return
     */
    public Callback.Cancelable getCouponItemsByUserId(PageDataCallback<CouponItem> callback, Pager pager, @NonNull String userId) {
        String url = MessageFormat.format(Constant.USER_COUPON_ITEMS_URL, userId);
        return getPageData(url, new ModelParams().addPager(pager), callback);
    }

    /**
     * 根据优惠券Id获取优惠券详情信息
     *
     * @param callback
     * @param couponItemId
     * @return
     */
    public Callback.Cancelable getCouponItemInfoById(ResponseMessageCallback<CouponItem> callback, @NonNull String couponItemId) {
        String url = MessageFormat.format(Constant.COUPON_ITEM_DETAILS_URL, couponItemId);
        return getResponseMessage(url, null, callback);
    }
}
