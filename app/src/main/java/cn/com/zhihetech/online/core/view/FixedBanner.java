package cn.com.zhihetech.online.core.view;

import android.content.Context;
import android.util.AttributeSet;


/**
 * Created by ShenYunjie on 2016/1/18.
 */
public class FixedBanner<T> extends ZhiheBanner<T> {
    private float aspectRatio = 0;

    public FixedBanner(Context context, boolean canLoop) {
        super(context, canLoop);
    }

    public FixedBanner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public float getAspectRatio() {
        return aspectRatio;
    }

    public void setAspectRatio(float aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (aspectRatio <= 0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        if (widthMode == MeasureSpec.UNSPECIFIED) {
            throw new RuntimeException("FixedLayout width is not able UNSPECIFIED");
        }
        int heightSize = (int) ((float) widthSize * aspectRatio);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
