package cn.com.zhihetech.online.ui.widget;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

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
    @ViewInject(R.id.order_zsrl)
    private ZhiheSwipeRefreshLayout refreshLayout;
    @ViewInject(R.id.order_lv)
    private LoadMoreListView orderListView;

    private PageData<Order> pageData;
    private OrderAdapter adapter;

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
        orderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showMsg(view, "我被点击了");
            }
        });
        loadData();
    }

    private void loadData() {
        Pager pager = new Pager(5);
        if (pageData == null) {
            refreshLayout.setRefreshing(true);
        } else {
            pager = pageData.getNextPage();
        }
        new OrderModel().getOrdersByUserId(loadCallback, pager, getUserId(), null);
    }
}
