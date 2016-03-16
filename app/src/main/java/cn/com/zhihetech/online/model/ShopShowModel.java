package cn.com.zhihetech.online.model;

import android.support.annotation.NonNull;

import org.xutils.common.Callback;

import java.text.MessageFormat;

import cn.com.zhihetech.online.bean.ShopShow;
import cn.com.zhihetech.online.core.common.Constant;
import cn.com.zhihetech.online.core.http.ArrayCallback;

/**
 * Created by ShenYunjie on 2016/3/16.
 */
public class ShopShowModel extends BaseModel<ShopShow> {

    /**
     * 获取指定商家的门店照（最多只能获取20条数据）
     *
     * @param callback
     * @param merchantId
     * @return
     */
    public Callback.Cancelable getMerchantShopShows(ArrayCallback<ShopShow> callback, @NonNull String merchantId) {
        String url = MessageFormat.format(Constant.MERCHANT_SHOP_SHOWS_URL, merchantId);
        return getArray(url, null, callback);
    }
}
