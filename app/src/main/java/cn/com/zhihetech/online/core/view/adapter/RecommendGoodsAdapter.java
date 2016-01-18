package cn.com.zhihetech.online.core.view.adapter;

import android.content.Context;
import android.view.View;

import java.util.List;

import cn.com.zhihetech.online.bean.Goods;

/**
 * Created by ShenYunjie on 2016/1/18.
 */
public class RecommendGoodsAdapter extends ZhiheAdapter<Goods,RecommendGoodsAdapter.RecommendGoodsHolder>{

    public RecommendGoodsAdapter(Context mContext, int layoutId) {
        super(mContext, layoutId);
    }

    public RecommendGoodsAdapter(Context mContext, int layoutId, List<Goods> mDatas) {
        super(mContext, layoutId, mDatas);
    }

    @Override
    public RecommendGoodsHolder onCreateViewHolder(View itemView) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecommendGoodsHolder holder, Goods data) {

    }

    public class RecommendGoodsHolder extends ZhiheAdapter.BaseViewHolder{

        public RecommendGoodsHolder(View itemView) {
            super(itemView);
        }
    }
}
