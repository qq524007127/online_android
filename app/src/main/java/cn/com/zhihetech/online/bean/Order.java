package cn.com.zhihetech.online.bean;

import java.util.Date;
import java.util.List;

import cn.com.zhihetech.online.core.common.Constant;

/**
 * Created by YangDaiChun on 2015/11/12.
 */

public class Order extends BaseBean {

    private String orderId;
    private String orderName;
    private User user;
    private Merchant merchant;
    private String orderCode;
    private float carriage; //运费
    private float orderTotal;
    private int orderState;
    private Date createDate;
    private Date payDate;
    private String payType;
    private String userMsg;
    private String receiverName;
    private String receiverPhone;
    private String receiverAdd;
    private String orderDetailInfo;
    private String carriageNum;  //快递公司及单号
    private List<OrderDetail> orderDetails;

    public Order() {
    }

    public Order(Goods goods) {
        this.orderName = goods.getGoodsName();
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public float getCarriage() {
        return carriage;
    }

    public void setCarriage(float carriage) {
        this.carriage = carriage;
    }

    public float getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(float orderTotal) {
        this.orderTotal = orderTotal;
    }

    public int getOrderState() {
        return orderState;
    }

    public void setOrderState(int orderState) {
        this.orderState = orderState;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getUserMsg() {
        return userMsg;
    }

    public void setUserMsg(String userMsg) {
        this.userMsg = userMsg;
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

    public String getReceiverAdd() {
        return receiverAdd;
    }

    public void setReceiverAdd(String receiverAdd) {
        this.receiverAdd = receiverAdd;
    }

    public String getOrderDetailInfo() {
        return orderDetailInfo;
    }

    public void setOrderDetailInfo(String orderDetailInfo) {
        this.orderDetailInfo = orderDetailInfo;
    }

    public String getCarriageNum() {
        return carriageNum;
    }

    public void setCarriageNum(String carriageNum) {
        this.carriageNum = carriageNum;
    }

    /**
     * 根据商品id和数量生成订单详情组合
     *
     * @param goodsIds
     * @param goodsCunts
     */
    public void createOrderDetailInfo(String[] goodsIds, int[] goodsCunts, float[] prices) {
        StringBuffer sb = new StringBuffer("");
        for (int i = 0; i < goodsIds.length; i++) {
            sb.append(goodsIds[i]).append("*").append(String.valueOf(goodsCunts[i]))
                    .append("#").append(prices[i]).append("&");
        }
        this.orderDetailInfo = sb.substring(0, sb.length() - 1);
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    /**
     * 设置收货地址
     *
     * @param address
     */
    public void setReceiptAddress(ReceivedGoodsAddress address) {
        this.receiverAdd = address.getDetailAddress();
        this.receiverName = address.getReceiverName();
        this.receiverPhone = address.getReceiverPhone();
    }

    /**
     * 设置支付渠道
     *
     * @param channel
     */
    public void setPayChannel(ChargeInfo.PayChannel channel) {
        switch (channel) {
            case ALIPAY:
                this.payType = "alipay";
                break;
            case WXPAY:
                this.payType = "wx";
                break;
        }
    }

    public String getStateDisplayText() {
        switch (this.orderState) {
            case Constant.ORDER_STATE_NO_SUBMIT:
                return "订单未提交";
            case Constant.ORDER_STATE_NO_PAYMENT:
                return "待支付";
            case Constant.ORDER_STATE_NO_DISPATCHER:
                return "待发货";
            case Constant.ORDER_STATE_ALREADY_DISPATCHER:
                return "待收货";
            case Constant.ORDER_STATE_ALREADY_DELIVER:
                return "待评价";
            case Constant.ORDER_STATE_ALREADY_CANCEL:
                return "已取消";
            case Constant.ORDER_STATE_WAIT_REFUND:
                return "正在退款";
            case Constant.ORDER_STATE_ALREADY_REFUND:
                return "已退款";
            case Constant.ORDER_STATE_ALREADY_EVALUATE:
                return "已评价";
        }
        return "";
    }
}
