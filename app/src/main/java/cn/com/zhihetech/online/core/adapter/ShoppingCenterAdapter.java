package cn.com.zhihetech.online.core.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import org.xutils.view.annotation.ViewInject;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.ShoppingCenter;
import cn.com.zhihetech.online.core.util.ImageLoader;
import cn.com.zhihetech.online.ui.activity.BaseActivity;
import cn.com.zhihetech.online.ui.activity.MerchantListActivity;

/**
 * Created by ShenYunjie on 2016/3/10.
 */
public class ShoppingCenterAdapter extends ZhiheAdapter<ShoppingCenter, ShoppingCenterAdapter.ShoppingCenterHolder> {

    public ShoppingCenterAdapter(Context mContext) {
        super(mContext, R.layout.content_shopping_center_and_featured_block_item);
    }

    @Override
    public ShoppingCenterHolder onCreateViewHolder(View itemView) {
        return new ShoppingCenterHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ShoppingCenterHolder holder, final ShoppingCenter data) {
        ImageLoader.disPlayImage(holder.coverImg, data.getCoverImg());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MerchantListActivity.class);
                intent.putExtra(BaseActivity.CUSTOM_TITLE_KEY, data.getScName());
                intent.putExtra(MerchantListActivity.MERCHANT_TYPE_KEY, MerchantListActivity.SHOPPING_CENTER_SHOP_TYPE);
                intent.putExtra(MerchantListActivity.TYPE_ID_KEY, data.getScId());
                mContext.startActivity(intent);
            }
        });
    }

    public class ShoppingCenterHolder extends ZhiheAdapter.BaseViewHolder {

        @ViewInject(R.id.cover_content_img)
        public ImageView coverImg;

        public ShoppingCenterHolder(View itemView) {
            super(itemView);
        }
    }
}
