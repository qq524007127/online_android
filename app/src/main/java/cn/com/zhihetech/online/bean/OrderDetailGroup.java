package cn.com.zhihetech.online.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ShenYunjie on 2016/1/27.
 */
public class OrderDetailGroup extends BaseBean {

    private Merchant merchant;
    private List<OrderDetail> orderDetails;
    private String userMsg; //用户留言

    public OrderDetailGroup() {
        this.orderDetails = new ArrayList<>();
    }

    public OrderDetailGroup(OrderDetail orderDetail) {
        this();
        this.merchant = orderDetail.getGoods().getMerchant();
        this.orderDetails.add(orderDetail);
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public void addOrderDetail(OrderDetail detail) {
        this.orderDetails.add(detail);
    }

    public String getUserMsg() {
        return userMsg;
    }

    public void setUserMsg(String userMsg) {
        this.userMsg = userMsg;
    }
}
