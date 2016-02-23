package cn.com.zhihetech.online.core.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.xutils.view.annotation.ViewInject;

import java.util.List;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.OrderDetail;
import cn.com.zhihetech.online.core.util.ImageLoader;

/**
 * Created by ShenYunjie on 2016/1/27.
 */
public class OrderDetailAdapter extends ZhiheAdapter<OrderDetail, OrderDetailAdapter.OrderDetailHolder> {

    public OrderDetailAdapter(Context mContext, int layoutId) {
        super(mContext, layoutId);
    }

    public OrderDetailAdapter(Context mContext, int layoutId, List<OrderDetail> mDatas) {
        super(mContext, layoutId, mDatas);
    }

    @Override
    public OrderDetailHolder onCreateViewHolder(View itemView) {
        return new OrderDetailHolder(itemView);
    }

    @Override
    public void onBindViewHolder(OrderDetailHolder holder, OrderDetail data) {
        ImageLoader.disPlayImage(holder.goodsCoverIv, data.getGoods().getCoverImg());
        holder.goodsNameTv.setText(data.getGoods().getGoodsName());
        holder.detailCount.setText("数量:" + data.getCount());
        holder.detailPrice.setText("￥" + data.getPrice());
        /*holder.detailCarriage.setText("邮费:包邮");
        if (data.getOrder().getCarriage() > 0) {
            holder.detailCarriage.setText("邮费:" + data.getOrder().getCarriage());
        }*/
    }

    public class OrderDetailHolder extends ZhiheAdapter.BaseViewHolder {
        @ViewInject(R.id.order_detail_goods_cover_iv)
        public ImageView goodsCoverIv;
        @ViewInject(R.id.order_detail_goods_name_tv)
        public TextView goodsNameTv;
        @ViewInject(R.id.order_detail_price_tv)
        public TextView detailPrice;
        @ViewInject(R.id.order_detail_carriage_tv)
        public TextView detailCarriage;
        @ViewInject(R.id.order_detail_count_tv)
        public TextView detailCount;

        public OrderDetailHolder(View itemView) {
            super(itemView);
        }
    }
}
