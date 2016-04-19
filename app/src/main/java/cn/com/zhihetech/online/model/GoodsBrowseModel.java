package cn.com.zhihetech.online.model;

import android.support.annotation.NonNull;

import org.xutils.common.Callback;

import cn.com.zhihetech.online.bean.GoodsBrowse;
import cn.com.zhihetech.online.core.common.Constant;
import cn.com.zhihetech.online.core.common.ResponseMessage;
import cn.com.zhihetech.online.core.http.ObjectCallback;

/**
 * Created by ShenYunjie on 2016/4/19.
 */
public class GoodsBrowseModel extends BaseModel<GoodsBrowse> {

    /**
     * 添加商品浏览记录
     *
     * @param callback 回调
     * @param userId   用户ID,不能为空
     * @param goodsId  商品ID,不能为空
     * @return
     */
    public Callback.Cancelable addGoodsBrowse(ObjectCallback<ResponseMessage> callback, @NonNull String userId, @NonNull String goodsId) {
        ModelParams params = new ModelParams().addParam("user.userId", userId).addParam("goods.goodsId", goodsId);
        return new SimpleModel(ResponseMessage.class).postObject(Constant.ADD_GOODS_BROWSE_URL, params, callback);
    }
}
