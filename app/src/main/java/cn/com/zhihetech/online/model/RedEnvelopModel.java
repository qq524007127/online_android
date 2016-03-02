package cn.com.zhihetech.online.model;

import android.support.annotation.NonNull;

import org.xutils.common.Callback;

import java.text.MessageFormat;

import cn.com.zhihetech.online.bean.EMUserInfo;
import cn.com.zhihetech.online.bean.RedEnvelop;
import cn.com.zhihetech.online.core.common.Constant;
import cn.com.zhihetech.online.core.common.Pager;
import cn.com.zhihetech.online.core.common.ResponseMessage;
import cn.com.zhihetech.online.core.http.ObjectCallback;
import cn.com.zhihetech.online.core.http.PageDataCallback;

/**
 * Created by ShenYunjie on 2016/3/2.
 */
public class RedEnvelopModel extends BaseModel<RedEnvelop> {

    /**
     * 根据商家ID和活动ID分页查询红包
     *
     * @param callback
     * @param pager
     * @param merchantId
     * @param activityId
     * @return
     */
    public Callback.Cancelable getRedEnvelopsByMerchantIdAndActivityId(PageDataCallback<RedEnvelop> callback, Pager pager, @NonNull String merchantId, @NonNull String activityId) {
        ModelParams params = new ModelParams().addPager(pager).addParam("merchantId", merchantId).addParam("activityId", activityId);
        return getPageData(Constant.MERCHANT_ACTIVITY_RED_ENVELOPS_URL, params, callback);
    }

    public Callback.Cancelable updateSendState(ObjectCallback<ResponseMessage> callback, @NonNull String envelopId) {
        String url = MessageFormat.format(Constant.UPDATE_RED_ENVELOP_STATU_URL, envelopId);
        return new SimpleModel(ResponseMessage.class).postObject(url, null, callback);
    }
}
