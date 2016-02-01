package cn.com.zhihetech.online.bean;

import com.easemob.chat.EMConversation;

/**
 * Created by ShenYunjie on 2016/2/1.
 */
public class Conversation extends BaseBean {
    private String userId;    //对应用户ID或商家ID
    private String nickName;    //当前对应聊天的用户昵称
    private String headerImg;   //头像地址
    private EMConversation emConver; //对应的环信对话

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeaderImg() {
        return headerImg;
    }

    public void setHeaderImg(String headerImg) {
        this.headerImg = headerImg;
    }

    public EMConversation getEmConver() {
        return emConver;
    }

    public void setEmConver(EMConversation emConver) {
        this.emConver = emConver;
    }
}
