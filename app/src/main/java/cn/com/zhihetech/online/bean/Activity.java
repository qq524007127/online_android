package cn.com.zhihetech.online.bean;
import java.util.Date;

import cn.com.zhihetech.online.core.common.Constant;

public class Activity extends BaseBean {
    private String activitId;
    private String activitName;
    private String activitDetail;   //活动详情介绍
    private ImgInfo coverImg;   //活动封面图
    private Merchant activityPromoter;   //活动发起商家
    private ActivityCategory category;  //活动类别
    private GoodsAttributeSet attributeSet; //活动商品的类别
    private int currentState = Constant.ACTIVITY_STATE_UNSBUMIT; //活动当前状态（对应常数中的活动状态）
    private Date beginDate; //活动开始时间
    private Date endDate;   //活动结束时间
    private Date createDate;    //活动创建时间
    private String contacterName;   //活动负责人姓名
    private String contactTell;   //活动负责人联系电话
    private String activitDesc; //活动备注信息
    private String displayState; //显示当前状态

    public String getActivitId() {
        return activitId;
    }

    public void setActivitId(String activitId) {
        this.activitId = activitId;
    }

    public String getActivitName() {
        return activitName;
    }

    public void setActivitName(String activitName) {
        this.activitName = activitName;
    }

    public String getActivitDetail() {
        return activitDetail;
    }

    public void setActivitDetail(String activitDetail) {
        this.activitDetail = activitDetail;
    }

    public ImgInfo getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(ImgInfo coverImg) {
        this.coverImg = coverImg;
    }

    public Merchant getActivityPromoter() {
        return activityPromoter;
    }

    public void setActivityPromoter(Merchant activityPromoter) {
        this.activityPromoter = activityPromoter;
    }

    public ActivityCategory getCategory() {
        return category;
    }

    public void setCategory(ActivityCategory category) {
        this.category = category;
    }

    public GoodsAttributeSet getAttributeSet() {
        return attributeSet;
    }

    public void setAttributeSet(GoodsAttributeSet attributeSet) {
        this.attributeSet = attributeSet;
    }

    public int getCurrentState() {
        return currentState;
    }

    public void setCurrentState(int currentState) {
        this.currentState = currentState;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getContacterName() {
        return contacterName;
    }

    public void setContacterName(String contacterName) {
        this.contacterName = contacterName;
    }

    public String getContactTell() {
        return contactTell;
    }

    public void setContactTell(String contactTell) {
        this.contactTell = contactTell;
    }

    public String getActivitDesc() {
        return activitDesc;
    }

    public void setActivitDesc(String activitDesc) {
        this.activitDesc = activitDesc;
    }

    public String getDisplayState() {
        switch (currentState) {
            case Constant.ACTIVITY_STATE_UNSBUMIT:
                displayState = "未提交";
                break;
            case Constant.ACTIVITY_STATE_SBUMITED:
                displayState = "待审核";
                break;
            case Constant.ACTIVITY_STATE_EXAMINED_NOT:
                displayState = "审核未通过";
                break;
            case Constant.ACTIVITY_STATE_EXAMINED_OK:
                displayState = "已审核";
                break;
            case Constant.ACTIVITY_STATE_STARTED:
                displayState = "已开始";
                break;
            case Constant.ACTIVITY_STATE_FNISHED:
                displayState = "已结束";
                break;
        }
        return displayState;
    }

    public void setDisplayState(String displayState) {
        this.displayState = displayState;
    }
}
