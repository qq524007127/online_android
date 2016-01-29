package cn.com.zhihetech.online.core.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.xutils.view.annotation.ViewInject;

import java.util.List;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.Order;
import cn.com.zhihetech.online.core.common.Constant;
import cn.com.zhihetech.online.core.util.DateUtils;
import cn.com.zhihetech.online.core.util.StringUtils;
import cn.com.zhihetech.online.ui.activity.OrderDetailActivity;

/**
 * Created by ShenYunjie on 2016/1/27.
 */
public class OrderAdapter extends ZhiheAdapter<Order, OrderAdapter.OrderViewHolder> {

    public OrderAdapter(Context mContext) {
        super(mContext, R.layout.content_order_item);
    }

    public OrderAdapter(Context mContext, int layoutId) {
        super(mContext, layoutId);
    }

    public OrderAdapter(Context mContext, int layoutId, List<Order> mDatas) {
        super(mContext, layoutId, mDatas);
    }

    @Override
    public OrderViewHolder onCreateViewHolder(View itemView) {
        return new OrderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, final Order data) {
        holder.orderNameTv.setText("订单名：" + data.getOrderName());
        holder.orderCodeTv.setText("订单号：" + data.getOrderCode());
        holder.orderCarriage.setText("邮费：" + data.getCarriage());
        holder.totalPriceTv.setText("总价：" + data.getOrderTotal());
        holder.orderCreateDateTv.setText("下单时间：" + DateUtils.formatDateTime(data.getCreateDate()));
        holder.orderSateTv.setText(data.getStateDisplayText());
        holder.userMsgTv.setText("留言：" + data.getUserMsg());
        holder.userMsgTv.setVisibility(StringUtils.isEmpty(data.getUserMsg()) ? View.GONE : View.VISIBLE);
        holder.receiptPersonNameTv.setText("收货人：" + data.getReceiverName());
        holder.receiptNumTv.setText("联系电话：" + data.getReceiverPhone());
        holder.receiptDetailAddressTv.setText("地址：" + data.getReceiverAdd());

        holder.cancelBtn.setVisibility(data.getOrderState() == Constant.ORDER_STATE_NO_PAYMENT ? View.VISIBLE : View.GONE);
        holder.payBtn.setVisibility(data.getOrderState() == Constant.ORDER_STATE_NO_PAYMENT ? View.VISIBLE : View.GONE);
        holder.refundBtn.setVisibility(data.getOrderState() == Constant.ORDER_STATE_NO_DISPATCHER ? View.VISIBLE : View.GONE);
        holder.receivedBtn.setVisibility(data.getOrderState() == Constant.ORDER_STATE_ALREADY_DISPATCHER ? View.VISIBLE : View.GONE);
        holder.deleteBtn.setVisibility(View.GONE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, OrderDetailActivity.class);
                intent.putExtra(OrderDetailActivity.ORDER_ID_KEY, data.getOrderId());
                mContext.startActivity(intent);
            }
        });
    }

    public class OrderViewHolder extends ZhiheAdapter.BaseViewHolder {
        @ViewInject(R.id.order_item_name_tv)
        public TextView orderNameTv;
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

        @ViewInject(R.id.order_item_cancel_btn)
        public Button cancelBtn;
        @ViewInject(R.id.order_item_pay_btn)
        public Button payBtn;
        @ViewInject(R.id.order_item_aplay_refund_btn)
        public Button refundBtn;
        @ViewInject(R.id.order_item_received_btn)
        public Button receivedBtn;
        @ViewInject(R.id.order_item_delete_btn)
        public Button deleteBtn;

        public OrderViewHolder(View itemView) {
            super(itemView);
        }
    }
}
