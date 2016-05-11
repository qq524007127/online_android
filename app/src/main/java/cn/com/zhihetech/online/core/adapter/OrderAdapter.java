package cn.com.zhihetech.online.core.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.easemob.easeui.widget.EaseImageView;

import org.xutils.view.annotation.ViewInject;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.Merchant;
import cn.com.zhihetech.online.bean.Order;
import cn.com.zhihetech.online.core.common.Constant;
import cn.com.zhihetech.online.core.util.DateUtils;
import cn.com.zhihetech.online.core.util.ImageLoader;
import cn.com.zhihetech.online.core.util.StringUtils;
import cn.com.zhihetech.online.ui.activity.MerchantHomeActivity;
import cn.com.zhihetech.online.ui.activity.OrderDetailActivity;

/**
 * Created by ShenYunjie on 2016/1/27.
 */
public class OrderAdapter extends ZhiheAdapter<Order, OrderAdapter.OrderViewHolder> {

    private OnOrderItemClickListener itemClickListener;

    public OrderAdapter(Context mContext) {
        this(mContext, R.layout.content_order_item);
    }

    public OrderAdapter(Context mContext, int layoutId) {
        super(mContext, layoutId);
    }

    @Override
    public OrderViewHolder onCreateViewHolder(View itemView) {
        return new OrderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, final Order data) {
        String orderName = data.getOrderName();
        orderName = orderName.length() > 100 ? orderName.substring(0, 100) + "..." : orderName;
        holder.orderName.setText(orderName);
        holder.orderCodeTv.setText("订单号：" + data.getOrderCode());
        holder.orderCarriage.setText("邮费：" + data.getCarriage());
        holder.totalPriceTv.setText("总价：" + data.getOrderTotal() + "元");
        holder.orderCreateDateTv.setText("下单时间：" + DateUtils.formatDateTime(data.getCreateDate()));
        holder.orderSateTv.setText(data.getStateDisplayText());
        holder.userMsgTv.setText("留言：" + data.getUserMsg());
        holder.userMsgTv.setVisibility(StringUtils.isEmpty(data.getUserMsg()) ? View.GONE : View.VISIBLE);
        holder.receiptPersonNameTv.setText("收货人：" + data.getReceiverName());
        holder.receiptNumTv.setText("联系电话：" + data.getReceiverPhone());
        holder.receiptDetailAddressTv.setText("地址：" + data.getReceiverAdd());
        ImageLoader.disPlayImage(holder.orderMerchantHeaderIv, data.getMerchant().getCoverImg());
        holder.orderMerchantNameTv.setText(data.getMerchant().getMerchName());

        holder.carriageInfoTv.setVisibility(View.GONE);
        if (data.getOrderState() == Constant.ORDER_STATE_ALREADY_DISPATCHER && !StringUtils.isEmpty(data.getCarriageNum())) {
            holder.carriageInfoTv.setVisibility(View.VISIBLE);
            holder.carriageInfoTv.setText("快递信息：" + data.getCarriageNum());
        }

        /**
         * 点击商家信息进入商家界面
         */
        holder.orderMerchanLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Merchant merchant = data.getMerchant();
                Intent intent = new Intent(mContext, MerchantHomeActivity.class);
                intent.putExtra(MerchantHomeActivity.MERCHANT_ID_KEY, merchant.getMerchantId());
                intent.putExtra(MerchantHomeActivity.MERCHANT_NAME_KEY, merchant.getMerchName());
                mContext.startActivity(intent);
            }
        });

        /**
         * 点击订单详情监听
         */
        holder.orderDetailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, OrderDetailActivity.class);
                intent.putExtra(OrderDetailActivity.ORDER_ID_KEY, data.getOrderId());
                mContext.startActivity(intent);
            }
        });

        initItemState(holder, data);

        initItemClick(holder, data);
    }

    /**
     * 初始化订单按钮显示状态,先隐藏所有功能按钮，再根据订单状态显示所需按钮
     *
     * @param holder
     * @param data
     */
    private void initItemState(OrderViewHolder holder, Order data) {
        hideAllItemView(holder);
        holder.refundBtn.setVisibility(data.getOrderState() == Constant.ORDER_STATE_NO_DISPATCHER ? View.VISIBLE : View.GONE);
        holder.receivedBtn.setVisibility(data.getOrderState() == Constant.ORDER_STATE_ALREADY_DISPATCHER ? View.VISIBLE : View.GONE);
        holder.deleteBtn.setVisibility(View.GONE);
        switch (data.getOrderState()) {
            case Constant.ORDER_STATE_NO_PAYMENT:
                holder.cancelBtn.setVisibility(View.VISIBLE);
                holder.payBtn.setVisibility(View.VISIBLE);
                break;
            case Constant.ORDER_STATE_NO_DISPATCHER:
                holder.refundBtn.setVisibility(View.VISIBLE);
                break;
            case Constant.ORDER_STATE_ALREADY_DISPATCHER:
                holder.receivedBtn.setVisibility(View.VISIBLE);
                break;
            case Constant.ORDER_STATE_ALREADY_DELIVER:
                holder.evaluateBtn.setVisibility(View.VISIBLE);
                break;
            case Constant.ORDER_STATE_ALREADY_CANCEL:
                holder.deleteBtn.setVisibility(View.VISIBLE);
                break;
            case Constant.ORDER_STATE_ALREADY_REFUND:
                holder.deleteBtn.setVisibility(View.VISIBLE);
                break;
            case Constant.ORDER_STATE_ALREADY_EVALUATE:
                holder.deleteBtn.setVisibility(View.VISIBLE);
                break;
        }
    }

    /**
     * 隐藏所有功能按钮
     *
     * @param holder
     */
    private void hideAllItemView(OrderViewHolder holder) {
        holder.cancelBtn.setVisibility(View.GONE);
        holder.payBtn.setVisibility(View.GONE);
        holder.refundBtn.setVisibility(View.GONE);
        holder.receivedBtn.setVisibility(View.GONE);
        holder.evaluateBtn.setVisibility(View.GONE);
        holder.deleteBtn.setVisibility(View.GONE);
    }

    /**
     * 初始化订单功能按钮点击监听
     *
     * @param holder
     * @param data
     */
    private void initItemClick(OrderViewHolder holder, Order data) {
        if (this.itemClickListener == null) {
            return;
        }
        OnViewClickListener clickListener = new OnViewClickListener(data, this.itemClickListener);
        holder.cancelBtn.setOnClickListener(clickListener);
        holder.payBtn.setOnClickListener(clickListener);
        holder.refundBtn.setOnClickListener(clickListener);
        holder.receivedBtn.setOnClickListener(clickListener);
        holder.evaluateBtn.setOnClickListener(clickListener);
        holder.deleteBtn.setOnClickListener(clickListener);
    }

    public void setItemClickListener(OnOrderItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    private class OnViewClickListener implements View.OnClickListener {
        private Order order;
        private OnOrderItemClickListener itemClickListener;

        public OnViewClickListener(Order order, OnOrderItemClickListener itemClickListener) {
            this.order = order;
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            if (order == null || itemClickListener == null) {
                return;
            }
            switch (v.getId()) {
                case R.id.order_item_cancel_btn:
                    itemClickListener.onCancelClick(order, v);
                    break;
                case R.id.order_item_pay_btn:
                    itemClickListener.onPayClick(order, v);
                    break;
                case R.id.order_item_apply_refund_btn:
                    itemClickListener.onRefundClick(order, v);
                    break;
                case R.id.order_item_received_btn:
                    itemClickListener.onReceiptClick(order, v);
                    break;
                case R.id.order_item_evaluate:
                    itemClickListener.onEvaluateClick(order, v);
                    break;
                case R.id.order_item_delete_btn:
                    itemClickListener.onDeleteClick(order, v);
                    break;
            }
        }
    }

    public class OrderViewHolder extends ZhiheAdapter.BaseViewHolder {
        @ViewInject(R.id.order_merchant_header_iv)
        public EaseImageView orderMerchantHeaderIv;
        @ViewInject(R.id.order_merchant_name_tv)
        public TextView orderMerchantNameTv;
        @ViewInject(R.id.order_detail_view)
        public TextView orderDetail;
        @ViewInject(R.id.order_name_tv)
        public TextView orderName;
        @ViewInject(R.id.order_item_code_tv)
        public TextView orderCodeTv;
        @ViewInject(R.id.order_carriage_tv)
        public TextView orderCarriage;
        @ViewInject(R.id.order_total_price_tv)
        public TextView totalPriceTv;
        @ViewInject(R.id.order_item_create_date_tv)
        public TextView orderCreateDateTv;
        @ViewInject(R.id.order_state__tv)
        public TextView orderSateTv;
        @ViewInject(R.id.order_user_msg_tv)
        public TextView userMsgTv;
        @ViewInject(R.id.order_receipt_person_name_tv)
        public TextView receiptPersonNameTv;
        @ViewInject(R.id.order_receipt_person_num_tv)
        public TextView receiptNumTv;
        @ViewInject(R.id.order_receipt_detail_address_tv)
        public TextView receiptDetailAddressTv;
        @ViewInject(R.id.order_carriage_info_tv)
        public TextView carriageInfoTv;

        @ViewInject(R.id.order_merchant_layout)
        public View orderMerchanLayout;
        @ViewInject(R.id.order_detail_layout)
        public View orderDetailLayout;

        @ViewInject(R.id.order_item_cancel_btn)
        public Button cancelBtn;
        @ViewInject(R.id.order_item_pay_btn)
        public Button payBtn;
        @ViewInject(R.id.order_item_apply_refund_btn)
        public Button refundBtn;
        @ViewInject(R.id.order_item_received_btn)
        public Button receivedBtn;
        @ViewInject(R.id.order_item_evaluate)
        public Button evaluateBtn;
        @ViewInject(R.id.order_item_delete_btn)
        public Button deleteBtn;

        public OrderViewHolder(View itemView) {
            super(itemView);
        }
    }

    public interface OnOrderItemClickListener {
        /**
         * 取消订单
         */
        void onCancelClick(Order order, View view);

        /**
         * 订单支付
         */
        void onPayClick(Order order, View view);

        /**
         * 退款
         */
        void onRefundClick(Order order, View view);

        /**
         * 签收
         */
        void onReceiptClick(Order order, View view);

        /**
         * 评价
         */
        void onEvaluateClick(Order order, View view);

        /**
         * 删除订单
         */
        void onDeleteClick(Order order, View view);
    }
}
