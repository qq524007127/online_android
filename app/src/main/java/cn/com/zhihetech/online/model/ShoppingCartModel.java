package cn.com.zhihetech.online.model;

import android.support.annotation.NonNull;

import org.xutils.common.Callback;

import java.text.MessageFormat;

import cn.com.zhihetech.online.bean.ShoppingCart;
import cn.com.zhihetech.online.core.common.Constant;
import cn.com.zhihetech.online.core.common.Pager;
import cn.com.zhihetech.online.core.common.ResponseMessage;
import cn.com.zhihetech.online.core.http.ObjectCallback;
import cn.com.zhihetech.online.core.http.PageDataCallback;

/**
 * Created by ShenYunjie on 2016/1/21.
 */
public class ShoppingCartModel extends BaseModel<ShoppingCart> {
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

    public Callback.Cancelable getShoppingCartsByUserId(PageDataCallback<ShoppingCart> callback, Pager pager, @NonNull String userId) {
        ModelParams params = new ModelParams().addPager(pager);
        String url = MessageFormat.format(Constant.USER_SHOPPING_CARTS_URL, userId);
        return getPageData(url, params, callback);
    }
}
