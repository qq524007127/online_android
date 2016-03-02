package cn.com.zhihetech.online.ui.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.Goods;
import cn.com.zhihetech.online.core.adapter.GoodsListAdapter;
import cn.com.zhihetech.online.core.common.PageData;
import cn.com.zhihetech.online.core.common.Pager;
import cn.com.zhihetech.online.core.http.PageDataCallback;
import cn.com.zhihetech.online.core.util.StringUtils;
import cn.com.zhihetech.online.core.view.LoadMoreListView;
import cn.com.zhihetech.online.core.view.OnLoadMoreListener;
import cn.com.zhihetech.online.core.view.ZhiheProgressDialog;
import cn.com.zhihetech.online.model.GoodsModel;
import cn.com.zhihetech.online.model.ModelParams;

/**
 * Created by ShenYunjie on 2016/3/2.
 */
@ContentView(R.layout.activity_goods_list)
public class GoodsListActivity extends MerchantBaseActivity {

    public final static String MERCHANT_ID_KEY = "_merchant_id";
    public final static String RESULT_GOODS_KEY = "resultGoods";

    @ViewInject(R.id.goods_list_view)
    private LoadMoreListView goodsListView;
    private ProgressDialog progressDialog;

    private PageData<Goods> pageData;
    private GoodsListAdapter adapter;
    private String merchantId;

    /**
     * 首次加载数据回调
     */
    private PageDataCallback<Goods> initCallback = new PageDataCallback<Goods>() {
        @Override
        public void onPageData(PageData<Goods> result, List<Goods> rows) {
            pageData = result;
            adapter.refreshData(rows);
        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            showMsg("数据加载失败，请重试！");
        }

        @Override
        public void onFinished() {
            progressDialog.dismiss();
        }
    };

    /**
     * 分页加载回调
     */
    private PageDataCallback<Goods> loadmoreCallback = new PageDataCallback<Goods>() {
        @Override
        public void onPageData(PageData<Goods> result, List<Goods> rows) {
            pageData = result;
            adapter.addDatas(rows);
        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            showMsg("数据加载失败，请重试！");
        }

        @Override
        public void onFinished() {
            goodsListView.loadComplete();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        merchantId = getIntent().getStringExtra(MERCHANT_ID_KEY);
        if (StringUtils.isEmpty(merchantId)) {
            showMsg("出错了");
            finish();
            return;
        }
        initViewAndData();
    }

    private void initViewAndData() {
        initViews();
        initData();
    }

    private void initViews() {
        goodsListView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public boolean checkCanDoLoad() {
                return pageData != null && pageData.hasNextPage();
            }

            @Override
            public void onStartLoad() {
                loadMoreData();
            }
        });
        goodsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra(RESULT_GOODS_KEY, adapter.getItem(position));
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        adapter = new GoodsListAdapter(this);
        goodsListView.setAdapter(adapter);
    }

    /**
     * 首次加载数据
     */
    private void initData() {
        ModelParams params = new ModelParams().addPager(new Pager());
        progressDialog = ProgressDialog.show(this, "", getString(R.string.data_loading));
        progressDialog.setCancelable(true);
        final Callback.Cancelable cancelable = new GoodsModel().getGoodsesByMerchantId(initCallback, params, merchantId);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (!cancelable.isCancelled()) {
                    cancelable.cancel();
                }
            }
        });
    }

    /**
     * 分页加载数据
     */
    private void loadMoreData() {
        ModelParams params = new ModelParams().addPager(pageData.getNextPage());
        new GoodsModel().getGoodsesByMerchantId(loadmoreCallback, params, merchantId);
    }
}
