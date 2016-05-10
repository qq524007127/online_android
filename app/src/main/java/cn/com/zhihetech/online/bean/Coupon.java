package cn.com.zhihetech.online.bean;

import java.util.Date;

import cn.com.zhihetech.online.core.common.Constant;

/**
 * 商家优惠券（包括优惠券和代金券）
 * Created by ShenYunjie on 2015/12/15.
 */
public class Coupon extends BaseBean {
    private String couponId;
    private String couponName = "打折券";
    private float faceValue;   //面值
    private String couponMsg;   //使用规则
    private int total;  //共有多少份
    private int totalReceived;  //被领取数量
    private int surplus;    //剩余数量
    private Merchant merchant;  //优惠券所对应的商家
    private Activity activity;  //优惠券对应的活动
    private Date createDate;
    private int couponType = Constant.COUPON_DISCOUNT_TYPE; //默认为打折卷
    private boolean deleted = false;    //是否已删除

    private Date startValidity; //有效起始时间
    private Date endValidity;   //有效期结束时间

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public float getFaceValue() {
        return faceValue;
    }

    public void setFaceValue(float faceValue) {
        this.faceValue = faceValue;
    }

    public String getCouponMsg() {
        return couponMsg;
    }

    public void setCouponMsg(String couponMsg) {
        this.couponMsg = couponMsg;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotalReceived() {
        return totalReceived;
    }

    public void setTotalReceived(int totalReceived) {
        this.totalReceived = totalReceived;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public int getSurplus() {
        return this.total - this.totalReceived;
    }

    public void setSurplus(int surplus) {
        this.surplus = surplus;
    }

    public int getCouponType() {
        return couponType;
    }

    public void setCouponType(int couponType) {
        this.couponType = couponType;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Date getStartValidity() {
        return startValidity;
    }

    public void setStartValidity(Date startValidity) {
        this.startValidity = startValidity;
    }

    public Date getEndValidity() {
        return endValidity;
    }

    public void setEndValidity(Date endValidity) {
        this.endValidity = endValidity;
    }
}
