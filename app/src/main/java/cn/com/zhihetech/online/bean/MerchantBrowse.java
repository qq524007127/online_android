package cn.com.zhihetech.online.bean;

import java.util.Date;

/**
 * Created by ShenYunjie on 2016/4/19.
 */
public class MerchantBrowse {

    private String merchantBrowseId;
    private Merchant merchant;
    private User user;
    private Date browseDate;

    public String getMerchantBrowseId() {
        return merchantBrowseId;
    }

    public void setMerchantBrowseId(String merchantBrowseId) {
        this.merchantBrowseId = merchantBrowseId;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getBrowseDate() {
        return browseDate;
    }

    public void setBrowseDate(Date browseDate) {
        this.browseDate = browseDate;
    }
}
