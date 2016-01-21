package cn.com.zhihetech.online.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.Merchant;
import cn.com.zhihetech.online.core.adapter.MerchantAdapter;
import cn.com.zhihetech.online.core.common.PageData;
import cn.com.zhihetech.online.core.common.Pager;
import cn.com.zhihetech.online.core.http.PageDataCallback;
import cn.com.zhihetech.online.core.view.LoadMoreListView;
import cn.com.zhihetech.online.core.view.OnLoadMoreListener;
import cn.com.zhihetech.online.core.view.ZhiheSwipeRefreshLayout;
import cn.com.zhihetech.online.model.MerchantModel;

/**
 * Created by ShenYunjie on 2016/1/21.
 */
@ContentView(R.layout.activity_daily_new)
public class CategoryMerchantFragment extends BaseFragment {

    @ViewInject(R.id.daily_new_srl)
    private ZhiheSwipeRefreshLayout refreshLayout;
    @ViewInject(R.id.merchant_list_lv)
    private LoadMoreListView merchantLv;
    @ViewInject(R.id.toolbar)
    private Toolbar toolbar;

    private String categoryId;
    private PageData<Merchant> pageData;
    private MerchantAdapter adapter;

    PageDataCallback<Merchant> refreshCallback = new PageDataCallback<Merchant>() {
        @Override
        public void onPageData(PageData<Merchant> result, List<Merchant> rows) {
            pageData = result;
            adapter.refreshData(rows);
        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            super.onError(ex, isOnCallback);
            onHttpError(ex, isOnCallback);
        }

        @Override
        public void onFinished() {
            refreshLayout.setRefreshing(false);
        }
    };

    PageDataCallback<Merchant> loadMoreCallback = new PageDataCallback<Merchant>() {
        @Override
        public void onPageData(PageData<Merchant> result, List<Merchant> rows) {
            pageData = result;
            adapter.addDatas(rows);
        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            super.onError(ex, isOnCallback);
            onHttpError(ex, isOnCallback);
        }

        @Override
        public void onFinished() {
            merchantLv.loadComplete();
        }
    };

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
    }

    public CategoryMerchantFragment getInstance(String categoryId) {
        this.categoryId = categoryId;
        return this;
    }

    private void initViews() {
        if (toolbar != null) {
            toolbar.setVisibility(View.GONE);
        }
        adapter = new MerchantAdapter(getContext(), R.layout.content_merchant_list_item);
        merchantLv.setAdapter(adapter);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });

        merchantLv.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public boolean checkCanDoLoad() {
                return pageData != null && pageData.hasNextPage();
            }

            @Override
            public void onStartLoad() {
                loadMoreData();
            }
        });
        refreshData();
    }


    private void refreshData() {
        if (!refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(true);
        }
        new MerchantModel().getMerchantsByCategory(refreshCallback, new Pager(), categoryId);
    }

    private void loadMoreData() {
        new MerchantModel().getMerchantsByCategory(loadMoreCallback, pageData.getNextPage(), categoryId);
    }
}
