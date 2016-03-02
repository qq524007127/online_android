package cn.com.zhihetech.online.core.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.xutils.view.annotation.ViewInject;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.Goods;
import cn.com.zhihetech.online.core.util.ImageLoader;

/**
 * Created by ShenYunjie on 2016/3/2.
 */
public class GoodsListAdapter extends ZhiheAdapter<Goods, GoodsListAdapter.GoodsListHolder> {

    public GoodsListAdapter(Context mContext) {
        super(mContext, R.layout.content_goods_list_item);
    }

    @Override
    public GoodsListHolder onCreateViewHolder(View itemView) {
        return new GoodsListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GoodsListHolder holder, Goods data) {
        ImageLoader.disPlayImage(holder.coverImg, data.getCoverImg());
        holder.goodsNameTv.setText(data.getGoodsName());
        holder.goodsPriceTv.setText("￥:" + data.getPrice());
        holder.goodsStockTv.setText("库存:" + data.getCurrentStock());
    }

    public class GoodsListHolder extends ZhiheAdapter.BaseViewHolder {

        @ViewInject(R.id.goods_list_cover_iv)
        public ImageView coverImg;
        @ViewInject(R.id.goods_list_name_tv)
        public TextView goodsNameTv;
        @ViewInject(R.id.goods_list_price_tv)
        public TextView goodsPriceTv;
        @ViewInject(R.id.goods_list_stock_tv)
        public TextView goodsStockTv;

        public GoodsListHolder(View itemView) {
            super(itemView);
        }
    }
}
