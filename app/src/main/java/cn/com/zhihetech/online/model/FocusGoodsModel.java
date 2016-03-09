package cn.com.zhihetech.online.model;

import android.support.annotation.NonNull;

import org.xutils.common.Callback;

import java.text.MessageFormat;

import cn.com.zhihetech.online.bean.FocusGoods;
import cn.com.zhihetech.online.bean.Goods;
import cn.com.zhihetech.online.core.common.Constant;
import cn.com.zhihetech.online.core.common.Pager;
import cn.com.zhihetech.online.core.common.ResponseMessage;
import cn.com.zhihetech.online.core.http.ObjectCallback;
import cn.com.zhihetech.online.core.http.PageDataCallback;

/**
 * Created by ShenYunjie on 2016/1/20.
 */
public class FocusGoodsModel extends BaseModel<FocusGoods> {
    /**
     * 查询指定用户是否已收藏了指定的商品
     *
     * @param callback
     * @param goodsId
     * @param userId
     * @return
     */
    public Callback.Cancelable checkFocusSate(ObjectCallback<ResponseMessage> callback, @NonNull String goodsId, @NonNull String userId) {
        ModelParams params = new ModelParams().addParam("goodsId", goodsId).addParam("userId", userId);
        return new SimpleModel<ResponseMessage>(ResponseMessage.class).getObject(Constant.CHECK_FOCUS_GOODS_URL, params, callback);
    }

    /**
     * 收藏商品
     *
     * @param callback
     * @param goodsId
     * @param userId
     * @return
     */
    public Callback.Cancelable focusGoods(ObjectCallback<ResponseMessage> callback, @NonNull String goodsId, @NonNull String userId) {
        ModelParams params = new ModelParams().addParam("goodsId", goodsId).addParam("userId", userId);
        return new SimpleModel<ResponseMessage>(ResponseMessage.class).getObject(Constant.FOCUS_GOODS_URL, params, callback);
    }

    /**
     * 取消收藏商品
     *
     * @param callback
     * @param goodsId
     * @param userId
     * @return
     */
    public Callback.Cancelable unFocusGoods(ObjectCallback<ResponseMessage> callback, @NonNull String goodsId, @NonNull String userId) {
        ModelParams params = new ModelParams().addParam("goodsId", goodsId).addParam("userId", userId);
        return new SimpleModel<ResponseMessage>(ResponseMessage.class).getObject(Constant.UN_FOCUS_GOODS_URL, params, callback);
    }

    /**
     * 获取指定用户收藏的商品
     *
     * @param callback
     * @param pager
     * @param userId
     * @return
     */
    public Callback.Cancelable getFavoriteGoodsesByUserId(PageDataCallback<FocusGoods> callback, Pager pager, @NonNull String userId) {
        String url = MessageFormat.format(Constant.USER_FAVORITES_GOODSES_URL, userId);
        return getPageData(url, new ModelParams().addPager(pager), callback);
    }

}
