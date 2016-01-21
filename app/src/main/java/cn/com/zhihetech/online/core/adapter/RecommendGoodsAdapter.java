package cn.com.zhihetech.online.core.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import org.xutils.view.annotation.ViewInject;

import java.util.List;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.Goods;
import cn.com.zhihetech.online.core.util.ImageLoader;
import cn.com.zhihetech.online.core.view.FixedImageView;
import cn.com.zhihetech.online.ui.widget.GoodsInfoActivity;

/**
 * Created by ShenYunjie on 2016/1/18.
 */
public class RecommendGoodsAdapter extends ZhiheAdapter<Goods, RecommendGoodsAdapter.RecommendGoodsHolder> {

    private boolean _isEmpty = true; //默认为空

    public RecommendGoodsAdapter(Context mContext, int layoutId, List<Goods> mDatas) {
        super(mContext, layoutId, mDatas);
        if (mDatas != null && mDatas.size() > 0) {
            _isEmpty = false;
        } else {
            this.mDatas.add(new Goods());
            _isEmpty = true;
        }
    }

    @Override
    public RecommendGoodsHolder onCreateViewHolder(View itemView) {
        return new RecommendGoodsHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecommendGoodsHolder holder, Goods data) {
        if (_isEmpty) {
            bindEmpty(holder);
            return;
        }
        bindData(holder, data);
    }

    private void bindData(RecommendGoodsHolder holder, final Goods data) {
        ImageLoader.disPlayImage(holder.recommendGoodsCoverImg, data.getCoverImg());
        holder.recommendGoodsPriceTv.setText("￥:" + data.getPrice());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, GoodsInfoActivity.class);
                intent.putExtra(GoodsInfoActivity.GOODS_ID_KEY, data.getGoodsId());
                mContext.startActivity(intent);
            }
        });
    }

    private void bindEmpty(RecommendGoodsHolder holder) {
        holder.recommendGoodsCoverImg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.home_activity));
        holder.recommendGoodsPriceTv.setVisibility(View.GONE);
        holder.itemView.setOnClickListener(null);
    }

    @Override
    public int getCount() {
        return mDatas.size() > 3 ? 3 : mDatas.size();
    }

    public class RecommendGoodsHolder extends ZhiheAdapter.BaseViewHolder {
        @ViewInject(R.id.recommend_goods_cover_fiv)
        public FixedImageView recommendGoodsCoverImg;
        @ViewInject(R.id.recommend_goods_price_tv)
        public TextView recommendGoodsPriceTv;

        public RecommendGoodsHolder(View itemView) {
            super(itemView);
        }
    }
}
