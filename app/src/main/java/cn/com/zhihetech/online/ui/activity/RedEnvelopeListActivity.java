package cn.com.zhihetech.online.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.RedEnvelop;
import cn.com.zhihetech.online.core.adapter.RedEnvelopAdapter;
import cn.com.zhihetech.online.core.common.PageData;
import cn.com.zhihetech.online.core.common.Pager;
import cn.com.zhihetech.online.core.common.ResponseMessage;
import cn.com.zhihetech.online.core.common.ResponseStateCode;
import cn.com.zhihetech.online.core.http.ObjectCallback;
import cn.com.zhihetech.online.core.http.PageDataCallback;
import cn.com.zhihetech.online.core.util.StringUtils;
import cn.com.zhihetech.online.core.view.LoadMoreListView;
import cn.com.zhihetech.online.core.view.OnLoadMoreListener;
import cn.com.zhihetech.online.core.view.ZhiheSwipeRefreshLayout;
import cn.com.zhihetech.online.model.RedEnvelopModel;

/**
 * Created by ShenYunjie on 2016/3/2.
 */
@ContentView(R.layout.activity_red_envelope)
public class RedEnvelopeListActivity extends MerchantBaseActivity {
    public final static String MERCHANT_ID_KEY = "_MERCHANT_ID";
    public final static String ACTIVITY_ID_KEY = "_ACTIVITY_ID";
    public static String RESULT_RED_ENVELOP_KEY = "RESULT_RED_ENVELOP";

    @ViewInject(R.id.red_envelope_refresh_layout)
    private ZhiheSwipeRefreshLayout refreshLayout;
    @ViewInject(R.id.red_envelope_list_view)
    private LoadMoreListView loadMoreListView;

    private ProgressDialog progressDialog;

    private String merchantId;
    private String activityId;
    private PageData<RedEnvelop> pageData;
    private RedEnvelopAdapter adapter;

    /**
     * 刷新（重载）数据回调
     */
    private PageDataCallback<RedEnvelop> refreshCallback = new PageDataCallback<RedEnvelop>() {
        @Override
        public void onPageData(PageData<RedEnvelop> result, List<RedEnvelop> rows) {
            pageData = result;
            adapter.refreshData(rows);
        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            showMsg("数据加载失败，请刷新!");
        }

        @Override
        public void onFinished() {
            refreshLayout.setRefreshing(false);
        }
    };

    /**
     * 分页加载数据回调
     */
    private PageDataCallback<RedEnvelop> loadMoreCallback = new PageDataCallback<RedEnvelop>() {
        @Override
        public void onPageData(PageData<RedEnvelop> result, List<RedEnvelop> rows) {
            pageData = result;
            adapter.addDatas(rows);
        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            showMsg("数据加载失败，请重试!");
        }

        @Override
        public void onFinished() {
            loadMoreListView.loadComplete();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        merchantId = getIntent().getStringExtra(MERCHANT_ID_KEY);
        activityId = getIntent().getStringExtra(ACTIVITY_ID_KEY);
        if (StringUtils.isEmpty(merchantId) || StringUtils.isEmpty(activityId)) {
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
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
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
        adapter = new RedEnvelopAdapter(this);
        loadMoreListView.setAdapter(adapter);
        loadMoreListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                /*progressDialog = ProgressDialog.show(getSelf(), "", "正在派发红包...");
                new RedEnvelopModel().updateSendState(new ObjectCallback<ResponseMessage>() {
                    @Override
                    public void onObject(ResponseMessage data) {
                        if (data.getCode() != ResponseStateCode.SUCCESS) {
                            showMsg(data.getMsg());
                            return;
                        }
                        Intent intent = new Intent();
                        intent.putExtra(RESULT_RED_ENVELOP_KEY, adapter.getItem(position));
                        setResult(RESULT_OK, intent);
                        finish();
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        showMsg("红包派发失败，请重试！");
                    }

                    @Override
                    public void onFinished() {
                        super.onFinished();
                        progressDialog.dismiss();
                    }
                }, adapter.getItem(position).getEnvelopId());*/
                Intent intent = new Intent();
                intent.putExtra(RESULT_RED_ENVELOP_KEY, adapter.getItem(position));
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    /**
     * 分页加载数据
     */
    private void loadMoreData() {
        new RedEnvelopModel().getRedEnvelopsByMerchantIdAndActivityId(loadMoreCallback, pageData.getNextPage(), merchantId, activityId);
    }

    /**
     * 刷新（重载）数据
     */
    private void refreshData() {
        if (!refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(true);
        }
        new RedEnvelopModel().getRedEnvelopsByMerchantIdAndActivityId(refreshCallback, new Pager(), merchantId, activityId);
    }
}
