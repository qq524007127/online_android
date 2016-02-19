package cn.com.zhihetech.online.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by ShenYunjie on 2016/2/19.
 */
public class GoodsEvaluate extends BaseBean {
    private String goodsId;
    private float score;
    private String evaluate;

    public GoodsEvaluate() {
    }

    public GoodsEvaluate(String goodsId, float score, String evaluate) {
        this.goodsId = goodsId;
        this.score = score;
        this.evaluate = evaluate;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
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
}
