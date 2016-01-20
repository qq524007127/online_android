package cn.com.zhihetech.online.model;

import android.support.annotation.NonNull;

import org.xutils.common.Callback;

import java.text.MessageFormat;

import cn.com.zhihetech.online.bean.GoodsDetail;
import cn.com.zhihetech.online.core.common.Constant;
import cn.com.zhihetech.online.core.http.ArrayCallback;

/**
 * Created by ShenYunjie on 2016/1/20.
 */
public class GoodsDetailModel extends BaseModel<GoodsDetail> {
    public Callback.Cancelable getGoodsDetailByGoodsId(ArrayCallback<GoodsDetail> callback, @NonNull String goodsId) {
        String url = MessageFormat.format(Constant.GOODS_DETAILS_URL, goodsId);
        return getArray(url, null, callback);
    }
}
