package cn.com.zhihetech.online.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.ShoppingCart;
import cn.com.zhihetech.online.core.common.PageData;
import cn.com.zhihetech.online.core.common.Pager;
import cn.com.zhihetech.online.core.http.PageDataCallback;
import cn.com.zhihetech.online.core.view.LoadMoreListView;
import cn.com.zhihetech.online.core.view.OnLoadMoreListener;
import cn.com.zhihetech.online.core.view.ZhiheSwipeRefreshLayout;
import cn.com.zhihetech.online.core.view.adapter.ShoppingCartAdapter;
import cn.com.zhihetech.online.model.ShoppingCartModel;

/**
 * Created by ShenYunjie on 2016/1/22.
 */
@ContentView(R.layout.content_shopping_cart_fragment)
public class ShoppingCartFragment extends BaseFragment {

    @ViewInject(R.id.shopping_cart_zsrl)
    private ZhiheSwipeRefreshLayout refreshLayout;
    @ViewInject(R.id.shopping_cart_lmlv)
    private LoadMoreListView listView;

    private PageData<ShoppingCart> pageData;
    private ShoppingCartAdapter adapter;

    private PageDataCallback<ShoppingCart> refreshCallback = new PageDataCallback<ShoppingCart>() {
        @Override
        public void onPageData(PageData<ShoppingCart> result, List<ShoppingCart> rows) {
            pageData = result;
            adapter.refreshData(rows);
        }

        @Override
        public void onFinished() {
            super.onFinished();
            refreshLayout.setRefreshing(false);
        }
    };

    private PageDataCallback<ShoppingCart> loadMoreCallback = new PageDataCallback<ShoppingCart>() {
        @Override
        public void onPageData(PageData<ShoppingCart> result, List<ShoppingCart> rows) {
            pageData = result;
            adapter.addDatas(rows);
        }

        @Override
        public void onFinished() {
            super.onFinished();
            listView.loadComplete();
        }
    };

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
        refreshData();
    }

    private void initViews() {
        adapter = new ShoppingCartAdapter(getContext(), R.layout.content_shopping_cart_item);
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

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
    }

    private void refreshData() {
        refreshLayout.setRefreshing(true);
        new ShoppingCartModel().getShoppingCartsByUserId(refreshCallback, new Pager(), getUseId());
    }

    private void loadMoreData() {
        new ShoppingCartModel().getShoppingCartsByUserId(loadMoreCallback, pageData.getNextPage(), getUseId());
    }
}
