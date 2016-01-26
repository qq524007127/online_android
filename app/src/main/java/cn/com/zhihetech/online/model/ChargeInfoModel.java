package cn.com.zhihetech.online.model;

import android.support.annotation.Nullable;

import org.xutils.common.Callback;

import cn.com.zhihetech.online.bean.ChargeInfo;
import cn.com.zhihetech.online.bean.Order;
import cn.com.zhihetech.online.core.common.Constant;
import cn.com.zhihetech.online.core.http.ResponseMessageCallback;

/**
 * Created by ShenYunjie on 2016/1/26.
 */
public class ChargeInfoModel extends BaseModel<ChargeInfo> {
    /**
     * 根据订单获取支付charge
     *
     * @param callback
     * @param order
     * @param channel
     * @return
     */
    public Callback.Cancelable getChargeInfoByOrder(ResponseMessageCallback<String> callback, @Nullable Order order, ChargeInfo.PayChannel channel) {
        ModelParams params = createParamsWithOrder(order);
        return new SimpleModel<String>(String.class).postResponseMessage(Constant.CHARGE_URL, params, callback);
    }

    /**
     * 根据订单获取支付charge
     *
     * @param order
     * @return
     */
    private ModelParams createParamsWithOrder(Order order) {
        ModelParams params = new ModelParams().addParam("amount", String.valueOf((int) (order.getOrderTotal() * 100)))
                .addParam("orderNo", order.getOrderCode()).addParam("subject", order.getOrderName())
                .addParam("body", order.getOrderName());
        return params;
    }
}
