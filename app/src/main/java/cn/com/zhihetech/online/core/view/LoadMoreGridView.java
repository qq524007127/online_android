package cn.com.zhihetech.online.core.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;

import cn.com.zhihetech.online.R;
import in.srain.cube.views.GridViewWithHeaderAndFooter;

/**
 * Created by ShenYunjie on 2016/1/15.
 */
public class LoadMoreGridView extends GridViewWithHeaderAndFooter implements AbsListView.OnScrollListener {
    private View loadingView;
    private boolean isLoading = false;
    private boolean isLastItem = false;

    private OnLoadMoreListener onLoadMoreListener;

    public LoadMoreGridView(Context context) {
        this(context, null);
    }

    public LoadMoreGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadMoreGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
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

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (totalItemCount <= (getHeaderViewCount() + getFooterViewCount())) {
            return;
        }
        if ((firstVisibleItem + visibleItemCount) >= totalItemCount) {
            isLastItem = true;
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
