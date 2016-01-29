package cn.com.zhihetech.online.bean;

/**
 * Created by YangDaiChun on 2015/12/22.
 */
public class Token extends BaseBean {

    private String userID;
    private String token;

    public Token() {
    }

    public Token(String userID, String token) {
        this.userID = userID;
        this.token = token;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
