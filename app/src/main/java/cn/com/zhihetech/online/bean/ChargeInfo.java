package cn.com.zhihetech.online.bean;

public class ChargeInfo extends BaseBean {

    private String chargeInfoId;
    private int amount;
    private  String orderNo;
    private int channel;
    private String currency = "cny";
    private String clientIp;
    private String subject;
    private String body;

    public String getChargeInfoId() {
        return chargeInfoId;
    }

    public void setChargeInfoId(String chargeInfoId) {
        this.chargeInfoId = chargeInfoId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
