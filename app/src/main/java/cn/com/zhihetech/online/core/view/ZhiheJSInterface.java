package cn.com.zhihetech.online.core.view;

import android.content.Context;
import android.content.Intent;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import java.text.MessageFormat;

import cn.com.zhihetech.online.core.ZhiheApplication;
import cn.com.zhihetech.online.core.util.SharedPreferenceUtils;
import cn.com.zhihetech.online.ui.activity.CouponItemInfoActivity;
import cn.com.zhihetech.online.ui.activity.GoodsInfoActivity;
import cn.com.zhihetech.online.ui.activity.MerchantHomeActivity;
import cn.com.zhihetech.online.ui.activity.SeckillGoodsInfoActivity;

/**
 * Android与WebView中js交互的接口
 * Created by ShenYunjie on 2016/3/17.
 */
public class ZhiheJSInterface implements ZhiheWebView.JsInterface {

    private Context mContext;
    private WebView target;

    public ZhiheJSInterface(Context mContext, WebView target) {
        this.mContext = mContext;
        this.target = target;
    }

    /**
     * 跳转到指定的秒杀商品
     *
     * @param goodsId
     * @param goodsName
     */
    @Override
    public void navigationActivityGoods(final String goodsId, final String goodsName) {
        target.post(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(mContext, SeckillGoodsInfoActivity.class);
                intent.putExtra(SeckillGoodsInfoActivity.SECKILL_GOODS_ID_KEY, goodsId);
                intent.putExtra(SeckillGoodsInfoActivity.CUSTOM_TITLE_KEY, goodsName);
                mContext.startActivity(intent);
            }
        });
    }

    /**
     * 跳转到指定的普通商品
     *
     * @param goodsId
     * @param goodsName
     */
    @Override
    public void navigationGoods(final String goodsId, final String goodsName) {
        target.post(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(mContext, GoodsInfoActivity.class);
                intent.putExtra(GoodsInfoActivity.GOODS_ID_KEY, goodsId);
                intent.putExtra(GoodsInfoActivity.CUSTOM_TITLE_KEY, goodsName);
                mContext.startActivity(intent);
            }
        });
    }

    /**
     * 跳转到指定的商家
     *
     * @param merchantId
     * @param merchantName
     */
    @Override
    public void navigationMerchant(final String merchantId, final String merchantName) {
        target.post(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(mContext, MerchantHomeActivity.class);
                intent.putExtra(MerchantHomeActivity.MERCHANT_ID_KEY, merchantId);
                intent.putExtra(MerchantHomeActivity.CUSTOM_TITLE_KEY, merchantName);
                mContext.startActivity(intent);
            }
        });
    }

    /**
     * 导航的优惠券详情页
     *
     * @param couponItemId
     */
    @Override
    public void navigationCouponItemInfo(final String couponItemId) {
        target.post(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(mContext, CouponItemInfoActivity.class);
                intent.putExtra(CouponItemInfoActivity.COUPON_ITEM_ID_KEY, couponItemId);
                mContext.startActivity(intent);
            }
        });
    }

    /**
     * 获取当前登录用户的token
     */
    @Override
    public void getUserToken() {
        SharedPreferenceUtils sharedPreferenceUtils = SharedPreferenceUtils.getInstance(mContext);
        final String token = sharedPreferenceUtils.getUserToken();
        final String onBackUserTokenTpl = "javascript:onBackUserToken(\"{0}\")";
        target.post(new Runnable() {
            @Override
            public void run() {
                target.loadUrl(MessageFormat.format(onBackUserTokenTpl, token));
            }
        });
    }

    /**
     * 获取当前登录用户的ID
     */
    @Override
    public void getUserId() {
        final String userId = ZhiheApplication.getInstance().getUserId();
        final String onBackUserIdTpl = "javascript:onBackUserId(\"{0}\")";
        target.post(new Runnable() {
            @Override
            public void run() {
                target.loadUrl(MessageFormat.format(onBackUserIdTpl, userId));
            }
        });
    }

    @Override
    public void initJsRuntime() {
        getUserId();
        getUserToken();
    }
}
