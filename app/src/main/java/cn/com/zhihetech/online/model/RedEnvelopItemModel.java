package cn.com.zhihetech.online.model;

import android.support.annotation.NonNull;

import org.xutils.common.Callback;

import java.text.MessageFormat;

import cn.com.zhihetech.online.bean.RedEnvelopItem;
import cn.com.zhihetech.online.core.common.Constant;
import cn.com.zhihetech.online.core.common.Pager;
import cn.com.zhihetech.online.core.common.ResponseMessage;
import cn.com.zhihetech.online.core.http.ObjectCallback;
import cn.com.zhihetech.online.core.http.PageDataCallback;
import cn.com.zhihetech.online.core.http.ResponseMessageCallback;

/**
 * Created by ShenYunjie on 2016/3/4.
 */
public class RedEnvelopItemModel extends BaseModel<RedEnvelopItem> {

    /**
     * 根据用户ID红指定红包ID获取用户已领取的红包
     *
     * @param callback
     * @param envelopItemId
     * @return
     */
    public Callback.Cancelable getEnvelopItemById(ResponseMessageCallback<RedEnvelopItem> callback, @NonNull String envelopItemId) {
        String url = MessageFormat.format(Constant.RED_ENVELOP_ITEM_URL, envelopItemId);
        return getResponseMessage(url, null, callback);
    }

    /**
     * 将红包存入我的钱包
     *
     * @param callback
     * @param envelopItemId
     * @return
     */
    public Callback.Cancelable extractRedEnvelopItem(ObjectCallback<ResponseMessage> callback, @NonNull String envelopItemId, @NonNull String userId) {
        ModelParams params = new ModelParams().addParam("userId", userId).addParam("envelopItemId", envelopItemId);
        return new SimpleModel(ResponseMessage.class).postObject(Constant.EXTRACT_RED_ENVELOP_ITEM_URL, params, callback);
    }

    /**
     * 根据用户ID获取用户的红包
     *
     * @param callback
     * @param userId
     * @return
     */
    public Callback.Cancelable getEnvelopItemsByUserId(PageDataCallback<RedEnvelopItem> callback, Pager pager, @NonNull String userId) {
        String url = MessageFormat.format(Constant.USER_RED_ENVELOP_ITEMS_URL, userId);
        return getPageData(url, new ModelParams().addPager(pager), callback);
    }
}
