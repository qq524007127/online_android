package cn.com.zhihetech.online.bean;

/**
 * Created by ShenYunjie on 2016/2/29.
 */
public class MerchantToken extends BaseBean {
    private Merchant merchant;
    private String token;

    public MerchantToken() {
    }

    public MerchantToken(Merchant merchant, String token) {
        this.merchant = merchant;
        this.token = token;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
