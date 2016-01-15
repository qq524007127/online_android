package cn.com.zhihetech.online.bean;

import java.util.Date;

/**
 * 红包对应实体类
 * Created by ShenYunjie on 2015/12/11.
 */
public class RedEnvelop extends BaseBean {
    private String envelopId;
    private float totalMoney;   //红包金额（总额）
    private int numbers;    //红包个数
    private Merchant merchant;  //红包对应的商家
    private Activity activity;  //红包对应的活动
    private Date createDate;
    private boolean sended; //是否已发出
    private Date sendDate;  //红包发出时间
    private String envelopMsg;  //留言信息

    public String getEnvelopId() {
        return envelopId;
    }

    public void setEnvelopId(String envelopId) {
        this.envelopId = envelopId;
    }

    public float getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(float totalMoney) {
        this.totalMoney = totalMoney;
    }

    public int getNumbers() {
        return numbers;
    }

    public void setNumbers(int numbers) {
        this.numbers = numbers;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public boolean isSended() {
        return sended;
    }

    public void setSended(boolean sended) {
        this.sended = sended;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public String getEnvelopMsg() {
        return envelopMsg;
    }

    public void setEnvelopMsg(String envelopMsg) {
        this.envelopMsg = envelopMsg;
    }
}
