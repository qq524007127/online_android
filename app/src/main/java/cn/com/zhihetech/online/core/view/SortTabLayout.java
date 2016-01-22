package cn.com.zhihetech.online.core.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by ShenYunjie on 2016/1/20.
 */
public class SortTabLayout extends LinearLayout implements View.OnClickListener {

    private OnSortTabChangListener onSortTabChangListener;

    SortTabView[] tabViews;

    public SortTabLayout(Context context) {
        this(context, null);
    }

    public SortTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SortTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SortTabLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        init();
    }

    private void init() {
        if (getChildCount() < 1) {
            return;
        }
        this.tabViews = new SortTabView[getChildCount()];
        for (int i = 0; i < getChildCount(); i++) {
            View sortTab = getChildAt(i);
            if (!(sortTab instanceof SortTabView)) {
                throw new RuntimeException("SortTabLayout childView is must SortTabView");
            }
            tabViews[i] = (SortTabView) sortTab;
            sortTab.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        for (int i = 0; i < tabViews.length; i++) {
            SortTabView tabView = tabViews[i];
            if (v == tabView) {
                settingCurrentSortTab(tabView);
                if (onSortTabChangListener != null) {
                    onSortTabChangListener.onSortTabChange(tabView, i);
                }
                continue;
            }
            tabView.resetState(true);
        }
    }

    public void reset() {
        for (SortTabView tab : this.tabViews){
            tab.resetState(true);
        }
    }

    public void setOnSortTabChangListener(OnSortTabChangListener onSortTabChangListener) {
        this.onSortTabChangListener = onSortTabChangListener;
    }

    private void settingCurrentSortTab(SortTabView tabView) {
        SortTabView.OrderType orderType = tabView.getOrderType();
        orderType = orderType == null ? SortTabView.OrderType.asc : (orderType == SortTabView.OrderType.asc ? SortTabView.OrderType.desc : SortTabView.OrderType.asc);
        tabView.settingSortType(orderType);
    }

    public interface OnSortTabChangListener {
        /**
         * 排序状态改变接口
         *
         * @param sortTabView 当前改变的排序控件
         * @param position    在当前排序中的位置索引，从0开始
         */
        void onSortTabChange(SortTabView sortTabView, int position);
    }
}
