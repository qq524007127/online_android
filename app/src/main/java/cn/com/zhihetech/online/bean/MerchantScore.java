package cn.com.zhihetech.online.bean;


/**
 * Created by YangDaiChun on 2016/2/18.
 */
public class MerchantScore extends BaseBean {
    private String merScoreId;
    private Merchant merchant;
    private float score = 0;

    public String getMerScoreId() {
        return merScoreId;
    }

    public void setMerScoreId(String merScoreId) {
        this.merScoreId = merScoreId;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }
}
