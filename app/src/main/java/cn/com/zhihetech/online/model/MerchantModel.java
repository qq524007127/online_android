package cn.com.zhihetech.online.model;

import android.support.annotation.NonNull;

import org.xutils.common.Callback;

import java.text.MessageFormat;

import cn.com.zhihetech.online.bean.Merchant;
import cn.com.zhihetech.online.bean.MerchantToken;
import cn.com.zhihetech.online.core.common.Constant;
import cn.com.zhihetech.online.core.common.MerchantLoginCallback;
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
    public Callback.Cancelable getMerchantsByTypeAndTypeId(PageDataCallback<Merchant> callback, Pager pager, int type, String typeId) {
        ModelParams params = new ModelParams().addPager(pager).addParam("type", String.valueOf(type));
        if (type != 3) {
            params.addParam("typeId", typeId);
        }
        return getPageData(Constant.FEATURED_BLOCK_OR_SHOPPING_CENTER_MERCHANTS_URL, params, callback);
    }

    /**
     * 根据查询条件获取所用的商家
     *
     * @param callback
     * @param params
     */
    public Callback.Cancelable getMerchantsByModelParams(PageDataCallback<Merchant> callback, ModelParams params) {
        return getPageData(Constant.MERCHANT_LIST_URL, params, callback);
    }

    /**
     * 根据商家ID获取商家基本信息
     *
     * @param callback
     * @param merchantId 商家ID
     */
    public Callback.Cancelable getMerchantById(ResponseMessageCallback<Merchant> callback, @NonNull String merchantId) {
        String url = MessageFormat.format(Constant.MERCHANT_INFO_URL, merchantId);
        return getResponseMessage(url, null, callback);
    }

    /**
     * 根据用户ID和商家ID检查是否用户是否已关注商家
     *
     * @param callback
     * @param userId     用户id
     * @param merchantId 商家ID
     */
    public Callback.Cancelable checkFucosState(ObjectCallback<ResponseMessage> callback, @NonNull String userId, @NonNull String merchantId) {
        ModelParams params = new ModelParams().addParam(MERCHANT_ID_KEY, merchantId).addParam("userId", userId);
        return new SimpleModel(ResponseMessage.class).getObject(Constant.CHECK_MERCHANT_FUCOS_STATE_URL, params, callback);
    }

    /**
     * 用户关注商家
     *
     * @param callback
     * @param userId     用户ID
     * @param merchantId 商家ID
     */
    public Callback.Cancelable focusMerchant(ObjectCallback<ResponseMessage> callback, @NonNull String userId, @NonNull String merchantId) {
        ModelParams params = new ModelParams().addParam(MERCHANT_ID_KEY, merchantId).addParam("userId", userId);
        return new SimpleModel(ResponseMessage.class).postObject(Constant.FOCUS_MERCHANT_URL, params, callback);
    }

    /**
     * 取消关注商家
     *
     * @param callback   回调
     * @param userId     用户 ID
     * @param merchantId 商家 ID
     * @return
     */
    public Callback.Cancelable cancelFocusMerchant(ObjectCallback<ResponseMessage> callback, @NonNull String userId, @NonNull String merchantId) {
        ModelParams params = new ModelParams().addParam(MERCHANT_ID_KEY, merchantId).addParam("userId", userId);
        return  new SimpleModel(ResponseMessage.class).postObject(Constant.CANCEL_FOCUS_MERCHANT_URL,params,callback);
    }

    /**
     * 根据商品类别获取商家
     *
     * @param callback
     * @param categoryId 类别ID
     */
    public Callback.Cancelable getMerchantsByCategory(PageDataCallback<Merchant> callback, Pager pager, @NonNull String categoryId) {
        ModelParams params = new ModelParams().addPager(pager);
        String url = MessageFormat.format(Constant.CATEGORY_MERCHANTS_URL, categoryId);
        return getPageData(url, params, callback);
    }

    /**
     * 根据用户ID获取收藏的商家（获取好友列表)
     *
     * @param callback
     * @param pager
     * @param userId
     * @return
     */
    public Callback.Cancelable getFocusMerchantsByUserId(PageDataCallback<Merchant> callback, Pager pager, @NonNull String userId) {
        String url = MessageFormat.format(Constant.USER_FOCUS_MERCHANTS_URL, userId);
        ModelParams params = new ModelParams().addPager(pager);
        return getPageData(url, params, callback);
    }

    /**
     * 商家版账号登录
     *
     * @param callback
     * @param adminCode //商家登录账号
     * @param adminPwd  //商家登录密码
     * @return
     */
    public Callback.Cancelable login(MerchantLoginCallback callback, @NonNull String adminCode, @NonNull String adminPwd) {
        ModelParams params = new ModelParams().addParam("adminCode", adminCode).addParam("adminPwd", adminPwd);
        return new SimpleModel(MerchantToken.class).postResponseMessage(Constant.MERCHANT_LOGIN_URL, params, callback);
    }

    /**
     * 更改当前商家登录密码
     *
     * @param changeCallback
     * @param adminCode
     * @param oldPwd
     * @param newPwd
     */
    public Callback.Cancelable changePwd(ObjectCallback<ResponseMessage> changeCallback, @NonNull String adminCode, @NonNull String oldPwd, @NonNull String newPwd) {
        ModelParams params = new ModelParams().addParam("adminCode", adminCode).addParam("oldPwd", oldPwd).addParam("newPwd", newPwd);
        return new SimpleModel(ResponseMessage.class).postObject(Constant.ADMIN_CHANGE_PASSWORD_URL, params, changeCallback);
    }
}
