package cn.com.zhihetech.online.bean;

import java.util.Date;

/**
 * 活动类别
 * Created by ShenYunjie on 2015/12/4.
 */
public class ActivityCategory extends BaseBean {
    private String categId;
    private String categName;
    private boolean official;    //是否是官方活动，如果是则商家自行组织的活动类别中不显示此项活动类别
    private int categType;   //活动种类，活动只能是系统常量中的活动种类的值
    private String categDesc;
    private Date createDate;


    public String getCategId() {
        return categId;
    }

    public void setCategId(String categId) {
        this.categId = categId;
    }

    public String getCategName() {
        return categName;
    }

    public void setCategName(String categName) {
        this.categName = categName;
    }

    public boolean getOfficial() {
        return official;
    }

    public void setOfficial(boolean official) {
        this.official = official;
    }

    public int getCategType() {
        return categType;
    }

    public void setCategType(int categType) {
        this.categType = categType;
    }

    public String getCategDesc() {
        return categDesc;
    }

    public void setCategDesc(String categDesc) {
        this.categDesc = categDesc;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
