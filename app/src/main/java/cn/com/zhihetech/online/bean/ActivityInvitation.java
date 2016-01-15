package cn.com.zhihetech.online.bean;

import java.util.Date;

/**
 * 活动邀请
 * Created by ShenYunjie on 2015/12/7.
 */

public class ActivityInvitation extends BaseBean {

    private String invitationId;
    private Activity activity;
    private Merchant invitedMerch;  //受邀商家
    private Date createDate;    //邀请时间
    private Date expiredDate;   //到期时间
    private String invitedMsg;  //邀请留言

    public String getInvitationId() {
        return invitationId;
    }

    public void setInvitationId(String invitationId) {
        this.invitationId = invitationId;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Merchant getInvitedMerch() {
        return invitedMerch;
    }

    public void setInvitedMerch(Merchant invitedMerch) {
        this.invitedMerch = invitedMerch;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(Date expiredDate) {
        this.expiredDate = expiredDate;
    }

    public String getInvitedMsg() {
        return invitedMsg;
    }

    public void setInvitedMsg(String invitedMsg) {
        this.invitedMsg = invitedMsg;
    }
}
