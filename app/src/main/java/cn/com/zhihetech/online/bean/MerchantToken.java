package cn.com.zhihetech.online.bean;

/**
 * Created by ShenYunjie on 2016/2/29.
 */
public class MerchantToken extends BaseBean {
    private Merchant merchant;
    private String token;
    private ChatUserInfo chatUser;

    public MerchantToken() {
    }

    public MerchantToken(Merchant merchant, String token) {
        this.merchant = merchant;
        this.token = token;
    }

    public MerchantToken(Merchant merchant, String token, ChatUserInfo chatUserInfo) {
        this.merchant = merchant;
        this.token = token;
        this.chatUser = chatUserInfo;
    }

    public ChatUserInfo getChatUser() {
        return chatUser;
    }

    public void setChatUser(ChatUserInfo chatUser) {
        this.chatUser = chatUser;
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
