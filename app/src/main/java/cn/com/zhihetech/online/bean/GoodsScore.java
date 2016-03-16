package cn.com.zhihetech.online.bean;

import java.util.Date;

/**
 * Created by YangDaiChun on 2016/2/18.
 */
public class GoodsScore extends BaseBean{
    private String goodsScoreId;
    private float score;
    private String evaluate;
    private Goods goods;
    private String goodsId;
    private Date createDate;
    private User user;

    public String getGoodsScoreId() {
        return goodsScoreId;
    }

    public void setGoodsScoreId(String goodsScoreId) {
        this.goodsScoreId = goodsScoreId;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public String getEvaluate() {
        return evaluate;
    }

    public void setEvaluate(String evaluate) {
        this.evaluate = evaluate;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
