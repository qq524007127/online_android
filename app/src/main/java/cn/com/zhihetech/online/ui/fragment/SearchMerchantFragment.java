package cn.com.zhihetech.online.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
import cn.com.zhihetech.online.model.ModelParams;

/**
 * Created by ShenYunjie on 2016/1/21.
 */
@ContentView(R.layout.activity_daily_new)
public class SearchMerchantFragment extends BaseFragment {

    @ViewInject(R.id.daily_new_srl)
    private ZhiheSwipeRefreshLayout refreshLayout;
    @ViewInject(R.id.merchant_list_lv)
    private LoadMoreListView merchantLv;
    @ViewInject(R.id.toolbar)
    private Toolbar toolbar;

    private PageData<Merchant> pageData;
    private MerchantAdapter adapter;
    private ModelParams params = new ModelParams();

    PageDataCallback<Merchant> searchCallback = new PageDataCallback<Merchant>() {
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

    private void initViews() {
        if (toolbar != null) {
            toolbar.setVisibility(View.GONE);
        }

        refreshLayout.setEnabled(false);

        adapter = new MerchantAdapter(getContext(), R.layout.content_merchant_list_item);
        merchantLv.setAdapter(adapter);

        merchantLv.setOnLoadMoreListener(new OnLoadMoreListener() {
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


    public void doSearch(String merchantName) {
        if (!refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(true);
        }
        params.addSearcher("merchName", merchantName);
        new MerchantModel().getMerchantsByModelParams(searchCallback, params.addPager(new Pager()));
    }

    private void loadMoreData() {
        new MerchantModel().getMerchantsByModelParams(loadMoreCallback, params.addPager(pageData.getNextPage()));
    }
}
