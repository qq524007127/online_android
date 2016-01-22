package cn.com.zhihetech.online.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.Goods;
import cn.com.zhihetech.online.core.adapter.GoodsAdapter;
import cn.com.zhihetech.online.core.common.PageData;
import cn.com.zhihetech.online.core.common.Pager;
import cn.com.zhihetech.online.core.http.PageDataCallback;
import cn.com.zhihetech.online.core.view.LoadMoreGridView;
import cn.com.zhihetech.online.core.view.OnLoadMoreListener;
import cn.com.zhihetech.online.core.view.SortTabLayout;
import cn.com.zhihetech.online.core.view.SortTabView;
import cn.com.zhihetech.online.core.view.ZhiheSwipeRefreshLayout;
import cn.com.zhihetech.online.model.GoodsModel;
import cn.com.zhihetech.online.model.ModelParams;

/**
 * Created by ShenYunjie on 2016/1/19.
 */
@ContentView(R.layout.content_merchant_goods_fragment)
public class SearchGoodsFragment extends BaseFragment {
    @ViewInject(R.id.merchant_goods_zsrl)
    private ZhiheSwipeRefreshLayout refreshLayout;
    @ViewInject(R.id.merchant_goods_lmgv)
    private LoadMoreGridView gridView;
    @ViewInject(R.id.merchant_goods_stl)
    private SortTabLayout sortTabLayout;

    private PageData<Goods> pageData;
    private GoodsAdapter adapter;
    private ModelParams params = new ModelParams();

    private String searchValue = null;

    private PageDataCallback<Goods> searchCallback = new PageDataCallback<Goods>() {

        @Override
        public void onPageData(PageData result, List rows) {
            pageData = result;
            adapter.refreshData(rows);
            gridView.smoothScrollToPosition(0);
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

    private void initViewAndData() {
        initViews();
    }

    private void initViews() {
        refreshLayout.setEnabled(false);
        adapter = new GoodsAdapter(getContext(), R.layout.content_goods_item);
        gridView.setAdapter(adapter);
        gridView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public boolean checkCanDoLoad() {
                return pageData != null && pageData.hasNextPage() && !refreshLayout.isRefreshing();
            }

            @Override
            public void onStartLoad() {
                loadMoreData();
            }
        });
        sortTabLayout.setOnSortTabChangListener(new SortTabLayout.OnSortTabChangListener() {
            @Override
            public void onSortTabChange(SortTabView sortTabView, int position) {
                if (!refreshLayout.isRefreshing() && searchValue != null) {
                    params.addSort(sortTabView.getSortProperty(), sortTabView.getOrderType());
                    reLoadData();
                }
            }
        });
    }

    public void doSearch(String searchValue) {
        this.searchValue = searchValue;

        sortTabLayout.reset();
        params.clearSort();

        params.addSearcher("goodsAttributeSet.goodsAttSetName", this.searchValue);
        reLoadData();
    }

    /**
     * 重载数据
     */
    private void reLoadData() {
        if (!refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(true);
        }
        new GoodsModel().getGoodsesByModelParams(searchCallback, params.addPager(new Pager()));
    }

    /**
     * 加载更多，（分页加载）
     */
    private void loadMoreData() {
        new GoodsModel().getGoodsesByModelParams(loadMoreCallback, params.addPager(pageData.getNextPage()));
    }
}
