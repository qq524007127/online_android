package cn.com.zhihetech.online.ui.widget;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;

import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.Banner;
import cn.com.zhihetech.online.bean.Merchant;
import cn.com.zhihetech.online.core.common.PageData;
import cn.com.zhihetech.online.core.common.Pager;
import cn.com.zhihetech.online.core.http.ArrayCallback;
import cn.com.zhihetech.online.core.http.PageDataCallback;
import cn.com.zhihetech.online.core.view.FixedBanner;
import cn.com.zhihetech.online.core.view.LoadMoreListView;
import cn.com.zhihetech.online.core.view.OnLoadMoreListener;
import cn.com.zhihetech.online.core.view.ZhiheSwipeRefreshLayout;
import cn.com.zhihetech.online.core.view.adapter.MerchantAdapter;
import cn.com.zhihetech.online.model.BannerModel;
import cn.com.zhihetech.online.model.MerchantModel;

@ContentView(R.layout.activity_daily_new)
public class DailyNewActivity extends BaseActivity {

    @ViewInject(R.id.daily_new_srl)
    private ZhiheSwipeRefreshLayout refreshLayout;
    @ViewInject(R.id.merchant_list_lv)
    private LoadMoreListView merchantLv;

    PageData<Merchant> pageData;
    MerchantAdapter adapter;
    FixedBanner banner;


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
        initViews();
    }

    private void initViews() {
        initBanner();
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
                return pageData != null && pageData.hasNextPage();
            }

            @Override
            public void onStartLoad() {
                loadMoreData();
            }
        });
        refreshData();
    }


    private void refreshData() {
        if (!refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(true);
        }
        new MerchantModel().getDailyNewList(refreshCallback, new Pager(3));
    }

    private void loadMoreData() {
        new MerchantModel().getDailyNewList(loadMoreCallback, pageData.getNextPage());
    }

    /**
     * 添加顶部轮播图
     */
    private void initBanner() {
        banner = new FixedBanner<>(this, true);
        banner.setAspectRatio(0.4f);
        new BannerModel().getBanners(new ArrayCallback<Banner>() {
            @Override
            public void onArray(List<Banner> datas) {
                banner.setPages(new CBViewHolderCreator<OnLoadMoreListener.BannerHolder>() {
                    @Override
                    public OnLoadMoreListener.BannerHolder createHolder() {
                        return new OnLoadMoreListener.BannerHolder();
                    }
                }, datas);
            }
        });
        merchantLv.addHeaderView(banner);
    }
}
