package cn.com.zhihetech.online.core.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.com.zhihetech.online.R;

/**
 * Created by ShenYunjie on 2016/1/19.
 */
public class SortTabView extends LinearLayout {

    private String title;
    private String sortProperty;
    private OrderType _order = null;

    private TextView titleView;
    private ImageView ascView;
    private ImageView descView;

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
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.SortTabView);
        title = ta.getString(R.styleable.SortTabView_sort_title);
        sortProperty = ta.getString(R.styleable.SortTabView_sort_property);
        ta.recycle();
        initViews();
    }

    private void initViews() {
        View tab = LayoutInflater.from(getContext()).inflate(R.layout.content_sort_tab, null);
        titleView = (TextView) tab.findViewById(R.id.sort_tab_title_tv);
        ascView = (ImageView) tab.findViewById(R.id.sort_asc_iv);
        descView = (ImageView) tab.findViewById(R.id.sort_desc_iv);
        titleView.setText(title);
        addView(tab);
        //tab.setOnClickListener(this);
    }

    public void settingSortType(OrderType orderType) {
        resetState(false);
        _order = orderType;
        switch (_order) {
            case asc:
                ascView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.press_asc));
                break;
            case desc:
                descView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.press_desc));
                break;
            default:
                break;
        }
    }

    /**
     * 重置排序图标状态，且根据所传参数判断是否将排序类型设置为null
     *
     * @param isResetOrder 是否将排序值设置为null
     */
    public void resetState(boolean isResetOrder) {
        ascView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.normal_asc));
        descView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.normal_desc));
        if (isResetOrder) {
            _order = null;
        }
    }

    /**
     * 获取排序类型
     *
     * @return
     */
    public OrderType getOrderType() {
        return this._order;
    }

    /**
     * 获取排序你属性名称
     *
     * @return
     */
    public String getSortProperty() {
        return this.sortProperty;
    }

    /**
     * 排序方式；asc:升序；desc:降序
     */
    public enum OrderType {
        asc, desc
    }
}
