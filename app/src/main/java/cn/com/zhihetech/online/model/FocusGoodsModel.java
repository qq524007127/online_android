package cn.com.zhihetech.online.model;

import android.support.annotation.NonNull;

import org.xutils.common.Callback;

import cn.com.zhihetech.online.bean.FocusGoods;
import cn.com.zhihetech.online.core.common.Constant;
import cn.com.zhihetech.online.core.common.ResponseMessage;
import cn.com.zhihetech.online.core.http.ObjectCallback;

/**
 * Created by ShenYunjie on 2016/1/20.
 */
public class FocusGoodsModel extends BaseModel<FocusGoods> {
    public Callback.Cancelable checkFocusSate(ObjectCallback<ResponseMessage> callback, @NonNull String goodsId, @NonNull String userId) {
        ModelParams params = new ModelParams().addParam("goodsId", goodsId).addParam("userId", userId);
        return new SimpleModel<ResponseMessage>(ResponseMessage.class).getObject(Constant.CHECK_FOCUS_GOODS_URL, params, callback);
    }

    public Callback.Cancelable focusGoods(ObjectCallback<ResponseMessage> callback, @NonNull String goodsId, @NonNull String userId) {
        ModelParams params = new ModelParams().addParam("goodsId", goodsId).addParam("userId", userId);
        return new SimpleModel<ResponseMessage>(ResponseMessage.class).getObject(Constant.FOCUS_GOODS_URL, params, callback);
    }

    public Callback.Cancelable unFocusGoods(ObjectCallback<ResponseMessage> callback, @NonNull String goodsId, @NonNull String userId) {
        ModelParams params = new ModelParams().addParam("goodsId", goodsId).addParam("userId", userId);
        return new SimpleModel<ResponseMessage>(ResponseMessage.class).getObject(Constant.UN_FOCUS_GOODS_URL, params, callback);
    }
}
