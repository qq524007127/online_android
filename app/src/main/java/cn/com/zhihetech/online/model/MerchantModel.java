package cn.com.zhihetech.online.model;

import android.support.annotation.NonNull;

import java.text.MessageFormat;

import cn.com.zhihetech.online.bean.Merchant;
import cn.com.zhihetech.online.core.common.Constant;
import cn.com.zhihetech.online.core.common.Pager;
import cn.com.zhihetech.online.core.common.ResponseMessage;
import cn.com.zhihetech.online.core.http.ObjectCallback;
import cn.com.zhihetech.online.core.http.PageDataCallback;
import cn.com.zhihetech.online.core.http.ResponseMessageCallback;

/**
 * Created by ShenYunjie on 2016/1/18.
 */
public class MerchantModel extends BaseModel<Merchant> {

    private final String MERCHANT_ID_KEY = "merchantId";

    /**
     * 每日上新
     *
     * @param callback
     * @param pager
     */
    public void getDailyNewList(PageDataCallback<Merchant> callback, Pager pager) {
        ModelParams params = new ModelParams().addPager(pager);
        getPageData(Constant.DAILY_NEW_URL, params, callback);
    }

    /**
     * 根据商家ID获取商家基本信息
     *
     * @param callback
     * @param merchantId    商家ID
     */
    public void getMerchantById(ObjectCallback<Merchant> callback, @NonNull String merchantId) {
        String url = MessageFormat.format(Constant.MERCHANT_URL, merchantId);
        getObject(url, null, callback);
    }

    /**
     * 根据用户ID和商家ID检查是否用户是否已关注商家
     *
     * @param callback
     * @param userId    用户id
     * @param merchantId    商家ID
     */
    public void checkFucosState(ObjectCallback<ResponseMessage> callback, @NonNull String userId, @NonNull String merchantId) {
        ModelParams params = new ModelParams().addParam(MERCHANT_ID_KEY, merchantId).addParam("userId", userId);
        new FlexibleModel(ResponseMessage.class).getObject(Constant.CHECK_MERCHANT_FUCOS_STATE_URL, params, callback);
    }

    /**
     * 用户关注商家
     *
     * @param callback
     * @param userId    用户ID
     * @param merchantId    商家ID
     */
    public void focusMerchant(ObjectCallback<ResponseMessage> callback, @NonNull String userId, @NonNull String merchantId) {
        ModelParams params = new ModelParams().addParam(MERCHANT_ID_KEY, merchantId).addParam("userId", userId);
        new FlexibleModel(ResponseMessage.class).postObject(Constant.FOCUS_MERCHANT_URL, params, callback);
    }
}
