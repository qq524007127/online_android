package cn.com.zhihetech.online.ui.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.ShoppingCart;
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
import cn.com.zhihetech.online.core.adapter.ShoppingCartAdapter;
import cn.com.zhihetech.online.model.ShoppingCartModel;

/**
 * Created by ShenYunjie on 2016/1/22.
 */
@ContentView(R.layout.content_shopping_cart_fragment)
public class ShoppingCartFragment extends BaseFragment {

    @ViewInject(R.id.shopping_cart_zsrl)
    private ZhiheSwipeRefreshLayout refreshLayout;
    @ViewInject(R.id.shopping_cart_lmlv)
    private LoadMoreListView listView;
    @ViewInject(R.id.shopping_cart_delete_view)
    private View deleteView;
    @ViewInject(R.id.shopping_cart_buy_view)
    private View buyView;

    private ProgressDialog progressDialog;

    private PageData<ShoppingCart> pageData;
    private ShoppingCartAdapter adapter;

    /**
     * 刷新（重载）数据回调
     */
    private PageDataCallback<ShoppingCart> refreshCallback = new PageDataCallback<ShoppingCart>() {
        @Override
        public void onPageData(PageData<ShoppingCart> result, List<ShoppingCart> rows) {
            pageData = result;
            adapter.refreshData(rows);
        }

        @Override
        public void onFinished() {
            super.onFinished();
            refreshLayout.setRefreshing(false);
        }
    };

    /**
     * 分页加载回调
     */
    private PageDataCallback<ShoppingCart> loadMoreCallback = new PageDataCallback<ShoppingCart>() {
        @Override
        public void onPageData(PageData<ShoppingCart> result, List<ShoppingCart> rows) {
            pageData = result;
            adapter.addDatas(rows);
        }

        @Override
        public void onFinished() {
            super.onFinished();
            listView.loadComplete();
        }
    };

    /**
     * 删除购物车回调
     */
    private ObjectCallback<ResponseMessage> deleteCallback = new ObjectCallback<ResponseMessage>() {
        @Override
        public void onObject(ResponseMessage data) {
            if (data.getCode() == ResponseStateCode.SUCCESS) {
                showMsg(deleteView, "删除成功");
                refreshData();
            } else {
                showMsg(deleteView, data.getMsg());
            }
        }

        @Override
        public void onFinished() {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    };


    /**
     * 更新购物车商品数量
     */
    class UpdateAmountCallback extends ObjectCallback<ResponseMessage> {

        ShoppingCart cart;
        int amount;

        public UpdateAmountCallback(ShoppingCart cart, int amount) {
            this.cart = cart;
            this.amount = amount;
        }

        @Override
        public void onObject(ResponseMessage data) {
            cart.setAmount(amount);
        }

        @Override
        public void onFinished() {
            adapter.notifyDataSetChanged();
            progressDialog.dismiss();
        }
    }

    public ShoppingCartFragment() {
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
        refreshData();
    }

    private void initViews() {
        initProgress();
        initListViewAndAdapter();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
    }

    private void initListViewAndAdapter() {
        adapter = new ShoppingCartAdapter(getContext(), R.layout.content_shopping_cart_item);
        listView.setAdapter(adapter);
        adapter.setOnShoppingCatrAmountChangeListener(new ShoppingCartAdapter.OnShoppingCatrAmountChangeListener() {
            @Override
            public void onAmountCanged(ShoppingCart data, int amount) {
                updateAmount(data, amount);
            }
        });
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
    }

    private void initProgress() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("处理中，请稍等...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    private void refreshData() {
        refreshLayout.setRefreshing(true);
        new ShoppingCartModel().getShoppingCartsByUserId(refreshCallback, new Pager(), getUseId());
    }

    private void loadMoreData() {
        new ShoppingCartModel().getShoppingCartsByUserId(loadMoreCallback, pageData.getNextPage(), getUseId());
    }

    @Event({R.id.shopping_cart_buy_view, R.id.shopping_cart_delete_view})
    private void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.shopping_cart_buy_view:
                if (isCheckedEmpty()) {
                    showMsg(view, "请选择需要购买的商品");
                    break;
                }
                break;
            case R.id.shopping_cart_delete_view:
                if (isCheckedEmpty()) {
                    showMsg(view, "请选择需要删除的数据");
                    break;
                }
                new AlertDialog.Builder(getContext())
                        .setMessage("确定要删除选中的数据吗？")
                        .setNegativeButton(R.string.cancel, null)
                        .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteShoppingCarts();
                            }
                        })
                        .show();
                break;
        }
    }

    /**
     * 删除选中的数据
     */
    private void deleteShoppingCarts() {
        String cartIds = "";
        for (ShoppingCart cart : adapter.getCheckedCarts()) {
            cartIds += cart.getShoppingCartId() + "*";
        }
        if (!StringUtils.isEmpty(cartIds)) {
            cartIds = cartIds.substring(0, cartIds.length() - 1);
        }
        progressDialog.show();
        new ShoppingCartModel().batchDeleteShoppingCarts(deleteCallback, cartIds);
    }

    private void updateAmount(ShoppingCart cart, int amount) {
        progressDialog.show();
        new ShoppingCartModel().updateAmount(new UpdateAmountCallback(cart, amount), cart.getShoppingCartId(), amount);
    }

    /**
     * 检查选中数据是否为空
     *
     * @return
     */
    private boolean isCheckedEmpty() {
        return this.adapter.getCheckedCarts().isEmpty();
    }
}
