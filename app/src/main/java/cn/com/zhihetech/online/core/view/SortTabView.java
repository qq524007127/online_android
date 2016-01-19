package cn.com.zhihetech.online.core.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import cn.com.zhihetech.online.R;

/**
 * Created by ShenYunjie on 2016/1/19.
 */
public class SortTabView extends LinearLayout {

    private String title;
    private String sortProperty;

    public SortTabView(Context context) {
        this(context, null);
    }

    public SortTabView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SortTabView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SortTabView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        setGravity(Gravity.CENTER);
        TypedArray ta = getContext().obtainStyledAttributes(R.styleable.SortTabView);
        title = ta.getString(R.styleable.SortTabView_title);
        sortProperty = ta.getString(R.styleable.SortTabView_sort_property);
        ta.recycle();
        View tab = LayoutInflater.from(getContext()).inflate(R.layout.content_sort_tab, null);
        addView(tab);
    }
}
