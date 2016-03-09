package cn.com.zhihetech.online.ui.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.FocusGoods;
import cn.com.zhihetech.online.bean.Goods;
import cn.com.zhihetech.online.core.adapter.FavoriteAdapter;
import cn.com.zhihetech.online.core.common.PageData;
import cn.com.zhihetech.online.core.common.Pager;
import cn.com.zhihetech.online.core.common.ResponseMessage;
import cn.com.zhihetech.online.core.common.ResponseStateCode;
import cn.com.zhihetech.online.core.http.ObjectCallback;
import cn.com.zhihetech.online.core.http.PageDataCallback;
import cn.com.zhihetech.online.core.view.LoadMoreListView;
import cn.com.zhihetech.online.core.view.OnLoadMoreListener;
import cn.com.zhihetech.online.core.view.ZhiheSwipeRefreshLayout;
import cn.com.zhihetech.online.model.FocusGoodsModel;

/**
 * Created by ShenYunjie on 2016/3/9.
 */
@ContentView(R.layout.activity_my_favorites)
public class MyFavoritesActivity extends BaseActivity {

    @ViewInject(R.id.my_favorites_refresh_layout)
    private ZhiheSwipeRefreshLayout refreshLayout;
    @ViewInject(R.id.my_favorites_list_view)
    private LoadMoreListView loadMoreListView;

    private PageData<FocusGoods> pageData;
    private FavoriteAdapter adapter;

    /**
     * 刷新（重载）数据回调
     */
    private PageDataCallback<FocusGoods> refreshCallback = new PageDataCallback<FocusGoods>() {
        @Override
        public void onPageData(PageData<FocusGoods> result, List<FocusGoods> rows) {
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
    private PageDataCallback<FocusGoods> loadMoreCallback = new PageDataCallback<FocusGoods>() {
        @Override
        public void onPageData(PageData<FocusGoods> result, List<FocusGoods> rows) {
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
        initViewAndData();
    }

    private void initViewAndData() {
        initViews();
        refreshData();
    }

    private void initViews() {
        adapter = new FavoriteAdapter(this);
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
                Goods goods = adapter.getItem(position).getGoods();
                Intent intent = new Intent(getSelf(), GoodsInfoActivity.class);
                intent.putExtra(GoodsInfoActivity.GOODS_ID_KEY, goods.getGoodsId());
                intent.putExtra(GoodsInfoActivity.GOODS_NAME_KEY, goods.getGoodsName());
                startActivity(intent);
            }
        });
        loadMoreListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(getSelf())
                        .setTitle(R.string.tip)
                        .setMessage("确定要删除选中的收藏吗？")
                        .setNegativeButton(R.string.cancel, null)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteFavorite(adapter.getItem(position));
                            }
                        }).show();
                return false;
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
     * 删除指定的收藏
     *
     * @param focGoods
     */
    private void deleteFavorite(FocusGoods focGoods) {
        final ProgressDialog progressDialog = ProgressDialog.show(this, "", getString(R.string.data_executing));
        progressDialog.setCancelable(true);
        final Callback.Cancelable cancelable = new FocusGoodsModel().unFocusGoods(new ObjectCallback<ResponseMessage>() {
            @Override
            public void onObject(ResponseMessage data) {
                if (data.getCode() == ResponseStateCode.SUCCESS) {
                    showMsg("删除成功！");
                    refreshData();
                    return;
                }
                showMsg(data.getMsg());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                showMsg("删除失败，请重试");
            }

            @Override
            public void onFinished() {
                progressDialog.dismiss();
            }
        }, focGoods.getGoods().getGoodsId(), getUserId());
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
     * 刷新（重载）数据
     */
    private void refreshData() {
        if (!refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(true);
        }
        new FocusGoodsModel().getFavoriteGoodsesByUserId(refreshCallback, new Pager(), getUserId());
    }

    /**
     * 分页加载数据
     */
    private void loadMoreData() {
        new FocusGoodsModel().getFavoriteGoodsesByUserId(loadMoreCallback, pageData.getNextPage(), getUserId());
    }
}
