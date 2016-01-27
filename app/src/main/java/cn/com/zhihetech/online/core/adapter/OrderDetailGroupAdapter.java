package cn.com.zhihetech.online.core.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.xutils.view.annotation.ViewInject;

import java.text.MessageFormat;
import java.util.List;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.OrderDetail;
import cn.com.zhihetech.online.bean.OrderDetailGroup;
import cn.com.zhihetech.online.core.util.ImageLoader;
import cn.com.zhihetech.online.core.util.ListViewHeigthUtils;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ShenYunjie on 2016/1/26.
 */
public class OrderDetailGroupAdapter extends ZhiheAdapter<OrderDetailGroup, OrderDetailGroupAdapter.OrderDetailGroupHolder> {

    public OrderDetailGroupAdapter(Context mContext, List<OrderDetailGroup> mDatas) {
        this(mContext, R.layout.content_order_detail_group_item, mDatas);
    }

    public OrderDetailGroupAdapter(Context mContext, int layoutId, List<OrderDetailGroup> mDatas) {
        super(mContext, layoutId, mDatas);
    }

    @Override
    public OrderDetailGroupHolder onCreateViewHolder(View itemView) {
        return new OrderDetailGroupHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final OrderDetailGroupHolder holder, final OrderDetailGroup data) {
        ImageLoader.disPlayImage(holder.merchantCoverCiv, data.getMerchant().getCoverImg());
        holder.merchantNameTv.setText(data.getMerchant().getMerchName());
        float totalPrice = 0f;
        float goodsTotal = 0f;
        float totalCarrige = 0f;
        for (OrderDetail orderDetail : data.getOrderDetails()) {
            totalCarrige += orderDetail.getGoods().getCarriage() * orderDetail.getCount();
            totalPrice += (orderDetail.getPrice() * orderDetail.getCount());
            goodsTotal += orderDetail.getCount();
        }
        holder.orderCarriageTv.setText("运费:" + totalCarrige);
        holder.orderItemTotalTv.setText("￥" + (totalPrice + totalCarrige));
        holder.goodsCount.setText(MessageFormat.format(mContext.getString(R.string.order_detail_group_goods_count), goodsTotal));
        holder.orderDetailLv.setAdapter(new OrderDetailAdapter(mContext, R.layout.content_order_detail_item, data.getOrderDetails()));
        ListViewHeigthUtils.setListViewHeightBasedOnChildren(holder.orderDetailLv);
        holder.userMsgEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                data.setUserMsg(holder.userMsgEt.getText().toString());
            }
        });
    }

    /**
     * 获取订单总价
     *
     * @param position
     * @return
     */
    public float getTotalPrice(int position) {
        OrderDetailGroup group = mDatas.get(position);
        float totalPrice = 0f;
        float totalCarrige = 0f;
        for (OrderDetail orderDetail : group.getOrderDetails()) {
            totalCarrige += orderDetail.getGoods().getCarriage() * orderDetail.getCount();
            totalPrice += (orderDetail.getPrice() * orderDetail.getCount());
        }
        return (totalPrice + totalCarrige);
    }

    /**
     * 获取订单总运费
     *
     * @param position
     * @return
     */
    public float getTotalCarrige(int position) {
        OrderDetailGroup group = mDatas.get(position);
        float totalPrice = 0f;
        float totalCarrige = 0f;
        for (OrderDetail orderDetail : group.getOrderDetails()) {
            totalCarrige += orderDetail.getGoods().getCarriage() * orderDetail.getCount();
            totalPrice += (orderDetail.getPrice() * orderDetail.getCount()) + totalCarrige;
        }
        return totalCarrige;
    }

    /**
     * 获取订单商品数量
     *
     * @param position
     * @return
     */
    public float getGoodsCount(int position) {
        OrderDetailGroup group = mDatas.get(position);
        float goodsTotal = 0f;
        for (OrderDetail orderDetail : group.getOrderDetails()) {
            goodsTotal += orderDetail.getCount();
        }
        return goodsTotal;
    }

    /**
     * 获取订单用户留言
     *
     * @param position
     * @return
     */
    public String getUserMessage(int position) {
        return mDatas.get(position).getUserMsg();
    }

    public class OrderDetailGroupHolder extends ZhiheAdapter.BaseViewHolder {
        @ViewInject(R.id.order_detail_merchant_cover_civ)
        public CircleImageView merchantCoverCiv;
        @ViewInject(R.id.order_confirm_item_merchant_name_tv)
        public TextView merchantNameTv;
        @ViewInject(R.id.order_detail_lv)
        public ListView orderDetailLv;
        @ViewInject(R.id.order_detail_group_carriage_tv)
        public TextView orderCarriageTv;
        @ViewInject(R.id.order_item_user_msg_et)
        public EditText userMsgEt;
        @ViewInject(R.id.order_detail_group_goods_count_tv)
        public TextView goodsCount;
        @ViewInject(R.id.order_item_total_tv)
        public TextView orderItemTotalTv;

        public OrderDetailGroupHolder(View itemView) {
            super(itemView);
        }
    }
}
