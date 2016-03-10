package cn.com.zhihetech.online.ui.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.FeaturedBlock;
import cn.com.zhihetech.online.core.adapter.FeaturedBlockAdapter;
import cn.com.zhihetech.online.core.common.PageData;
import cn.com.zhihetech.online.core.common.Pager;
import cn.com.zhihetech.online.core.http.PageDataCallback;
import cn.com.zhihetech.online.core.view.LoadMoreListView;
import cn.com.zhihetech.online.core.view.OnLoadMoreListener;
import cn.com.zhihetech.online.core.view.ZhiheSwipeRefreshLayout;
import cn.com.zhihetech.online.model.FeaturedBlockModel;

/**
 * Created by ShenYunjie on 2016/3/10.
 */
@ContentView(R.layout.activity_shopping_center_and_featured_block)
public class FeaturedBlocksActivity extends BaseActivity {

    @ViewInject(R.id.refresh_layout)
    private ZhiheSwipeRefreshLayout refreshLayout;
    @ViewInject(R.id.load_more_list_view)
    private LoadMoreListView loadMoreListView;

    private PageData<FeaturedBlock> pageData;
    private FeaturedBlockAdapter adapter;

    /**
     * 刷新（重载）数据回调
     */
    private PageDataCallback<FeaturedBlock> refreshCallback = new PageDataCallback<FeaturedBlock>() {
        @Override
        public void onPageData(PageData<FeaturedBlock> result, List<FeaturedBlock> rows) {
            pageData = result;
            adapter.refreshData(rows);
        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            showMsg(R.string.data_load_fial_please_try_agin);
        }

        @Override
        public void onFinished() {
            refreshLayout.setRefreshing(false);
        }
    };

    /**
     * 刷新（重载）数据回调
     */
    private PageDataCallback<FeaturedBlock> loadMoreCallback = new PageDataCallback<FeaturedBlock>() {
        @Override
        public void onPageData(PageData<FeaturedBlock> result, List<FeaturedBlock> rows) {
            pageData = result;
            adapter.addDatas(rows);
        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            showMsg(R.string.data_load_fial_please_try_agin);
        }

        @Override
        public void onFinished() {
            loadMoreListView.loadComplete();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViewAndData();
    }

    private void initViewAndData() {
        initViews();
        refreshData();
    }

    private void initViews() {
        adapter = new FeaturedBlockAdapter(this);
        loadMoreListView.setAdapter(adapter);
        loadMoreListView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public boolean checkCanDoLoad() {
                return pageData != null && pageData.hasNextPage() && !refreshLayout.isRefreshing();
            }

            @Override
            public void onStartLoad() {
                loadMoreData();
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
    }

    /**
     * 分页加载数据
     */
    private void loadMoreData() {
        new FeaturedBlockModel().getFeaturedBlocks(loadMoreCallback, pageData.getNextPage());
    }

    /**
     * 刷新（重载）数据
     */
    private void refreshData() {
        if (!refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(true);
        }
        new FeaturedBlockModel().getFeaturedBlocks(refreshCallback, new Pager());
    }
}
