package cn.com.zhihetech.online.bean;

/**
 * Created by YangDaiChun on 2015/11/12.
 */

public class ReceivedGoodsAddress extends BaseBean {

    private String receivedGoodsId;
    private User user;
    private String receiverName;
    private String receiverPhone;
    private String detailAddress;

    public String getReceivedGoodsId() {
        return receivedGoodsId;
    }

    public void setReceivedGoodsId(String receivedGoodsId) {
        this.receivedGoodsId = receivedGoodsId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }
}
