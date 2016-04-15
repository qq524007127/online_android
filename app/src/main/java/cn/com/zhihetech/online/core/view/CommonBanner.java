package cn.com.zhihetech.online.core.view;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;

import java.util.List;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.Banner;
import cn.com.zhihetech.online.core.common.Constant;
import cn.com.zhihetech.online.core.http.ArrayCallback;
import cn.com.zhihetech.online.core.util.ImageLoader;
import cn.com.zhihetech.online.model.BannerModel;
import cn.com.zhihetech.online.ui.activity.ActivityInfoActivity;
import cn.com.zhihetech.online.ui.activity.BaseActivity;
import cn.com.zhihetech.online.ui.activity.GoodsInfoActivity;
import cn.com.zhihetech.online.ui.activity.MerchantHomeActivity;
import cn.com.zhihetech.online.ui.activity.WebPageActivity;

/**
 * Created by ShenYunjie on 2016/3/10.
 */
public class CommonBanner extends ZhiheBanner<Banner> {

    protected int bannerType = 0;

    public CommonBanner(Context context, boolean canLoop) {
        super(context, canLoop);
    }

    public CommonBanner(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CommonBanner);
        int type = ta.getInt(R.styleable.CommonBanner_banner_type, 0);
        ta.recycle();
        if (type != 0) {
            bannerType = type;
            loadBannerData(bannerType);
        }
    }

    public void loadBannerData(int type) {
        new BannerModel().getBannersByType(new ArrayCallback<Banner>() {
            @Override
            public void onArray(List<Banner> banners) {
                setPages(new CBViewHolderCreator<CommonBannerHolder>() {
                    @Override
                    public CommonBannerHolder createHolder() {
                        return new CommonBannerHolder();
                    }
                }, banners);
            }
        }, type);
    }

    public class CommonBannerHolder implements Holder<Banner> {

        public ImageView bannerImg;
        private OnBannerClickListener onBannerClickListener;

        public OnBannerClickListener getOnBannerClickListener() {
            return onBannerClickListener;
        }

        @Override
        public View createView(Context context) {
            bannerImg = new ImageView(context);
            bannerImg.setScaleType(ImageView.ScaleType.FIT_XY);
            return bannerImg;
        }

        @Override
        public void UpdateUI(final Context context, final int position, final Banner data) {
            ImageLoader.disPlayImage(bannerImg, data.getImgInfo());
            bannerImg.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBannerClick(position, data);
                }
            });
        }

        private void onBannerClick(int position, Banner banner) {
            if (this.onBannerClickListener != null) {
                onBannerClickListener.onBannerClick(banner, position);
            } else {
                new DefaultBannerClickHandle().onBannerClick(banner, position);
            }
        }
    }

    public interface OnBannerClickListener {
        void onBannerClick(Banner banner, int position);
    }

    public class DefaultBannerClickHandle implements OnBannerClickListener {

        @Override
        public void onBannerClick(Banner banner, int position) {
            switch (banner.getViewType()) {
                case Constant.BANNER_VIEWTYPE_MERCHANT:
                    Intent merchIntent = new Intent(getContext(), MerchantHomeActivity.class);
                    merchIntent.putExtra(MerchantHomeActivity.MERCHANT_ID_KEY, banner.getViewTargert());
                    merchIntent.putExtra(BaseActivity.CUSTOM_TITLE_KEY, banner.getViewTargetTitle());
                    getContext().startActivity(merchIntent);
                    break;
                case Constant.BANNER_VIEWTYPE_GOODS:
                    Intent goodsIntent = new Intent(getContext(), GoodsInfoActivity.class);
                    goodsIntent.putExtra(GoodsInfoActivity.GOODS_ID_KEY, banner.getViewTargert());
                    goodsIntent.putExtra(BaseActivity.CUSTOM_TITLE_KEY, banner.getViewTargetTitle());
                    getContext().startActivity(goodsIntent);
                    break;
                case Constant.BANNER_VIEWTYPE_ACTIVITY:
                    Intent activityIntent = new Intent(getContext(), ActivityInfoActivity.class);
                    activityIntent.putExtra(ActivityInfoActivity.ACTIVITY_ID_KEY, banner.getViewTargert());
                    activityIntent.putExtra(BaseActivity.CUSTOM_TITLE_KEY, banner.getViewTargetTitle());
                    getContext().startActivity(activityIntent);
                    break;
                case Constant.BANNER_VIEWTYPE_WEB_PAGE:
                    Intent pageIntent = new Intent(getContext(), WebPageActivity.class);
                    pageIntent.putExtra(WebPageActivity.ENABLE_REFRESH, true);
                    pageIntent.putExtra(WebPageActivity.PAGE_URL, banner.getViewTargert());
                    pageIntent.putExtra(BaseActivity.CUSTOM_TITLE_KEY, banner.getViewTargetTitle());
                    getContext().startActivity(pageIntent);
                    break;
                default:
                    break;
            }
        }
    }
}
