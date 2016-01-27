package cn.com.zhihetech.online.model;

import android.support.annotation.NonNull;

import com.alibaba.fastjson.JSONObject;

import org.xutils.common.Callback;

import java.util.List;

import cn.com.zhihetech.online.bean.Order;
import cn.com.zhihetech.online.core.common.Constant;
import cn.com.zhihetech.online.core.common.Pager;
import cn.com.zhihetech.online.core.http.PageDataCallback;
import cn.com.zhihetech.online.core.http.ResponseMessageCallback;

/**
 * Created by ShenYunjie on 2016/1/26.
 */
public class OrderModel extends BaseModel<Order> {
    /**
     * 提交订单
     *
     * @param callback 回调接口
     * @param orders   需要提交的订单列表
     * @return
     */
    public Callback.Cancelable getChargeWithOrders(ResponseMessageCallback<String> callback, @NonNull List<Order> orders) {
        ModelParams params = new ModelParams().addParam("orderStr", JSONObject.toJSONString(orders));
        return new SimpleModel(String.class).postResponseMessage(Constant.ORDER_ADD_URL, params, callback);
    }

    /**
     * 分页获取用户的订单
     *
     * @param callback
     * @param pager
     * @param userId
     * @param orderState
     * @return
     */
    public Callback.Cancelable getOrdersByUserId(PageDataCallback<Order> callback, @NonNull Pager pager, @NonNull String userId, @NonNull Integer orderState) {
        if (orderState == null) {
            orderState = 0;
        }
        ModelParams params = new ModelParams().addParam("userId", userId)
                .addParam("orderState", String.valueOf(orderState)).addPager(pager);
        return getPageData(Constant.USER_ORDERS_URL, params, callback);
    }

    /**
     * 根据订单创建订单请求参数
     *
     * @param order
     * @return
     */
    private ModelParams createOrderParams(Order order) {
        ModelParams params = new ModelParams();
        params.addParam("orderName", order.getOrderName()).addParam("user.userId", order.getUser().getUserId())
                .addParam("orderTotal", String.valueOf(order.getOrderTotal())).addParam("userMsg", order.getUserMsg())
                .addParam("receiverName", order.getReceiverName()).addParam("receiverPhone", order.getReceiverPhone())
                .addParam("receiverAdd", order.getReceiverAdd()).addParam("orderDetailInfo", order.getOrderDetailInfo());
        return params;
    }
}