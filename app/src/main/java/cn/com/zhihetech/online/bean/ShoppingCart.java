package cn.com.zhihetech.online.bean;

import java.util.Date;

/**
 * Created by YangDaiChun on 2015/11/12.
 */

public class ShoppingCart extends BaseBean {

    private String shoppingCartId;
    private User user;
    private Goods goods;
    private int amount;
    private Date focusDate;

    public String getShoppingCartId() {
        return shoppingCartId;
    }

    public void setShoppingCartId(String shoppingCartId) {
        this.shoppingCartId = shoppingCartId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public Date getFocusDate() {
        return focusDate;
    }

    public void setFocusDate(Date focusDate) {
        this.focusDate = focusDate;
    }
}
