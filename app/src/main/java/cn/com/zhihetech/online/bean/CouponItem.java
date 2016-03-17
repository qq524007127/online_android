package cn.com.zhihetech.online.bean;

import java.util.Date;

/**
 * Created by YangDaiChun on 2016/3/9.
 */
public class CouponItem extends BaseBean {

    private String couponItemId;
    private Coupon coupon;
    private String couponName = "打折券";
    private User user;  //领取的用户
    private Date receivedDate;  //领取时间
    private boolean useState = false;  //是否已经使用,默认为未使用
    private Date useDate;  //优惠券的使用时间
    private String code;    //优惠券验证码

    public String getCouponItemId() {
        return couponItemId;
    }

    public void setCouponItemId(String couponItemId) {
        this.couponItemId = couponItemId;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public Date getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(Date receivedDate) {
        this.receivedDate = receivedDate;
    }

    public boolean isUseState() {
        return useState;
    }

    public void setUseState(boolean useState) {
        this.useState = useState;
    }

    public Date getUseDate() {
        return useDate;
    }

    public void setUseDate(Date useDate) {
        this.useDate = useDate;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
