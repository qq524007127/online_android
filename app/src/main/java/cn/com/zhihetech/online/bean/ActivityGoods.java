package cn.com.zhihetech.online.bean;

/**
 * 参加活动的商品
 * Created by ShenYunjie on 2015/12/7.
 */
public class ActivityGoods extends BaseBean {
    private String agId;
    private float activityPrice;    //活动价格
    private Activity activity;  //对应的活动
    private Goods goods;    //对应的商品
    private Merchant merchant;  //对应的商家
    private int count;  //数量

    public String getAgId() {
        return agId;
    }

    public void setAgId(String agId) {
        this.agId = agId;
    }

    public float getActivityPrice() {
        return activityPrice;
    }

    public void setActivityPrice(float activityPrice) {
        this.activityPrice = activityPrice;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
