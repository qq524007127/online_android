package cn.com.zhihetech.online.core.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

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
        setColorSchemeResources(R.color.colorPrimary, R.color.tabText, R.color.colorAccent);
    }
}
