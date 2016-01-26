package cn.com.zhihetech.online.core.adapter;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.OrderConfirm;
import cn.com.zhihetech.online.bean.OrderDetail;

/**
 * Created by ShenYunjie on 2016/1/26.
 */
public class OrderConfirmAdapter extends ZhiheAdapter<OrderDetail, OrderConfirmAdapter.OrderCfirmHolder> {
    public OrderConfirmAdapter(Context mContext, List<OrderDetail> mDatas) {
        this(mContext, R.layout.content_order_confirm_item, mDatas);
    }

    public OrderConfirmAdapter(Context mContext, int layoutId, List<OrderDetail> mDatas) {
        super(mContext, layoutId, mDatas);
    }

    @Override
    public OrderCfirmHolder onCreateViewHolder(View itemView) {
        return new OrderCfirmHolder(itemView);
    }

    @Override
    public void onBindViewHolder(OrderCfirmHolder holder, OrderDetail data) {
        holder.merchantNameTv.setText(data.getGoods().getMerchant().getMerchName());
        holder.orderItemTotalTv.setText("￥" + (data.getGoods().getPrice() * data.getCount()));
    }

    public class OrderCfirmHolder extends ZhiheAdapter.BaseViewHolder {

        @ViewInject(R.id.order_confirm_item_merchant_name_tv)
        public TextView merchantNameTv;
        @ViewInject(R.id.order_item_user_msg_et)
        public EditText userMsgEt;
        @ViewInject(R.id.order_item_total_tv)
        public TextView orderItemTotalTv;

        public OrderCfirmHolder(View itemView) {
            super(itemView);
        }
    }
}
