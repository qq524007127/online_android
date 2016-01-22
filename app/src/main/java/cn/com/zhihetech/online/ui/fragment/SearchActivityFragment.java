package cn.com.zhihetech.online.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.Activity;
import cn.com.zhihetech.online.core.common.PageData;
import cn.com.zhihetech.online.core.common.Pager;
import cn.com.zhihetech.online.core.http.PageDataCallback;
import cn.com.zhihetech.online.core.view.LoadMoreListView;
import cn.com.zhihetech.online.core.view.OnLoadMoreListener;
import cn.com.zhihetech.online.core.view.ZhiheSwipeRefreshLayout;
import cn.com.zhihetech.online.core.adapter.ActivityAdapter;
import cn.com.zhihetech.online.model.ActivityModel;
import cn.com.zhihetech.online.model.ModelParams;

/**
 * Created by ShenYunjie on 2016/1/19.
 */
@ContentView(R.layout.content_merchant_activity_fragment)
public class SearchActivityFragment extends BaseFragment {
    @ViewInject(R.id.merchant_activity_zsrl)
    private ZhiheSwipeRefreshLayout refreshLayout;
    @ViewInject(R.id.merchant_activity_lmls)
    private LoadMoreListView listView;

    private PageData<Activity> pageData;
    private ActivityAdapter adapter;

    private ModelParams params = new ModelParams();

    PageDataCallback<Activity> searchCallback = new PageDataCallback<Activity>() {
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

    private void initViewAndData() {
        initViews();
    }

    private void initViews() {
        refreshLayout.setEnabled(false);
        adapter = new ActivityAdapter(getContext(), R.layout.content_activity_item);
        listView.setAdapter(adapter);

        listView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public boolean checkCanDoLoad() {
                return pageData != null && pageData.hasNextPage() && !refreshLayout.isRefreshing();
            }

            @Override
            public void onStartLoad() {
                loadMoreData();
            }
        });
    }

    private void loadMoreData() {
        new ActivityModel().getActivitiesByModelParams(loadMoreCallback, params.addPager(pageData.getNextPage()));
    }

    public void doSearch(String searchValue) {
        refreshLayout.setRefreshing(true);
        params.addPager(new Pager()).addSearcher("attributeSet.goodsAttSetName", searchValue);
        new ActivityModel().getActivitiesByModelParams(searchCallback, params);
    }
}
