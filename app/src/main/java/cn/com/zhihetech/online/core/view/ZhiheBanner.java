package cn.com.zhihetech.online.core.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.bigkoo.convenientbanner.ConvenientBanner;

import cn.com.zhihetech.online.R;

/**
 * Created by ShenYunjie on 2016/1/18.
 */
public class ZhiheBanner<T> extends ConvenientBanner<T> {

    private float dx;
    private float dy;

    public ZhiheBanner(Context context, boolean canLoop) {
        super(context, canLoop);
        zhiheInit();
    }

    public ZhiheBanner(Context context, AttributeSet attrs) {
        super(context, attrs);
        zhiheInit();
    }

    private void zhiheInit() {
        setPageIndicator(new int[]{R.drawable.banner_indicator_normal, R.drawable.banner_indicator_activity})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
                .startTurning(1000 * 3);
    }


}
