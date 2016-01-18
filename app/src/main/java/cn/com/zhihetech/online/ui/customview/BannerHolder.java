package cn.com.zhihetech.online.ui.customview;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;

import cn.com.zhihetech.online.bean.Banner;
import cn.com.zhihetech.online.core.util.ImageLoader;

/**
 * Created by ShenYunjie on 2016/1/18.
 */
public class BannerHolder implements Holder<Banner> {

    private ImageView bannerImg;

    @Override
    public View createView(Context context) {
        bannerImg = new ImageView(context);
        bannerImg.setScaleType(ImageView.ScaleType.FIT_XY);
        return bannerImg;
    }

    @Override
    public void UpdateUI(Context context, int position, Banner data) {
        ImageLoader.disPlayImage(bannerImg, data.getImgInfo());
    }
}
