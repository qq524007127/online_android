package cn.com.zhihetech.online.model;

import android.support.annotation.NonNull;

import org.xutils.common.Callback;

import cn.com.zhihetech.online.bean.MerchantBrowse;
import cn.com.zhihetech.online.core.common.Constant;
import cn.com.zhihetech.online.core.common.ResponseMessage;
import cn.com.zhihetech.online.core.http.ObjectCallback;

/**
 * Created by ShenYunjie on 2016/4/19.
 */
public class MerchantBrowseModel extends BaseModel<MerchantBrowse> {

    /**
     * 添加商家浏览记录
     *
     * @param callback   回调
     * @param userId     用户ID,不能为空
     * @param merchantId 商家ID,不能为空
     * @return
     */
    public Callback.Cancelable addMerchantBorwse(ObjectCallback<ResponseMessage> callback, @NonNull String userId, @NonNull String merchantId) {
        ModelParams param = new ModelParams().addParam("user.userId", userId).addParam("merchant.merchantId", merchantId);
        return new SimpleModel(ResponseMessage.class).postObject(Constant.ADD_MERCHANT_BROWSE_URL, param, callback);
    }
}
