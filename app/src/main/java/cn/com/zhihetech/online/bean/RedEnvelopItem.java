package cn.com.zhihetech.online.bean;

import java.util.Date;

/**
 * 红包裂变明细
 * Created by ShenYunjie on 2015/12/14.
 */

public class RedEnvelopItem extends BaseBean {
    private String envelopItemId;
    private float amountOfMoney;    //金额
    private boolean received;   //是否已被领取
    private Date receivedDate;  //红包领取时间
    private RedEnvelop redEnvelop;
    private boolean extractState = false;   //是否已提取到我的钱包

    public String getEnvelopItemId() {
        return envelopItemId;
    }

    public void setEnvelopItemId(String envelopItemId) {
        this.envelopItemId = envelopItemId;
    }


    public float getAmountOfMoney() {
        return amountOfMoney;
    }

    public void setAmountOfMoney(float amountOfMoney) {
        this.amountOfMoney = amountOfMoney;
    }

    public boolean isReceived() {
        return received;
    }

    public void setReceived(boolean received) {
        this.received = received;
    }

    public Date getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(Date receivedDate) {
        this.receivedDate = receivedDate;
    }

    public RedEnvelop getRedEnvelop() {
        return redEnvelop;
    }

    public void setRedEnvelop(RedEnvelop redEnvelop) {
        this.redEnvelop = redEnvelop;
    }

    public boolean isExtractState() {
        return extractState;
    }

    public void setExtractState(boolean extractState) {
        this.extractState = extractState;
    }
}
