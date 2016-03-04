package cn.com.zhihetech.online.ui.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.User;
import cn.com.zhihetech.online.core.adapter.ActivityFansAdapter;
import cn.com.zhihetech.online.core.common.PageData;
import cn.com.zhihetech.online.core.common.Pager;
import cn.com.zhihetech.online.core.http.PageDataCallback;
import cn.com.zhihetech.online.core.util.StringUtils;
import cn.com.zhihetech.online.core.view.LoadMoreListView;
import cn.com.zhihetech.online.core.view.OnLoadMoreListener;
import cn.com.zhihetech.online.core.view.ZhiheSwipeRefreshLayout;
import cn.com.zhihetech.online.model.ActivityFansModel;

/**
 * Created by ShenYunjie on 2016/2/26.
 */
@ContentView(R.layout.activity_activity_fans)
public class ActivityFansActivity extends BaseActivity {

    public final static String ACTIVITY_ID_KEY = "ACTIVITY_ID";

    @ViewInject(R.id.activity_fans_refresh_zsrl)
    private ZhiheSwipeRefreshLayout refreshLayout;
    @ViewInject(R.id.activity_fans_lmlv)
    private LoadMoreListView fansListView;

    private String activityId;
    private ActivityFansAdapter adapter;

    private PageData<User> pageData;

    /**
     * 刷新数据回调
     */
    private PageDataCallback<User> refreshFansCallback = new PageDataCallback<User>() {
        @Override
        public void onPageData(PageData<User> result, List<User> rows) {
            pageData = result;
            adapter.refreshData(rows);
        }

        @Override
        public void onFinished() {
            refreshLayout.setRefreshing(false);
        }
    };

    /**
     * 加载更多回调
     */
    private PageDataCallback<User> loadMoreCallback = new PageDataCallback<User>() {
        @Override
        public void onPageData(PageData<User> result, List<User> rows) {
            pageData = result;
            adapter.addDatas(rows);
        }

        @Override
        public void onFinished() {
            fansListView.loadComplete();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityId = getIntent().getStringExtra(ACTIVITY_ID_KEY);
        if (StringUtils.isEmpty(activityId)) {
            showMsg("数据初始化错误");
            finish();
            return;
        }
        initViewAndData();
    }

    private void initViewAndData() {
        initViews();
        refreshData();
    }

    private void refreshData() {
        new ActivityFansModel().getFansByActivityId(refreshFansCallback, new Pager(), this.activityId);
    }

    private void loadMoreData() {
        new ActivityFansModel().getFansByActivityId(refreshFansCallback, pageData.getNextPage(), this.activityId);
    }

    private void initViews() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });

        fansListView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public boolean checkCanDoLoad() {
                return pageData != null && pageData.hasNextPage() && !refreshLayout.isRefreshing();
            }

            @Override
            public void onStartLoad() {
                loadMoreData();
            }
        });
        adapter = new ActivityFansAdapter(this);
        fansListView.setAdapter(adapter);
    }
}
