package cn.com.zhihetech.online.bean;

/**
 * Created by YangDaiChun on 2015/11/12.
 */

public class OrderDetail extends BaseBean {

    private String orderDetailId;
    private Order order;
    private Goods goods;
    private float price;    //单价
    private int count;  //数量
    private float totalPrice;   //总价（price * count)

    public OrderDetail(Goods goods, int amount) {
        this.goods = goods;
        this.count = amount;
        this.price = (float) goods.getPrice();
        this.totalPrice = this.price * this.count;
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

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }
}
