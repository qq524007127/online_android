package cn.com.zhihetech.online.model;

import android.support.annotation.NonNull;

import org.xutils.common.Callback;

import cn.com.zhihetech.online.core.common.Constant;
import cn.com.zhihetech.online.core.common.ResponseMessage;
import cn.com.zhihetech.online.core.http.ObjectCallback;

/**
 * Created by ShenYunjie on 2016/1/21.
 */
public class ShoppingCartModel extends BaseModel<ShoppingCartModel> {
    private final String USER_ID_KEY = "user.userId";
    private final String GOODS_ID_KEY = "goods.goodsId";
    private final String COUNT_KEY = "count";

    public Callback.Cancelable addShoppingCart(ObjectCallback<ResponseMessage> callback, @NonNull String goodsId, @NonNull String userId, @NonNull Integer count) {
        if (count == null || count <= 0) {
            count = 1;
        }
        ModelParams params = new ModelParams().addParam(USER_ID_KEY, userId)
                .addParam(GOODS_ID_KEY, goodsId).addParam(COUNT_KEY, String.valueOf(count));
        return new SimpleModel(ResponseMessage.class).postObject(Constant.ADD_SHOPPING_CART_URL, params, callback);
    }
}
