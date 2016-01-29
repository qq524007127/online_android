package cn.com.zhihetech.online.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.Activity;
import cn.com.zhihetech.online.core.common.PageData;
import cn.com.zhihetech.online.core.common.Pager;
import cn.com.zhihetech.online.core.http.PageDataCallback;
import cn.com.zhihetech.online.core.view.HomeHeaderView;
import cn.com.zhihetech.online.core.view.LoadMoreListView;
import cn.com.zhihetech.online.core.view.OnLoadMoreListener;
import cn.com.zhihetech.online.core.view.ZhiheSwipeRefreshLayout;
import cn.com.zhihetech.online.core.adapter.ActivityAdapter;
import cn.com.zhihetech.online.model.ActivityModel;
import cn.com.zhihetech.online.ui.activity.SearchActivity;

/**
 * Created by ShenYunjie on 2016/1/15.
 */
@ContentView(R.layout.content_home_fragment)
public class HomeFragment extends BaseFragment {
    @ViewInject(R.id.home_srl)
    private ZhiheSwipeRefreshLayout swipeRefreshLayout;
    @ViewInject(R.id.home_activity_lv)
    private LoadMoreListView activityLV;

    private ActivityAdapter adapter;
    private PageData<Activity> pageData;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
        refreshData();
    }

    private void initViews() {
        HomeHeaderView headerView = new HomeHeaderView(getContext());
        activityLV.addHeaderView(headerView);
        adapter = new ActivityAdapter(getContext(), R.layout.content_activity_item);
        activityLV.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
        activityLV.setOnLoadMoreListener(new OnLoadMoreListener() {
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
        new ActivityModel().getActivities(new PageDataCallback<Activity>() {
            @Override
            public void onPageData(PageData<Activity> result, List<Activity> rows) {
                pageData = result;
                adapter.addDatas(rows);
            }

            @Override
            public void onFinished() {
                activityLV.loadComplete();
            }
        }, pageData.getNextPage());
    }

    private void refreshData() {
        if (!swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(true);
        }
        new ActivityModel().getActivities(new PageDataCallback<Activity>() {
            @Override
            public void onPageData(PageData<Activity> result, List<Activity> rows) {
                adapter.refreshData(rows);
                pageData = result;
            }

            @Override
            public void onFinished() {
                swipeRefreshLayout.setRefreshing(false);
            }
        }, new Pager(5));
    }

    @Event({R.id.home_search_btn})
    private void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.home_search_btn:
                Intent intent = new Intent(getContext(), SearchActivity.class);
                getContext().startActivity(intent);
                break;
        }
    }
}
