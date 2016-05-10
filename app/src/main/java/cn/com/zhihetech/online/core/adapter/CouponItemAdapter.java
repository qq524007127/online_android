package cn.com.zhihetech.online.core.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.easeui.widget.EaseImageView;

import org.xutils.view.annotation.ViewInject;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.Coupon;
import cn.com.zhihetech.online.bean.CouponItem;
import cn.com.zhihetech.online.core.common.Constant;
import cn.com.zhihetech.online.core.util.DateUtils;
import cn.com.zhihetech.online.core.util.ImageLoader;
import cn.com.zhihetech.online.ui.activity.CouponItemInfoActivity;

/**
 * Created by ShenYunjie on 2016/3/17.
 */
public class CouponItemAdapter extends ZhiheAdapter<CouponItem, CouponItemAdapter.CouponItemHolder> {

    public CouponItemAdapter(Context mContext) {
        super(mContext, R.layout.item_my_coupon);
    }

    @Override
    public CouponItemHolder onCreateViewHolder(View itemView) {
        return new CouponItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CouponItemHolder holder, final CouponItem data) {
        Coupon coupon = data.getCoupon();
        ImageLoader.disPlayImage(holder.merchantCoverImg, coupon.getMerchant().getCoverImg());
        holder.merchantNameTv.setText(coupon.getMerchant().getMerchName());
        holder.couponFaceValueTv.setText(coupon.getCouponType() == Constant.COUPON_DISCOUNT_TYPE ?
                coupon.getFaceValue() + "折" : "￥" + coupon.getFaceValue());
        holder.couponTypeTv.setText(coupon.getCouponType() == Constant.COUPON_DISCOUNT_TYPE ? "打折券" : "代金券");
        holder.couponDetaiTv.setText(data.getCoupon().getCouponMsg());
        holder.couponValidityDateTv.setText("有效期:" + DateUtils.formatDate(coupon.getStartValidity())
                + "至" + DateUtils.formatDate(coupon.getEndValidity()));
        holder.couponStateTv.setText(data.isUseState() ? "已使用" : "未使用");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CouponItemInfoActivity.class);
                intent.putExtra(CouponItemInfoActivity.COUPON_ITEM_ID_KEY, data.getCouponItemId());
                mContext.startActivity(intent);
            }
        });
    }

    public class CouponItemHolder extends ZhiheAdapter.BaseViewHolder {

        @ViewInject(R.id.coupon_mernchant_cover_iv)
        public EaseImageView merchantCoverImg;
        @ViewInject(R.id.coupon_mernchant_name_tv)
        public TextView merchantNameTv;
        @ViewInject(R.id.coupon_face_value_tv)
        public TextView couponFaceValueTv;
        @ViewInject(R.id.coupon_type_tv)
        public TextView couponTypeTv;
        @ViewInject(R.id.coupon_detail_info_tv)
        public TextView couponDetaiTv;
        @ViewInject(R.id.coupon_validity_period_tv)
        public TextView couponValidityDateTv;
        @ViewInject(R.id.coupon_state_tv)
        public TextView couponStateTv;

        public CouponItemHolder(View itemView) {
            super(itemView);
        }
    }
}
