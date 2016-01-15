package cn.com.zhihetech.online.bean;

import java.util.Date;

/**
 * 商家推荐商品
 * Created by YangDaiChun on 2015/11/12.
 */

public class Recommend extends BaseBean {

    private String recommendId;
    private Goods goods;
    private Merchant merchant;
    private String reason;  //推荐理由
    private Date createDate;
    private int orderIndex; //排序

    public String getRecommendId() {
        return recommendId;
    }

    public void setRecommendId(String recommendId) {
        this.recommendId = recommendId;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }
}
