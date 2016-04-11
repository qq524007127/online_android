package cn.com.zhihetech.online.core.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easemob.easeui.widget.EaseImageView;

import org.xutils.view.annotation.ViewInject;

import java.util.List;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.Goods;
import cn.com.zhihetech.online.bean.Merchant;
import cn.com.zhihetech.online.core.util.ImageLoader;
import cn.com.zhihetech.online.ui.activity.MerchantHomeActivity;

/**
 * Created by ShenYunjie on 2016/1/18.
 */
public class MerchantAdapter extends ZhiheAdapter<Merchant, MerchantAdapter.MerchantHolder> {

    public MerchantAdapter(Context mContext, int layoutId) {
        super(mContext, layoutId);
    }

    public MerchantAdapter(Context mContext, int layoutId, List<Merchant> mDatas) {
        super(mContext, layoutId, mDatas);
    }

    @Override
    public MerchantHolder onCreateViewHolder(View itemView) {
        return new MerchantHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MerchantHolder holder, final Merchant data) {
        ImageLoader.disPlayImage(holder.merchHeader, data.getCoverImg());
        holder.merchantNameTv.setText(data.getMerchName());
        holder.merchGoodsCount.setText("共 " + data.getGoodsNum() + " 件宝贝");
        holder.activityFlag.setVisibility(data.getIsActivating() ? View.VISIBLE : View.GONE);
        data.getRecommendGoodses();
        initRecommendGoods(data.getRecommendGoodses(), holder);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MerchantHomeActivity.class);
                intent.putExtra(MerchantHomeActivity.MERCHANT_ID_KEY, data.getMerchantId());
                intent.putExtra(MerchantHomeActivity.MERCHANT_NAME_KEY, data.getMerchName());
                mContext.startActivity(intent);
            }
        });
    }

    private void initRecommendGoods(List<Goods> recommendGoodses, MerchantHolder holder) {
        holder.recommendGoodGrid.setAdapter(new RecommendGoodsAdapter(mContext, R.layout.content_recommend_goods_item, recommendGoodses));
    }

    public class MerchantHolder extends ZhiheAdapter.BaseViewHolder {
        @ViewInject(R.id.merch_header_iv)
        public EaseImageView merchHeader;
        @ViewInject(R.id.merchan_info_ll)
        public LinearLayout merchInfoLayout;
        @ViewInject(R.id.merch_activity_flag_iv)
        public ImageView activityFlag;
        @ViewInject(R.id.merch_name_tv)
        public TextView merchantNameTv;
        @ViewInject(R.id.merch_goods_count_tv)
        public TextView merchGoodsCount;

        @ViewInject(R.id.recommend_goods_gv)
        public GridView recommendGoodGrid;

        public MerchantHolder(View itemView) {
            super(itemView);
        }
    }
}
