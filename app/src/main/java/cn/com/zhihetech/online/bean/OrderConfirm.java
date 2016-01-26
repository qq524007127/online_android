package cn.com.zhihetech.online.bean;

/**
 * Created by ShenYunjie on 2016/1/26.
 */
public class OrderConfirm extends BaseBean {
    private Goods goods;
    private int amount;

    public OrderConfirm(Goods goods, int amount) {
        this.goods = goods;
        this.amount = amount;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
