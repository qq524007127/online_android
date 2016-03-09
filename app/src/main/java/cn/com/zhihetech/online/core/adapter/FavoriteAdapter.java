package cn.com.zhihetech.online.core.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.xutils.view.annotation.ViewInject;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.FocusGoods;
import cn.com.zhihetech.online.bean.Goods;
import cn.com.zhihetech.online.core.util.ImageLoader;
import cn.com.zhihetech.online.ui.activity.GoodsInfoActivity;

/**
 * Created by ShenYunjie on 2016/3/9.
 */
public class FavoriteAdapter extends ZhiheAdapter<FocusGoods, FavoriteAdapter.FavoriteHolder> {

    public FavoriteAdapter(Context mContext) {
        super(mContext, R.layout.content_favorite_item);
    }

    @Override
    public FavoriteHolder onCreateViewHolder(View itemView) {
        return new FavoriteHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FavoriteHolder holder, FocusGoods data) {
        final Goods goods = data.getGoods();
        ImageLoader.disPlayImage(holder.goodsCoverImg, goods.getCoverImg());
        holder.goodsNameTv.setText(goods.getGoodsName());
        holder.goodsPriceTv.setText("￥" + goods.getPrice());
        holder.goodsStockTv.setText("库存：" + "(" + goods.getCurrentStock() + ")");
    }

    public class FavoriteHolder extends ZhiheAdapter.BaseViewHolder {

        @ViewInject(R.id.fav_goods_cover_iv)
        public ImageView goodsCoverImg;
        @ViewInject(R.id.fav_goods_name_tv)
        public TextView goodsNameTv;
        @ViewInject(R.id.fav_goods_price_tv)
        public TextView goodsPriceTv;
        @ViewInject(R.id.fav_goods_stock_tv)
        public TextView goodsStockTv;

        public FavoriteHolder(View itemView) {
            super(itemView);
        }
    }
}
