package cn.com.zhihetech.online.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * 环信用户信息
 * Created by ShenYunjie on 2016/2/3.
 */
@Table(name = "em_user")
public class EMUserInfo extends BaseBean {
    @Column(name = "user_id", isId = true, autoGen = true)
    private int userId;
    @Column(name = "user_name", property = "NOT NULL")
    private String userName;  //环信用户唯一标示
    @Column(name = "user_nick", property = "NOT NULL")
    private String userNick;    //用户昵称
    @Column(name = "avatar_url")
    private String avatarUrl;   //用户头像地址
    @Column(name = "app_user_id", property = "NOT NULL")
    private String appUserId;   //平台用户ID
    private int userType;      //用户类型

    public EMUserInfo() {
    }

    public EMUserInfo(String userName, String userNick, String avatarUrl, String appUserId, int userType) {
        this.userName = userName;
        this.userNick = userNick;
        this.avatarUrl = avatarUrl;
        this.appUserId = appUserId;
        this.userType = userType;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(String appUserId) {
        this.appUserId = appUserId;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }
}
