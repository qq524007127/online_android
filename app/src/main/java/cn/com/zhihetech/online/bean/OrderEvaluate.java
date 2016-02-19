package cn.com.zhihetech.online.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ShenYunjie on 2016/2/19.
 */
public class OrderEvaluate extends BaseBean {
    private String orderId;
    private float score;
    private List<GoodsEvaluate> goodsScores = new ArrayList<>();

    public OrderEvaluate() {
    }

    public OrderEvaluate(String orderId, float score) {
        this.orderId = orderId;
        this.score = score;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public List<GoodsEvaluate> getGoodsScores() {
        return goodsScores;
    }

    public void setGoodsScores(List<GoodsEvaluate> goodsScores) {
        this.goodsScores = goodsScores;
    }

    public void addGoodsEvaluate(GoodsEvaluate goodsEvaluate) {
        this.goodsScores.add(goodsEvaluate);
    }
}
