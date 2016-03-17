package cn.com.zhihetech.online.ui.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.CouponItem;
import cn.com.zhihetech.online.core.adapter.CouponItemAdapter;
import cn.com.zhihetech.online.core.common.PageData;
import cn.com.zhihetech.online.core.common.Pager;
import cn.com.zhihetech.online.core.http.PageDataCallback;
import cn.com.zhihetech.online.core.view.LoadMoreListView;
import cn.com.zhihetech.online.core.view.OnLoadMoreListener;
import cn.com.zhihetech.online.core.view.ZhiheSwipeRefreshLayout;
import cn.com.zhihetech.online.model.CouponItemModel;

/**
 * Created by ShenYunjie on 2016/3/17.
 */
@ContentView(R.layout.activity_my_coupon)
public class MyCouponItemActivity extends BaseActivity {

    @ViewInject(R.id.my_volum_refresh_layout)
    private ZhiheSwipeRefreshLayout refreshLayout;
    @ViewInject(R.id.my_volum_list_view)
    private LoadMoreListView couponListView;

    private PageData<CouponItem> pageData;
    private CouponItemAdapter adapter;

    /**
     * 刷新（重载）回调
     */
    private PageDataCallback<CouponItem> refreshCallback = new PageDataCallback<CouponItem>() {
        @Override
        public void onPageData(PageData<CouponItem> result, List<CouponItem> rows) {
            pageData = result;
            adapter.refreshData(rows);
        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            showMsg(R.string.data_load_fial_please_try_agin);
        }

        @Override
        public void onFinished() {
            refreshLayout.setRefreshing(false);
        }
    };

    /**
     * 分页加载回调
     */
    private PageDataCallback<CouponItem> loadMoreCallback = new PageDataCallback<CouponItem>() {
        @Override
        public void onPageData(PageData<CouponItem> result, List<CouponItem> rows) {
            pageData = result;
            adapter.addDatas(rows);
        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            showMsg(R.string.data_load_fial_please_try_agin);
        }

        @Override
        public void onFinished() {
            couponListView.loadComplete();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        refreshData();
    }

    private void initViews() {
        adapter = new CouponItemAdapter(this);
        couponListView.setAdapter(adapter);
        couponListView.setOnLoadMoreListener(new OnLoadMoreListener() {
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

    /**
     * 刷新（重载）数据
     */
    private void refreshData() {
        if (!refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(true);
        }
        new CouponItemModel().getCouponItemsByUserId(refreshCallback, new Pager(), getUserId());
    }

    /**
     * 分页加载
     */
    private void loadMoreData() {
        new CouponItemModel().getCouponItemsByUserId(loadMoreCallback, pageData.getNextPage(), getUserId());
    }
}
