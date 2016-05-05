package cn.com.zhihetech.online.bean;

/**
 * 环信用户信息
 * Created by ShenYunjie on 2016/5/5.
 */
public class ChatUserInfo extends BaseBean {
    private String userName;    //环信账号
    private String userPwd;     //环信密码
    private String nickName;    //环信昵称
    private String portraitUrl; //头像地址
    private String appUserId;   //对应业务服务的UserId

    public ChatUserInfo() {
    }

    public ChatUserInfo(String userName, String userPwd) {
        this.userName = userName;
        this.userPwd = userPwd;
    }

    public ChatUserInfo(String userName, String userPwd, String nickName, String portraitUrl) {
        this.userName = userName;
        this.userPwd = userPwd;
        this.nickName = nickName;
        this.portraitUrl = portraitUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPortraitUrl() {
        return portraitUrl;
    }

    public void setPortraitUrl(String portraitUrl) {
        this.portraitUrl = portraitUrl;
    }

    public String getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(String appUserId) {
        this.appUserId = appUserId;
    }
}
