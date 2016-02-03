package cn.com.zhihetech.online.bean;

/**
 * 环信用户信息
 * Created by ShenYunjie on 2016/2/3.
 */
public class EMUserInfo extends BaseBean {
    private String userId;  //环信用户唯一标示
    private String userNick;    //用户昵称
    private String avatarUrl;   //用户头像地址
    private int userType;      //用户类型

    public EMUserInfo() {
    }

    public EMUserInfo(String userId, String userNick, String avatarUrl, int userType) {
        this.userId = userId;
        this.userNick = userNick;
        this.avatarUrl = avatarUrl;
        this.userType = userType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserNick() {
        return userNick;
    }

    public void setUserNick(String userNick) {
        this.userNick = userNick;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }
}
