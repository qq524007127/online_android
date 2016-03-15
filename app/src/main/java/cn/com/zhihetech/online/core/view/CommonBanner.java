package cn.com.zhihetech.online.core.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;

import java.util.List;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.Banner;
import cn.com.zhihetech.online.core.http.ArrayCallback;
import cn.com.zhihetech.online.core.util.ImageLoader;
import cn.com.zhihetech.online.model.BannerModel;

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
        new BannerModel().getBanners(new ArrayCallback<Banner>() {
            @Override
            public void onArray(List<Banner> banners) {
                setPages(new CBViewHolderCreator<CommonBannerHolder>() {
                    @Override
                    public CommonBannerHolder createHolder() {
                        return new CommonBannerHolder();
                    }
                }, banners);
            }
        });
    }

    public class CommonBannerHolder implements Holder<Banner> {

        public ImageView bannerImg;

        @Override
        public View createView(Context context) {
            bannerImg = new ImageView(context);
            bannerImg.setScaleType(ImageView.ScaleType.FIT_XY);
            return bannerImg;
        }

        @Override
        public void UpdateUI(final Context context, int position, final Banner data) {
            ImageLoader.disPlayImage(bannerImg, data.getImgInfo());
            bannerImg.setOnClickListener(new BannerClickListener(data));
        }
    }

    public class BannerClickListener implements OnClickListener {

        private Banner banner;

        public BannerClickListener(Banner banner) {
            this.banner = banner;
        }

        @Override
        public void onClick(View v) {

        }
    }
}