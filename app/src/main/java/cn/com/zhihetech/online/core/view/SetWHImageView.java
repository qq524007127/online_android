package cn.com.zhihetech.online.core.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 可自定义宽高的ImageView
 * Created by ShenYunjie on 2016/1/20.
 */
public class SetWHImageView extends ImageView {
    private int resWidth;
    private int resHeight;

    public SetWHImageView(Context context) {
        super(context);
    }

    public SetWHImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SetWHImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SetWHImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setResSize(int width, int height) {
        if (width <= 0 || height <= 0) {
            throw new RuntimeException("Image width or height is not able zero or negative");
        }
        this.resWidth = width;
        this.resHeight = height;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (this.resWidth <= 0 || this.resHeight <= 0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = (int) (((float) widthSize * (float) resHeight) / (float) resWidth);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
