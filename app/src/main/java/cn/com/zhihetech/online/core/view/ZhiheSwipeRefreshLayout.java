package cn.com.zhihetech.online.core.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

import org.xutils.common.util.DensityUtil;

import cn.com.zhihetech.online.R;

/**
 * Created by ShenYunjie on 2016/1/15.
 */
public class ZhiheSwipeRefreshLayout extends SwipeRefreshLayout {
    public ZhiheSwipeRefreshLayout(Context context) {
        this(context, null);
    }

    public ZhiheSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light,
                R.color.holo_orange_light, R.color.holo_red_light);
        setProgressViewOffset(false, 0, DensityUtil.dip2px(80));
    }
}
