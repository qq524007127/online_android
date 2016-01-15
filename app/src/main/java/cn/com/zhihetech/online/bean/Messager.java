package cn.com.zhihetech.online.bean;

import java.util.Date;

public class Messager extends BaseBean {

    private String messagerId;
    private String phoneNumber;
    private String securityCode;  //发送的验证码
    private String securityMsg;    //发送的验证信息
    private Date sendDate;          //发送时间
    private Date validity;                  //有效期

    public String getMessagerId() {
        return messagerId;
    }

    public void setMessagerId(String messagerId) {
        this.messagerId = messagerId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }

    public String getSecurityMsg() {
        return securityMsg;
    }

    public void setSecurityMsg(String securityMsg) {
        this.securityMsg = securityMsg;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }


    public Date getValidity() {
        return validity;
    }

    public void setValidity(Date validity) {
        this.validity = validity;
    }
}
