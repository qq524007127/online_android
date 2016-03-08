package cn.com.zhihetech.online.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.ActivityGoods;
import cn.com.zhihetech.online.core.adapter.ActivityGoodsAdapter;
import cn.com.zhihetech.online.core.common.PageData;
import cn.com.zhihetech.online.core.common.Pager;
import cn.com.zhihetech.online.core.http.PageDataCallback;
import cn.com.zhihetech.online.core.util.StringUtils;
import cn.com.zhihetech.online.core.view.LoadMoreListView;
import cn.com.zhihetech.online.core.view.OnLoadMoreListener;
import cn.com.zhihetech.online.core.view.ZhiheSwipeRefreshLayout;
import cn.com.zhihetech.online.model.ActivityGoodsModel;

/**
 * Created by ShenYunjie on 2016/3/7.
 */
@ContentView(R.layout.activity_activity_goods)
public class SeckillGoodsListActivity extends MerchantBaseActivity {

    public final static String RESULT_ACTIVITY_GOODS_KEY = "RESULT_ACTIVITY_GOODS";
    public final static String ACTIVITY_ID_KEY = "_ACTIVITY_ID";

    @ViewInject(R.id.activity_goods_refresh_layout)
    private ZhiheSwipeRefreshLayout refreshLayout;
    @ViewInject(R.id.activity_goods_load_more_lv)
    private LoadMoreListView loadMoreListView;

    private PageData<ActivityGoods> pageData;
    private ActivityGoodsAdapter adapter;
    private String activityId;

    /**
     * 刷新（重载）数据回调
     */
    private PageDataCallback<ActivityGoods> refreshCallback = new PageDataCallback<ActivityGoods>() {
        @Override
        public void onPageData(PageData<ActivityGoods> result, List<ActivityGoods> rows) {
            pageData = result;
            adapter.refreshData(rows);
        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            showMsg("数据加载失败，请重试！");
        }

        @Override
        public void onFinished() {
            refreshLayout.setRefreshing(false);
        }
    };

    private PageDataCallback<ActivityGoods> loadMoreCallback = new PageDataCallback<ActivityGoods>() {
        @Override
        public void onPageData(PageData<ActivityGoods> result, List<ActivityGoods> rows) {
            pageData = result;
            adapter.addDatas(rows);
        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            showMsg("数据加载失败，请重试！");
        }

        @Override
        public void onFinished() {
            loadMoreListView.loadComplete();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityId = getIntent().getStringExtra(ACTIVITY_ID_KEY);
        if (StringUtils.isEmpty(activityId)) {
            showMsg("出错了");
            finish();
            return;
        }
        onInitViewAndData();
    }

    private void onInitViewAndData() {
        initViews();
        refreshData();
    }

    private void initViews() {
        adapter = new ActivityGoodsAdapter(this);
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
        loadMoreListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra(RESULT_ACTIVITY_GOODS_KEY, adapter.getItem(position));
                setResult(RESULT_OK, intent);
                finish();
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
     * 刷新（重新）加载数据
     */
    private void refreshData() {
        if (!refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(true);
        }
        new ActivityGoodsModel().getActivityGoodsByMerchantId(refreshCallback, new Pager(), getUserId(), activityId);
    }

    /**
     * 分页加载数据
     */
    private void loadMoreData() {
        new ActivityGoodsModel().getActivityGoodsByMerchantId(loadMoreCallback, pageData.getNextPage(), getUserId(), activityId);
    }
}
