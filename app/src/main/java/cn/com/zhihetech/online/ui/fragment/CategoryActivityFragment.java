package cn.com.zhihetech.online.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.Activity;
import cn.com.zhihetech.online.core.adapter.ActivityAdapter;
import cn.com.zhihetech.online.core.common.PageData;
import cn.com.zhihetech.online.core.common.Pager;
import cn.com.zhihetech.online.core.http.PageDataCallback;
import cn.com.zhihetech.online.core.view.LoadMoreListView;
import cn.com.zhihetech.online.core.view.OnLoadMoreListener;
import cn.com.zhihetech.online.core.view.ZhiheSwipeRefreshLayout;
import cn.com.zhihetech.online.model.ActivityModel;

/**
 * Created by ShenYunjie on 2016/1/19.
 */
@ContentView(R.layout.content_merchant_activity_fragment)
public class CategoryActivityFragment extends BaseFragment {
    @ViewInject(R.id.merchant_activity_zsrl)
    private ZhiheSwipeRefreshLayout refreshLayout;
    @ViewInject(R.id.merchant_activity_lmls)
    private LoadMoreListView listView;

    private String categorieId;
    private PageData<Activity> pageData;
    private ActivityAdapter adapter;

    PageDataCallback<Activity> refreshCallback = new PageDataCallback<Activity>() {
        @Override
        public void onPageData(PageData<Activity> result, List<Activity> rows) {
            pageData = result;
            adapter.refreshData(rows);
        }

        @Override
        public void onFinished() {
            refreshLayout.setRefreshing(false);
        }
    };

    PageDataCallback<Activity> loadMoreCallback = new PageDataCallback<Activity>() {
        @Override
        public void onPageData(PageData<Activity> result, List<Activity> rows) {
            pageData = result;
            adapter.addDatas(rows);
        }

        @Override
        public void onFinished() {
            listView.loadComplete();
        }
    };

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewAndData();
    }

    public CategoryActivityFragment getInstance(@NonNull String categorieId) {
        this.categorieId = categorieId;
        return this;
    }

    private void initViewAndData() {
        initViews();
        refreshData();
    }

    private void initViews() {
        adapter = new ActivityAdapter(getContext(), R.layout.content_activity_item);
        listView.setAdapter(adapter);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });

        listView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public boolean checkCanDoLoad() {
                return pageData != null && pageData.hasNextPage();
            }

            @Override
            public void onStartLoad() {
                loadMoreData();
            }
        });
    }

    private void loadMoreData() {
        new ActivityModel().getActivitiesByCategorieId(loadMoreCallback, pageData.getNextPage(), categorieId);
    }

    private void refreshData() {
        if (!refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(true);
        }
        new ActivityModel().getActivitiesByCategorieId(refreshCallback, new Pager(), categorieId);
    }
}
