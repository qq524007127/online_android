package cn.com.zhihetech.online.core.view.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import org.xutils.view.annotation.ViewInject;

import java.text.MessageFormat;
import java.util.List;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.Goods;
import cn.com.zhihetech.online.core.util.ImageLoader;
import cn.com.zhihetech.online.core.view.FixedImageView;

/**
 * Created by ShenYunjie on 2016/1/19.
 */
public class GoodsAdapter extends ZhiheAdapter<Goods, GoodsAdapter.GoodsHolder> {

    public GoodsAdapter(Context mContext, int layoutId) {
        super(mContext, layoutId);
    }

    public GoodsAdapter(Context mContext, int layoutId, List<Goods> mDatas) {
        super(mContext, layoutId, mDatas);
    }

    @Override
    public GoodsHolder onCreateViewHolder(View itemView) {
        return new GoodsHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GoodsHolder holder, Goods data) {
        ImageLoader.disPlayImage(holder.coverImg, data.getCoverImg());
        holder.goodsNameTv.setText(data.getGoodsName());
        String text = MessageFormat.format(mContext.getString(R.string.goods_price), data.getPrice());
        holder.goodsPriceTv.setText(text);
        text = MessageFormat.format(mContext.getString(R.string.goods_volume), data.getVolume());
        holder.goodsVolumeTv.setText(text);
        if (data.getCarriage() <= 0) {
            text = MessageFormat.format(mContext.getString(R.string.goods_carriage), "包邮");
        } else {
            text = MessageFormat.format(mContext.getString(R.string.goods_carriage), data.getCarriage());
        }
        holder.goodsCarriageTv.setText(text);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public class GoodsHolder extends ZhiheAdapter.BaseViewHolder {
        @ViewInject(R.id.goods_cover_fiv)
        public FixedImageView coverImg;
        @ViewInject(R.id.goods_name_tv)
        public TextView goodsNameTv;
        @ViewInject(R.id.goods_price_tv)
        public TextView goodsPriceTv;
        @ViewInject(R.id.goods_volume_tv)
        public TextView goodsVolumeTv;
        @ViewInject(R.id.goods_carriage_tv)
        public TextView goodsCarriageTv;

        public GoodsHolder(View itemView) {
            super(itemView);
        }
    }
}
