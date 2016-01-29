package cn.com.zhihetech.online.ui.activity;

import android.os.Bundle;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.GoodsAttributeSet;
import cn.com.zhihetech.online.core.common.PageData;
import cn.com.zhihetech.online.core.common.Pager;
import cn.com.zhihetech.online.core.http.PageDataCallback;
import cn.com.zhihetech.online.core.view.LoadMoreGridView;
import cn.com.zhihetech.online.core.view.OnLoadMoreListener;
import cn.com.zhihetech.online.core.adapter.GoodsAttributeSetAdapter;
import cn.com.zhihetech.online.core.view.ZhiheSwipeRefreshLayout;
import cn.com.zhihetech.online.model.GoodsAttributeSetModel;

/**
 * Created by ShenYunjie on 2016/1/21.
 */
@ContentView(R.layout.activity_category)
public class CategoryActivity extends BaseActivity {
    @ViewInject(R.id.category_lvgv)
    private LoadMoreGridView gridView;
    @ViewInject(R.id.category_zsrl)
    private ZhiheSwipeRefreshLayout refreshLayout;

    private PageData<GoodsAttributeSet> pageData;
    private GoodsAttributeSetAdapter adapter;

    private PageDataCallback<GoodsAttributeSet> loadCallback = new PageDataCallback<GoodsAttributeSet>() {
        @Override
        public void onPageData(PageData<GoodsAttributeSet> result, List<GoodsAttributeSet> rows) {
            pageData = result;
            adapter.addDatas(rows);
        }

        @Override
        public void onFinished() {
            refreshLayout.setRefreshing(false);
            gridView.loadComplete();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        loadData();
    }

    private void initViews() {
        refreshLayout.setEnabled(false);
        adapter = new GoodsAttributeSetAdapter(this, R.layout.content_category_item);
        gridView.setAdapter(adapter);
        gridView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public boolean checkCanDoLoad() {
                return pageData != null && pageData.hasNextPage();
            }

            @Override
            public void onStartLoad() {
                loadData();
            }
        });
    }

    private void loadData() {
        refreshLayout.setRefreshing(pageData == null);
        Pager pager = pageData == null ? new Pager() : pageData.getNextPage();
        new GoodsAttributeSetModel().getCategories(loadCallback, pager);
    }
}
