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
    private final String AMOUNT_KEY = "amount";
    private final String SHOPPING_CARTS_IDS_KEY = "shoppingCartsIds";
    private final String SHOPPING_CART_ID_KEY = "shoppingCartId";

    /**
     * 添加商品到购物车
     *
     * @param callback
     * @param goodsId
     * @param userId
     * @param amount
     * @return
     */
    public Callback.Cancelable addShoppingCart(ObjectCallback<ResponseMessage> callback, @NonNull String goodsId, @NonNull String userId, @NonNull Integer amount) {
        if (amount == null || amount <= 0) {
            amount = 1;
        }
        ModelParams params = new ModelParams().addParam(USER_ID_KEY, userId)
                .addParam(GOODS_ID_KEY, goodsId).addParam(AMOUNT_KEY, String.valueOf(amount));
        return new SimpleModel(ResponseMessage.class).postObject(Constant.ADD_SHOPPING_CART_URL, params, callback);
    }

    /**
     * 分页获取指定用的购物车数据
     *
     * @param callback
     * @param pager
     * @param userId
     * @return
     */
    public Callback.Cancelable getShoppingCartsByUserId(PageDataCallback<ShoppingCart> callback, Pager pager, @NonNull String userId) {
        ModelParams params = new ModelParams().addPager(pager);
        String url = MessageFormat.format(Constant.USER_SHOPPING_CARTS_URL, userId);
        return getPageData(url, params, callback);
    }

    /**
     * 批量删除购物车数据
     *
     * @param callback
     * @param ids      购物车id（xx*xx*xx)
     * @return
     */
    public Callback.Cancelable batchDeleteShoppingCarts(ObjectCallback<ResponseMessage> callback, @NonNull String ids) {
        ModelParams params = new ModelParams().addParam(SHOPPING_CARTS_IDS_KEY, ids);
        return new SimpleModel<ResponseMessage>(ResponseMessage.class).postObject(Constant.DELETE_SHOPPING_CARTS_URL, params, callback);
    }

    /**
     * 更新指定购物车的商品数量
     *
     * @param callback
     * @param amount
     * @return
     */
    public Callback.Cancelable updateAmount(ObjectCallback<ResponseMessage> callback, @NonNull String shoppingCartId, @NonNull Integer amount) {
        ModelParams params = new ModelParams().addParam(SHOPPING_CART_ID_KEY, shoppingCartId).addParam(AMOUNT_KEY, String.valueOf(amount));
        return new SimpleModel<ResponseMessage>(ResponseMessage.class).postObject(Constant.UPDATE_SHOPPING_CART_AMOUNT_URL, params, callback);
    }
}
