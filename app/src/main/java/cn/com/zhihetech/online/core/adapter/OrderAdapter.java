package cn.com.zhihetech.online.core.adapter;

import android.content.Context;
import android.view.View;

import java.util.List;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.Order;

/**
 * Created by ShenYunjie on 2016/1/27.
 */
public class OrderAdapter extends ZhiheAdapter<Order, OrderAdapter.OrderViewHolder> {

    public OrderAdapter(Context mContext) {
        super(mContext, R.layout.content_order_detail_item);
    }

    public OrderAdapter(Context mContext, int layoutId) {
        super(mContext, layoutId);
    }

    public OrderAdapter(Context mContext, int layoutId, List<Order> mDatas) {
        super(mContext, layoutId, mDatas);
    }

    @Override
    public OrderViewHolder onCreateViewHolder(View itemView) {
        return null;
    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, Order data) {

    }

    public class OrderViewHolder extends ZhiheAdapter.BaseViewHolder {

        public OrderViewHolder(View itemView) {
            super(itemView);
        }
    }
}
