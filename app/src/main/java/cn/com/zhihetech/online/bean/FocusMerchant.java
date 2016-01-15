package cn.com.zhihetech.online.bean;

import java.util.Date;

public class FocusMerchant extends BaseBean {

    private String focusMerchantId;
    private User user;
    private Merchant merchant;
    private Date focusDate;

    public String getFocusMerchantId() {
        return focusMerchantId;
    }

    public void setFocusMerchantId(String focusMerchantId) {
        this.focusMerchantId = focusMerchantId;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public Date getFocusDate() {
        return focusDate;
    }

    public void setFocusDate(Date focusDate) {
        this.focusDate = focusDate;
    }
}
