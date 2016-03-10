package cn.com.zhihetech.online.core.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import org.xutils.view.annotation.ViewInject;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.FeaturedBlock;
import cn.com.zhihetech.online.core.util.ImageLoader;
import cn.com.zhihetech.online.ui.activity.BaseActivity;
import cn.com.zhihetech.online.ui.activity.MerchantListActivity;

/**
 * Created by ShenYunjie on 2016/3/10.
 */
public class FeaturedBlockAdapter extends ZhiheAdapter<FeaturedBlock, FeaturedBlockAdapter.FeaturedBlockHolder> {

    public FeaturedBlockAdapter(Context mContext) {
        super(mContext, R.layout.content_shopping_center_and_featured_block_item);
    }

    @Override
    public FeaturedBlockHolder onCreateViewHolder(View itemView) {
        return new FeaturedBlockHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FeaturedBlockHolder holder, final FeaturedBlock data) {
        ImageLoader.disPlayImage(holder.coverImg, data.getCoverImg());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MerchantListActivity.class);
                intent.putExtra(BaseActivity.CUSTOM_TITLE_KEY, data.getFbName());
                intent.putExtra(MerchantListActivity.MERCHANT_TYPE_KEY, MerchantListActivity.FEATURED_BLOCK_SHOP_TYPE);
                intent.putExtra(MerchantListActivity.TYPE_ID_KEY, data.getFbId());
                mContext.startActivity(intent);
            }
        });
    }

    public class FeaturedBlockHolder extends ZhiheAdapter.BaseViewHolder {

        @ViewInject(R.id.cover_content_img)
        public ImageView coverImg;

        public FeaturedBlockHolder(View itemView) {
            super(itemView);
        }
    }
}
