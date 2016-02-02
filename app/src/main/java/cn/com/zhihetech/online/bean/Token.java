package cn.com.zhihetech.online.bean;

/**
 * Created by YangDaiChun on 2015/12/22.
 */
public class Token extends BaseBean {

    private User user;
    private String token;

    public Token() {
    }

    public Token(User user, String token) {
        this.token = token;
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
