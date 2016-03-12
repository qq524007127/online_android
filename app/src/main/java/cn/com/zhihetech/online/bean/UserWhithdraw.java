package cn.com.zhihetech.online.bean;

import java.util.Date;

/**
 * 用户提现申请
 * Created by ShenYunjie on 2016/3/12.
 */
public class UserWhithdraw extends BaseBean {

    public static final int APPLY_WITHDRAW = 1;  //申请提现
    public static final int WITHDRAW_OK = 2;  //提现成功
    public static final int WITHDRAW_ERR = 3;  //提现失败

    private String userWithdrawId;   //id
    private float money;  //提现的钱
    private float poundage;  //手续费
    private float realMoney;  //实际提现的钱
    private Date applyDate;  //申请提现的日期
    private Date withdrawDate;   //管理员确认提现日期
    private String operator;  //进行提现确认的企业名称
    private String aliCode;   //支付宝账号
    private int withdrawState = APPLY_WITHDRAW;  //状态
    private String reason;  //原因

    public String getUserWithdrawId() {
        return userWithdrawId;
    }

    public void setUserWithdrawId(String userWithdrawId) {
        this.userWithdrawId = userWithdrawId;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public float getPoundage() {
        return poundage;
    }

    public void setPoundage(float poundage) {
        this.poundage = poundage;
    }

    public float getRealMoney() {
        return realMoney;
    }

    public void setRealMoney(float realMoney) {
        this.realMoney = realMoney;
    }

    public Date getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
    }

    public Date getWithdrawDate() {
        return withdrawDate;
    }

    public void setWithdrawDate(Date withdrawDate) {
        this.withdrawDate = withdrawDate;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getAliCode() {
        return aliCode;
    }

    public void setAliCode(String aliCode) {
        this.aliCode = aliCode;
    }

    public int getWithdrawState() {
        return withdrawState;
    }

    public void setWithdrawState(int withdrawState) {
        this.withdrawState = withdrawState;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String displayState() {
        switch (withdrawState) {
            case WITHDRAW_OK:
                return "提现成功";
            case WITHDRAW_ERR:
                return "提现失败";
        }
        return "已申请，待提现";
    }
}
