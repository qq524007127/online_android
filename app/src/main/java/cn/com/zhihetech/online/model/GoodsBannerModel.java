package cn.com.zhihetech.online.model;

import android.support.annotation.NonNull;

import org.xutils.common.Callback;

import java.text.MessageFormat;

import cn.com.zhihetech.online.bean.GoodsBanner;
import cn.com.zhihetech.online.core.common.Constant;
import cn.com.zhihetech.online.core.http.ArrayCallback;

/**
 * Created by ShenYunjie on 2016/1/20.
 */
public class GoodsBannerModel extends BaseModel<GoodsBanner> {
    public Callback.Cancelable getGoodsBannersByGoodsId(ArrayCallback<GoodsBanner> callback, @NonNull String goodsId) {
        String url = MessageFormat.format(Constant.GOODS_BANNERS_URL, goodsId);
        return getArray(url, null, callback);
    }
}
