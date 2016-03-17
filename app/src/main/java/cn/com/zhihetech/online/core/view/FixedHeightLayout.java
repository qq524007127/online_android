package cn.com.zhihetech.online.core.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import cn.com.zhihetech.online.R;

/**
 * 高度固定，可以动态设置宽、高比例的布局控件
 * Created by ShenYunjie on 2016/1/18.
 */
public class FixedHeightLayout extends FrameLayout {

    private float aspectRatio = 0;

    public FixedHeightLayout(Context context) {
        this(context, null);
    }

    public FixedHeightLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FixedHeightLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FixedHeightLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.FixedHeightLayout);
        aspectRatio = ta.getFloat(R.styleable.FixedHeightLayout_fixed_height_aspect_ratio, 0);
        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (aspectRatio <= 0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (heightMode == MeasureSpec.UNSPECIFIED) {
            throw new RuntimeException("FixedLayout width is not able UNSPECIFIED");
        }
        int widthSize = (int) ((float) heightSize * aspectRatio);
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
