package cn.com.zhihetech.online.model;

import android.support.annotation.NonNull;

import org.xutils.common.Callback;

import java.text.MessageFormat;

import cn.com.zhihetech.online.bean.OrderDetail;
import cn.com.zhihetech.online.core.common.Constant;
import cn.com.zhihetech.online.core.http.ArrayCallback;

/**
 * Created by ShenYunjie on 2016/2/18.
 */
public class OrderDetailModel extends BaseModel<OrderDetail> {
    /**
     * 根据订单ID获取订单详情
     *
     * @param callback
     * @param orderId
     * @return
     */
    public Callback.Cancelable getOrderDetailByOrderId(ArrayCallback<OrderDetail> callback, @NonNull String orderId) {
        String url = MessageFormat.format(Constant.ORDER_DETAIL_URL, orderId);
        return getArray(url, null, callback);
    }
}
