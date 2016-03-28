package cn.com.zhihetech.online.core.view;

import android.content.Context;
import android.util.AttributeSet;

import com.bigkoo.convenientbanner.ConvenientBanner;

import cn.com.zhihetech.online.R;

/**
 * Created by ShenYunjie on 2016/1/18.
 */
public class ZhiheBanner<T> extends ConvenientBanner<T> {

    public ZhiheBanner(Context context, boolean canLoop) {
        super(context, canLoop);
        initBanner();
    }

    public ZhiheBanner(Context context, AttributeSet attrs) {
        super(context, attrs);
        initBanner();
    }

    protected void initBanner() {
        setPageIndicator(new int[]{R.drawable.banner_indicator_normal, R.drawable.banner_indicator_activity})
                .setPageIndicatorAlign(PageIndicatorAlign.CENTER_HORIZONTAL)
                .startTurning(1000 * 3);
    }
}
