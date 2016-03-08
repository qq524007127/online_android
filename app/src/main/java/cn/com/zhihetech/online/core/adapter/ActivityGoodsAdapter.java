package cn.com.zhihetech.online.core.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;
import org.xutils.view.annotation.ViewInject;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.ActivityGoods;
import cn.com.zhihetech.online.bean.Goods;
import cn.com.zhihetech.online.core.util.ImageLoader;

/**
 * Created by ShenYunjie on 2016/3/7.
 */
public class ActivityGoodsAdapter extends ZhiheAdapter<ActivityGoods, ActivityGoodsAdapter.ActivityGoodsHolder> {

    public ActivityGoodsAdapter(Context mContext) {
        super(mContext, R.layout.content_activity_goods_item);
    }

    @Override
    public ActivityGoodsHolder onCreateViewHolder(View itemView) {
        return new ActivityGoodsHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ActivityGoodsHolder holder, ActivityGoods data) {
        Goods goods = data.getGoods();
        ImageLoader.disPlayImage(holder.goodsCoverImg, goods.getCoverImg());
        holder.goodsNameTv.setText(goods.getGoodsName());
        holder.goodsPriceTv.setText("原价:" + goods.getPrice());
        holder.seckillPriceTv.setText("现价:" + data.getActivityPrice());
        holder.goodsDescTv.setText("");
    }

    public class ActivityGoodsHolder extends ZhiheAdapter.BaseViewHolder {
        @ViewInject(R.id.activity_goods_list_cover_iv)
        public ImageView goodsCoverImg;
        @ViewInject(R.id.activity_goods_list_name_tv)
        public TextView goodsNameTv;
        @ViewInject(R.id.activity_goods_list_seckill_price_tv)
        public TextView seckillPriceTv;
        @ViewInject(R.id.activity_goods_list_price_tv)
        public TextView goodsPriceTv;
        @ViewInject(R.id.activity_goods_list_desc_tv)
        public TextView goodsDescTv;

        public ActivityGoodsHolder(View itemView) {
            super(itemView);
            this.goodsPriceTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }
}
