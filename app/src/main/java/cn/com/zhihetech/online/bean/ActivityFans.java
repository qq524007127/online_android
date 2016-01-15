package cn.com.zhihetech.online.bean;

import java.util.Date;

public class ActivityFans extends BaseBean {
    private String fansId;
    private Activity activity;  //对应的活动
    private Merchant invitationMerch;   //邀请加入的商家
    private Date joinDate;  //加入活动时间
    private User user;  //用户

    public String getFansId() {
        return fansId;
    }

    public void setFansId(String fansId) {
        this.fansId = fansId;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Merchant getInvitationMerch() {
        return invitationMerch;
    }

    public void setInvitationMerch(Merchant invitationMerch) {
        this.invitationMerch = invitationMerch;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
