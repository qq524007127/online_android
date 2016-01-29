package cn.com.zhihetech.online.ui.activity;

import android.os.Bundle;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.Order;
import cn.com.zhihetech.online.core.adapter.OrderAdapter;
import cn.com.zhihetech.online.core.common.PageData;
import cn.com.zhihetech.online.core.common.Pager;
import cn.com.zhihetech.online.core.http.PageDataCallback;
import cn.com.zhihetech.online.core.view.LoadMoreListView;
import cn.com.zhihetech.online.core.view.OnLoadMoreListener;
import cn.com.zhihetech.online.core.view.ZhiheSwipeRefreshLayout;
import cn.com.zhihetech.online.model.OrderModel;

/**
 * Created by ShenYunjie on 2016/1/27.
 */
@ContentView(R.layout.activity_orders)
public class OrderActivity extends BaseActivity {
    public final static String ORDER_STATE_KEY = "_order_state_key";
    @ViewInject(R.id.order_zsrl)
    private ZhiheSwipeRefreshLayout refreshLayout;
    @ViewInject(R.id.order_lv)
    private LoadMoreListView orderListView;

    private PageData<Order> pageData;
    private OrderAdapter adapter;

    private int orderState = 0;

    private PageDataCallback<Order> loadCallback = new PageDataCallback<Order>() {
        @Override
        public void onPageData(PageData<Order> result, List<Order> rows) {
            pageData = result;
            adapter.addDatas(rows);
        }

        @Override
        public void onFinished() {
            super.onFinished();
            orderListView.loadComplete();
            if (refreshLayout.isRefreshing()) {
                refreshLayout.setRefreshing(false);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orderState = getIntent().getIntExtra(ORDER_STATE_KEY, 0);
        initViewAndData();
    }

    private void initViewAndData() {
        adapter = new OrderAdapter(this);
        orderListView.setAdapter(adapter);
        refreshLayout.setEnabled(false);
        orderListView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public boolean checkCanDoLoad() {
                return pageData != null && pageData.hasNextPage() && !refreshLayout.isRefreshing();
            }

            @Override
            public void onStartLoad() {
                loadData();
            }
        });
        loadData();
    }

    private void loadData() {
        Pager pager = new Pager();
        if (pageData == null) {
            refreshLayout.setRefreshing(true);
        } else {
            pager = pageData.getNextPage();
        }
        new OrderModel().getOrdersByUserId(loadCallback, pager, getUserId(), orderState);
    }
}
