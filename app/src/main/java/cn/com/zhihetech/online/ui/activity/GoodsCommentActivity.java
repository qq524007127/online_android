package cn.com.zhihetech.online.ui.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.GoodsScore;
import cn.com.zhihetech.online.core.adapter.GoodsScoreAdapter;
import cn.com.zhihetech.online.core.common.PageData;
import cn.com.zhihetech.online.core.common.Pager;
import cn.com.zhihetech.online.core.http.PageDataCallback;
import cn.com.zhihetech.online.core.util.StringUtils;
import cn.com.zhihetech.online.core.view.LoadMoreListView;
import cn.com.zhihetech.online.core.view.OnLoadMoreListener;
import cn.com.zhihetech.online.core.view.ZhiheSwipeRefreshLayout;
import cn.com.zhihetech.online.model.GoodsScoreModel;

/**
 * Created by ShenYunjie on 2016/3/16.
 */
@ContentView(R.layout.activity_goods_comments)
public class GoodsCommentActivity extends BaseActivity {

    public final static String GOODS_ID_KEY = "GOODS_ID_KEY";

    @ViewInject(R.id.goods_comment_refresh_layout)
    private ZhiheSwipeRefreshLayout refreshLayout;
    @ViewInject(R.id.goods_comment_list_view)
    private LoadMoreListView listView;

    private String goodsId;
    private PageData<GoodsScore> pageData;
    private GoodsScoreAdapter adapter;

    /**
     * 刷新（重载）数据回调
     */
    private PageDataCallback<GoodsScore> refreshCallback = new PageDataCallback<GoodsScore>() {
        @Override
        public void onPageData(PageData<GoodsScore> result, List<GoodsScore> rows) {
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
    private PageDataCallback<GoodsScore> loadMoreCallback = new PageDataCallback<GoodsScore>() {
        @Override
        public void onPageData(PageData<GoodsScore> result, List<GoodsScore> rows) {
            pageData = result;
            adapter.addDatas(rows);
        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            showMsg(R.string.data_load_fial_please_try_agin);
        }

        @Override
        public void onFinished() {
            listView.loadComplete();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        goodsId = getIntent().getStringExtra(GOODS_ID_KEY);
        if (StringUtils.isEmpty(goodsId)) {
            showMsg("出错了");
            finish();
            return;
        }
        initViewAndData();
    }

    private void initViewAndData() {
        initViews();
        refreshData();
    }

    private void initViews() {
        adapter = new GoodsScoreAdapter(this);
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

    /**
     * 刷新（重载）数据
     */
    private void refreshData() {
        if (!refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(true);
        }
        new GoodsScoreModel().getGoodsScoresByGoodsId(refreshCallback, new Pager(), this.goodsId);
    }

    /**
     * 分页加载（加载更多）
     */
    private void loadMoreData() {
        new GoodsScoreModel().getGoodsScoresByGoodsId(loadMoreCallback, pageData.getNextPage(), this.goodsId);
    }
}
