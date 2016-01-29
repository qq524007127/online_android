package cn.com.zhihetech.online.core.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import org.xutils.view.annotation.ViewInject;

import java.util.List;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.GoodsAttributeSet;
import cn.com.zhihetech.online.core.util.ImageLoader;
import cn.com.zhihetech.online.core.view.FixedImageView;
import cn.com.zhihetech.online.ui.activity.CategoryDetailActivity;

/**
 * Created by ShenYunjie on 2016/1/21.
 */
public class GoodsAttributeSetAdapter extends ZhiheAdapter<GoodsAttributeSet, GoodsAttributeSetAdapter.GoodsAttributeSetHolder> {
    public GoodsAttributeSetAdapter(Context mContext, int layoutId) {
        super(mContext, layoutId);
    }

    public GoodsAttributeSetAdapter(Context mContext, int layoutId, List<GoodsAttributeSet> mDatas) {
        super(mContext, layoutId, mDatas);
    }

    @Override
    public GoodsAttributeSetHolder onCreateViewHolder(View itemView) {
        return new GoodsAttributeSetHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GoodsAttributeSetHolder holder, final GoodsAttributeSet data) {
        ImageLoader.disPlayImage(holder.coverImg, data.getCoverImg());
        holder.nameTv.setText(data.getGoodsAttSetName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CategoryDetailActivity.class);
                intent.putExtra(CategoryDetailActivity.CATETGORY_ID_KEY, data.getGoodsAttSetId());
                intent.putExtra(CategoryDetailActivity.CATEGORY_NAME_KEY, data.getGoodsAttSetName());
                mContext.startActivity(intent);
            }
        });
    }

    public class GoodsAttributeSetHolder extends ZhiheAdapter.BaseViewHolder {
        @ViewInject(R.id.category_cover_fiv)
        public FixedImageView coverImg;
        @ViewInject((R.id.category_name_tv))
        public TextView nameTv;

        public GoodsAttributeSetHolder(View itemView) {
            super(itemView);
        }
    }
}
