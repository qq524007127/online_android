package cn.com.zhihetech.online.bean;

import java.util.Date;

/**
 * 商家联盟
 * Created by ShenYunjie on 2015/12/7.
 */

public class MerchantAlliance extends BaseBean {
    private String allianceId;
    private String allianceName;    //默认为对应的活动名称
    private Activity activity;  //此联盟对应的活动
    private Merchant merchant;   //对应的商家
    private Date createDate;    //对应商家加入此联盟的时间
    private boolean agreed; //是否参加本次活动
    private String allianceDesc;
    private boolean promoted;   //是否是活动发起商家

    public String getAllianceId() {
        return allianceId;
    }

    public void setAllianceId(String allianceId) {
        this.allianceId = allianceId;
    }

    public String getAllianceName() {
        return allianceName;
    }

    public void setAllianceName(String allianceName) {
        this.allianceName = allianceName;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
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

    public boolean isAgreed() {
        return agreed;
    }

    public void setAgreed(boolean agreed) {
        this.agreed = agreed;
    }

    public String getAllianceDesc() {
        return allianceDesc;
    }

    public void setAllianceDesc(String allianceDesc) {
        this.allianceDesc = allianceDesc;
    }

    public boolean isPromoted() {
        return promoted;
    }

    public void setPromoted(boolean promoted) {
        this.promoted = promoted;
    }
}
