package cn.com.zhihetech.online.core.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import cn.com.zhihetech.online.R;

/**
 * Created by ShenYunjie on 2016/1/15.
 */
public class LoadMoreListView extends ListView implements AbsListView.OnScrollListener {

    private View loadingView;
    private boolean isLoading = false;
    private boolean isLastItem = false;

    private float dx;
    private float dy;

    private OnLoadMoreListener onLoadMoreListener;

    public LoadMoreListView(Context context) {
        this(context, null);
    }

    public LoadMoreListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadMoreListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LoadMoreListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        addLoadingView();
        setOnScrollListener(this);
    }

    private void addLoadingView() {
        loadingView = LayoutInflater.from(getContext()).inflate(R.layout.content_loadmore_footer, null);
        loadingView.setVisibility(View.GONE);
        addFooterView(loadingView);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE) {
            if (onLoadMoreListener == null) {
                return;
            }
            if (isLoading || !isLastItem) {
                return;
            }
            if (onLoadMoreListener.checkCanDoLoad()) {
                loadingView.setVisibility(View.VISIBLE);
                isLoading = true;
                onLoadMoreListener.onStartLoad();
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (totalItemCount <= (getHeaderViewsCount() + getFooterViewsCount())) {
            return;
        }
        if ((firstVisibleItem + visibleItemCount) >= totalItemCount) {
            isLastItem = true;
        } else {
            isLastItem = false;
        }
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    /**
     * 加载完成后必须调用此接口，否则一直处于加载中
     */
    public void loadComplete() {
        loadingView.setVisibility(View.GONE);
        isLoading = false;
    }
}
