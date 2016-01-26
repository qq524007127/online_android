package cn.com.zhihetech.online.bean;

/**
 * Created by YangDaiChun on 2015/11/12.
 */

public class OrderDetail extends BaseBean {

    private String orderDetailId;
    private Order order;
    private Goods goods;
    private int count;

    public OrderDetail(Goods goods, int amount) {
        this.goods = goods;
        this.count = amount;
    }

    public String getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(String orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
