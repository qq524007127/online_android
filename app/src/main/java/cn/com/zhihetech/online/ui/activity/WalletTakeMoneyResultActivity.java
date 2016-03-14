package cn.com.zhihetech.online.ui.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.UserWithdraw;
import cn.com.zhihetech.online.core.common.PageData;
import cn.com.zhihetech.online.core.common.Pager;
import cn.com.zhihetech.online.core.http.PageDataCallback;
import cn.com.zhihetech.online.core.view.LoadMoreListView;
import cn.com.zhihetech.online.core.view.OnLoadMoreListener;
import cn.com.zhihetech.online.core.view.ZhiheSwipeRefreshLayout;

/**
 * Created by ShenYunjie on 2016/3/14.
 */
@ContentView(R.layout.activity_wallet_take_money_result)
public class WalletTakeMoneyResultActivity extends BaseActivity {

    @ViewInject(R.id.take_money_result_refresh_layout)
    private ZhiheSwipeRefreshLayout refreshLayout;
    @ViewInject(R.id.take_money_result_list_view)
    private LoadMoreListView loadMoreListView;

    private PageData<UserWithdraw> pageData;
    private UserWithdrawAdapter adapter;

    /**
     * 刷新（重载）数据回调
     */
    private PageDataCallback<UserWithdraw> refreshCallback = new PageDataCallback<UserWithdraw>() {
        @Override
        public void onPageData(PageData<UserWithdraw> result, List<UserWithdraw> rows) {
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
    private PageDataCallback<UserWithdraw> loadMoreCallback = new PageDataCallback<UserWithdraw>() {
        @Override
        public void onPageData(PageData<UserWithdraw> result, List<UserWithdraw> rows) {
            pageData = result;
            adapter.addDatas(rows);
        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            showMsg(R.string.data_load_fial_please_try_agin);
        }

        @Override
        public void onFinished() {
            loadMoreListView.loadComplete();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intViews();
        refreshData();
    }

    private void intViews() {
        adapter = new UserWithdrawAdapter(this);
        loadMoreListView.setAdapter(adapter);
        loadMoreListView.setOnLoadMoreListener(new OnLoadMoreListener() {
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
     * 重载（刷新）数据
     */
    private void refreshData() {
        if(!refreshLayout.isRefreshing()){
            refreshLayout.setRefreshing(true);
        }
        new UserWithdrawModel().getWithDrawResults(refreshCallback,new Pager(),getUserId());
    }

    /**
     * 分页加载数据
     */
    private void loadMoreData() {
        new UserWithdrawModel().getWithDrawResults(loadMoreCallback,pageData.getNextPage(),getUserId());
    }
}
