package cn.com.zhihetech.online.core.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import cn.com.zhihetech.online.R;

/**
 * 宽度一定，可以动态设置高、宽比例的布局控件
 * Created by ShenYunjie on 2016/1/18.
 */
public class FixedWidthLayout extends FrameLayout {

    private float aspectRatio = 0;

    public FixedWidthLayout(Context context) {
        this(context, null);
    }

    public FixedWidthLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FixedWidthLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FixedWidthLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.FixedWidthLayout);
        aspectRatio = ta.getFloat(R.styleable.FixedWidthLayout_aspect_ratio, 0);
        ta.recycle();
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
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
