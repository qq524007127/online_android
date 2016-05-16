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
import cn.com.zhihetech.online.bean.RedEnvelopItem;
import cn.com.zhihetech.online.core.adapter.RedEnvelopItemAdapter;
import cn.com.zhihetech.online.core.common.PageData;
import cn.com.zhihetech.online.core.common.Pager;
import cn.com.zhihetech.online.core.http.PageDataCallback;
import cn.com.zhihetech.online.core.view.LoadMoreListView;
import cn.com.zhihetech.online.core.view.OnLoadMoreListener;
import cn.com.zhihetech.online.core.view.ZhiheSwipeRefreshLayout;
import cn.com.zhihetech.online.model.RedEnvelopItemModel;

/**
 * Created by ShenYunjie on 2016/3/8.
 */
@ContentView(R.layout.activity_red_envelop_item_list)
public class MyRedEnvelopItemListActivity extends BaseActivity {

    public final static int REQUEST_RED_ENVELOP_ITEM_DETAIL_CODE = 0X10;

    @ViewInject(R.id.envelop_item_refresh_layout)
    private ZhiheSwipeRefreshLayout refreshLayout;
    @ViewInject(R.id.envelop_item_list_view)
    private LoadMoreListView envelopItemListView;

    private PageData<RedEnvelopItem> pageData;
    private RedEnvelopItemAdapter adapter;

    /**
     * 刷新（重载）数据回调
     */
    private PageDataCallback<RedEnvelopItem> refreshCallback = new PageDataCallback<RedEnvelopItem>() {
        @Override
        public void onPageData(PageData<RedEnvelopItem> result, List<RedEnvelopItem> rows) {
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
    private PageDataCallback<RedEnvelopItem> loadMoreCallback = new PageDataCallback<RedEnvelopItem>() {
        @Override
        public void onPageData(PageData<RedEnvelopItem> result, List<RedEnvelopItem> rows) {
            pageData = result;
            adapter.addDatas(rows);
        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            showMsg(R.string.data_load_fial_please_try_agin);
        }

        @Override
        public void onFinished() {
            envelopItemListView.loadComplete();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViewAndData();
    }

    private void initViewAndData() {
        intiViews();
        refreshData();
    }

    private void intiViews() {
        adapter = new RedEnvelopItemAdapter(this);
        envelopItemListView.setAdapter(adapter);
        envelopItemListView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public boolean checkCanDoLoad() {
                return pageData != null && pageData.hasNextPage() && !refreshLayout.isRefreshing();
            }

            @Override
            public void onStartLoad() {
                loadMoreData();
            }
        });
        envelopItemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getSelf(), RedEnvelopItemDetailActivity.class);
                intent.putExtra(RedEnvelopItemDetailActivity.RED_ENVELOP_ITEM_ID, adapter.getItem(position).getEnvelopItemId());
                intent.putExtra(RedEnvelopItemDetailActivity.RESULT_FLAG_KEY, true);
                startActivityForResult(intent, REQUEST_RED_ENVELOP_ITEM_DETAIL_CODE);
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
        new RedEnvelopItemModel().getEnvelopItemsByUserId(refreshCallback, new Pager(), getUserId());
    }

    /**
     * 分页加载数据
     */
    private void loadMoreData() {
        new RedEnvelopItemModel().getEnvelopItemsByUserId(loadMoreCallback, pageData.getNextPage(), getUserId());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_RED_ENVELOP_ITEM_DETAIL_CODE && resultCode == RESULT_OK) {
            refreshData();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
