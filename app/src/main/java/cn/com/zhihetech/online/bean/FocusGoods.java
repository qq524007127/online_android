package cn.com.zhihetech.online.bean;

import java.util.Date;

/**
 * Created by YangDaiChun on 2015/11/12.
 */
public class FocusGoods extends BaseBean {

    private String focusGoodId;
    private User user;
    private Goods goods;
    private Date focusDate;

    public String getFocusGoodId() {
        return focusGoodId;
    }

    public void setFocusGoodId(String focusGoodId) {
        this.focusGoodId = focusGoodId;
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


    public Date getFocusDate() {
        return focusDate;
    }

    public void setFocusDate(Date focusDate) {
        this.focusDate = focusDate;
    }
}
