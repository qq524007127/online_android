package cn.com.zhihetech.online.ui.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.Merchant;
import cn.com.zhihetech.online.core.common.PageData;
import cn.com.zhihetech.online.core.common.Pager;
import cn.com.zhihetech.online.core.http.PageDataCallback;
import cn.com.zhihetech.online.core.util.StringUtils;
import cn.com.zhihetech.online.core.view.LoadMoreListView;
import cn.com.zhihetech.online.core.view.OnLoadMoreListener;
import cn.com.zhihetech.online.core.view.ZhiheSwipeRefreshLayout;
import cn.com.zhihetech.online.core.adapter.MerchantAdapter;
import cn.com.zhihetech.online.model.MerchantModel;

@ContentView(R.layout.activity_daily_new)
public class MerchantListActivity extends BaseActivity {

    public final static String MERCHANT_TYPE_KEY = "_MERCHANT_TYPE_KEY";
    public final static String TYPE_ID_KEY = "_TYPE_ID_KEY";

    public final static String FEATURED_SHOP_TYPE = "FEATURED_SHOP_TYPE";   //特色店
    public final static String FEATURED_BLOCK_SHOP_TYPE = "FEATURED_BLOCK_SHOP_TYPE";   //特色街区店铺
    public final static String SHOPPING_CENTER_SHOP_TYPE = "SHOPPING_CENTER_SHOP_TYPE"; //购物中心店铺

    private String defaultType = FEATURED_SHOP_TYPE;
    private String typeId;

    @ViewInject(R.id.daily_new_srl)
    private ZhiheSwipeRefreshLayout refreshLayout;
    @ViewInject(R.id.merchant_list_lv)
    private LoadMoreListView merchantLv;

    private PageData<Merchant> pageData;
    private MerchantAdapter adapter;


    PageDataCallback<Merchant> refreshCallback = new PageDataCallback<Merchant>() {
        @Override
        public void onPageData(PageData<Merchant> result, List<Merchant> rows) {
            pageData = result;
            adapter.refreshData(rows);
        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            super.onError(ex, isOnCallback);
            onHttpError(ex, isOnCallback);
        }

        @Override
        public void onFinished() {
            refreshLayout.setRefreshing(false);
        }
    };

    PageDataCallback<Merchant> loadMoreCallback = new PageDataCallback<Merchant>() {
        @Override
        public void onPageData(PageData<Merchant> result, List<Merchant> rows) {
            pageData = result;
            adapter.addDatas(rows);
        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            super.onError(ex, isOnCallback);
            onHttpError(ex, isOnCallback);
        }

        @Override
        public void onFinished() {
            merchantLv.loadComplete();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String type = getIntent().getStringExtra(MERCHANT_TYPE_KEY);
        typeId = getIntent().getStringExtra(TYPE_ID_KEY);
        if (!StringUtils.isEmpty(type)) {
            defaultType = type;
        }
        if (!defaultType.equals(FEATURED_SHOP_TYPE) && StringUtils.isEmpty(typeId)) {
            showMsg("出错了！");
            finish();
            return;
        }
        initViews();
        refreshData();
    }

    private void initViews() {
        initHeader();
        adapter = new MerchantAdapter(this, R.layout.content_merchant_list_item);
        merchantLv.setAdapter(adapter);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });

        merchantLv.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public boolean checkCanDoLoad() {
                return pageData != null && pageData.hasNextPage() && !refreshLayout.isRefreshing();
            }

            @Override
            public void onStartLoad() {
                loadMoreData();
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
        switch (defaultType) {
            case FEATURED_BLOCK_SHOP_TYPE:
                new MerchantModel().getMerchantsByTypeAndTypeId(refreshCallback, new Pager(), 2, typeId);
                break;
            case SHOPPING_CENTER_SHOP_TYPE:
                new MerchantModel().getMerchantsByTypeAndTypeId(refreshCallback, new Pager(), 1, typeId);
                break;
            default:
                new MerchantModel().getMerchantsByTypeAndTypeId(refreshCallback, new Pager(), 3, null);
        }
    }

    /**
     * 分页加载（加载更多）
     */
    private void loadMoreData() {
        switch (defaultType) {
            case FEATURED_BLOCK_SHOP_TYPE:
                new MerchantModel().getMerchantsByTypeAndTypeId(loadMoreCallback, pageData.getNextPage(), 2, typeId);
                break;
            case SHOPPING_CENTER_SHOP_TYPE:
                new MerchantModel().getMerchantsByTypeAndTypeId(loadMoreCallback, pageData.getNextPage(), 1, typeId);
                break;
            default:
                new MerchantModel().getMerchantsByTypeAndTypeId(loadMoreCallback, pageData.getNextPage(), 3, null);
        }
    }

    /**
     * 添加顶部轮播图
     */
    private void initHeader() {
        View headerView = LayoutInflater.from(this).inflate(R.layout.content_merchant_list_header, null);
        merchantLv.addHeaderView(headerView);
    }
}
