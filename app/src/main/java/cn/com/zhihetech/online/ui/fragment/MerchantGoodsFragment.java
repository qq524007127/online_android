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
import cn.com.zhihetech.online.bean.Goods;
import cn.com.zhihetech.online.core.common.PageData;
import cn.com.zhihetech.online.core.common.Pager;
import cn.com.zhihetech.online.core.http.PageDataCallback;
import cn.com.zhihetech.online.core.view.LoadMoreGridView;
import cn.com.zhihetech.online.core.view.OnLoadMoreListener;
import cn.com.zhihetech.online.core.view.ZhiheSwipeRefreshLayout;
import cn.com.zhihetech.online.core.view.adapter.GoodsAdapter;
import cn.com.zhihetech.online.model.GoodsModel;

/**
 * Created by ShenYunjie on 2016/1/19.
 */
@ContentView(R.layout.content_merchant_goods_fragment)
public class MerchantGoodsFragment extends BaseFragment {
    @ViewInject(R.id.merchant_goods_zsrl)
    private ZhiheSwipeRefreshLayout refreshLayout;
    @ViewInject(R.id.merchant_goods_lmgv)
    private LoadMoreGridView gridView;

    private String merchantId;
    private PageData<Goods> pageData;
    private GoodsAdapter adapter;

    private PageDataCallback<Goods> refreshCallback = new PageDataCallback<Goods>() {

        @Override
        public void onPageData(PageData result, List rows) {
            pageData = result;
            adapter.refreshData(rows);
        }

        @Override
        public void onFinished() {
            refreshLayout.setRefreshing(false);
        }
    };

    private PageDataCallback<Goods> loadMoreCallback = new PageDataCallback<Goods>() {
        @Override
        public void onPageData(PageData<Goods> result, List<Goods> rows) {
            pageData = result;
            adapter.addDatas(rows);
        }

        @Override
        public void onFinished() {
            gridView.loadComplete();
        }
    };

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewAndData();
    }

    public MerchantGoodsFragment getInstance(@NonNull String merchantId) {
        this.merchantId = merchantId;
        return this;
    }

    private void initViewAndData() {
        initViews();
        refreshData();
    }

    private void initViews() {
        adapter = new GoodsAdapter(getContext(), R.layout.content_goods_item);
        gridView.setAdapter(adapter);
        gridView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public boolean checkCanDoLoad() {
                return pageData != null && pageData.hasNextPage();
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

    private void refreshData() {
        if (!refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(true);
        }
        new GoodsModel().getGoodsesByMerchantId(refreshCallback, new Pager(), merchantId);
    }

    private void loadMoreData() {
        new GoodsModel().getGoodsesByMerchantId(loadMoreCallback, pageData.getNextPage(), merchantId);
    }
}
