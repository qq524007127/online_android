package cn.com.zhihetech.online.core.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;

import java.util.List;

import cn.com.zhihetech.online.R;

/**
 * Created by ShenYunjie on 2016/1/18.
 */
public class ZhiheBanner<T> extends ConvenientBanner<T> {

    private float mDownX, mDownY;

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
                .setPageIndicatorAlign(PageIndicatorAlign.CENTER_HORIZONTAL);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = ev.getX();
                mDownY = ev.getY();
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(ev.getX() - mDownX) > Math.abs(ev.getY() - mDownY)) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                } else {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public ConvenientBanner setPages(CBViewHolderCreator holderCreator, List<T> datas) {
        if (datas.size() <= 1) {
            stopTurning();
        } else {
            startTurning(1000 * 3);
        }
        return super.setPages(holderCreator, datas);
    }
}
