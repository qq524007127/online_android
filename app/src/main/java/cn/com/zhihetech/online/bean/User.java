package cn.com.zhihetech.online.bean;

import java.util.Calendar;
import java.util.Date;

import cn.com.zhihetech.online.core.common.Constant;

/**
 * Created by YangDaiChun on 2015/11/12.
 */

public class User extends BaseBean {
    private String userId;
    private String userPhone;
    private String userName;
    private String pwd;
    private int age;
    private Area area;
    private String occupation;
    private String income;
    private boolean sex;
    private Date birthday;
    private boolean permit;
    private String invitCode;
    private Date createDate;
    private int userType = Constant.COMMON_USER;    //默认为普通用户
    private ImgInfo portrait;  //用户头像

    public User() {
    }

    public User(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public boolean isPermit() {
        return permit;
    }

    public void setPermit(boolean permit) {
        this.permit = permit;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getInvitCode() {
        return invitCode;
    }

    public void setInvitCode(String invitCode) {
        this.invitCode = invitCode;
    }

    public int getAge() {
        if (birthday != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            int _now = calendar.get(Calendar.YEAR);
            calendar.setTime(birthday);
            int _birthday = calendar.get(Calendar.YEAR);
            return _now - _birthday;
        }
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getUserType() {
        return Constant.BUYER_USER;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public ImgInfo getPortrait() {
        return portrait;
    }

    public void setPortrait(ImgInfo portrait) {
        this.portrait = portrait;
    }

    /**
     * 获取用户对应的环信账号的信息
     *
     * @return
     */
    public String getEMUserId() {
        return getUserId().replaceAll("-", "");
    }

    /**
     * 获取环信账号的登录密码
     *
     * @return
     */
    public String getEMUserPwd() {
        return getUserId();
    }
}

