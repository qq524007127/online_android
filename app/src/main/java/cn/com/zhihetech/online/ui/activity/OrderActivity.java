package cn.com.zhihetech.online.ui.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.pingplusplus.android.PaymentActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.Order;
import cn.com.zhihetech.online.core.adapter.OrderAdapter;
import cn.com.zhihetech.online.core.common.Constant;
import cn.com.zhihetech.online.core.common.PageData;
import cn.com.zhihetech.online.core.common.Pager;
import cn.com.zhihetech.online.core.common.ResponseMessage;
import cn.com.zhihetech.online.core.common.ResponseStateCode;
import cn.com.zhihetech.online.core.http.ObjectCallback;
import cn.com.zhihetech.online.core.http.PageDataCallback;
import cn.com.zhihetech.online.core.http.ResponseMessageCallback;
import cn.com.zhihetech.online.core.view.LoadMoreListView;
import cn.com.zhihetech.online.core.view.OnLoadMoreListener;
import cn.com.zhihetech.online.core.view.ZhiheProgressDialog;
import cn.com.zhihetech.online.core.view.ZhiheSwipeRefreshLayout;
import cn.com.zhihetech.online.model.OrderModel;

/**
 * Created by ShenYunjie on 2016/1/27.
 */
@ContentView(R.layout.activity_orders)
public class OrderActivity extends BaseActivity implements OrderAdapter.OnOrderItemClickListener {

    public final static String ORDER_STATE_KEY = "_order_state_key";
    public final static int REQUEST_CODE_PAYMENT = 0x001;   //请求调用支付控件
    public final static int REQUEST_CODE_EVALUATE = 0X002;    //评价

    @ViewInject(R.id.order_zsrl)
    private ZhiheSwipeRefreshLayout refreshLayout;
    @ViewInject(R.id.order_lv)
    private LoadMoreListView orderListView;

    private ZhiheProgressDialog progressDialog;

    private PageData<Order> pageData;
    private OrderAdapter adapter;

    private int orderState = 0;

    /**
     * 订单加载回调
     */
    private PageDataCallback<Order> loadMoreCallback = new PageDataCallback<Order>() {
        @Override
        public void onPageData(PageData<Order> result, List<Order> rows) {
            pageData = result;
            adapter.addDatas(rows);
        }

        @Override
        public void onFinished() {
            super.onFinished();
            orderListView.loadComplete();
        }
    };

    /**
     * 订单刷新回调
     */
    private PageDataCallback<Order> refreshCallback = new PageDataCallback<Order>() {
        @Override
        public void onPageData(PageData<Order> result, List<Order> rows) {
            pageData = result;
            adapter.refreshData(rows);
        }

        @Override
        public void onFinished() {
            super.onFinished();
            refreshLayout.setRefreshing(false);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orderState = getIntent().getIntExtra(ORDER_STATE_KEY, 0);
        initViewAndData();
    }

    private void initViewAndData() {
        adapter = new OrderAdapter(this);
        orderListView.setAdapter(adapter);
        adapter.setItemClickListener(this);
        refreshLayout.setEnabled(false);
        orderListView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public boolean checkCanDoLoad() {
                return pageData != null && pageData.hasNextPage() && !refreshLayout.isRefreshing();
            }

            @Override
            public void onStartLoad() {
                loadMoreData();
            }
        });
        initProgressDialog();
        refreshData();
    }

    private void initProgressDialog() {
        progressDialog = new ZhiheProgressDialog(this);
        progressDialog.setMessage(getString(R.string.data_executing));
        progressDialog.setCancelable(false);
    }

    /**
     * 刷新数据（重新加载）
     */
    private void refreshData() {
        pageData = null;
        Pager pager = new Pager();
        refreshLayout.setRefreshing(true);
        new OrderModel().getOrdersByUserId(refreshCallback, pager, getUserId(), orderState);
    }

    /**
     * 加载更多数据
     */
    private void loadMoreData() {
        new OrderModel().getOrdersByUserId(loadMoreCallback, pageData.getNextPage(), getUserId(), orderState);
    }

    /**
     * 取消订单（只有为支付订单才有此功能）
     *
     * @param order
     * @param view
     */
    @Override
    public void onCancelClick(final Order order, final View view) {
        if (order.getOrderState() != Constant.ORDER_STATE_NO_PAYMENT) {
            showMsg(view, "只有未付款的订单才支持此操作");
            return;
        }
        new AlertDialog.Builder(this)
                .setTitle(R.string.tip)
                .setMessage("确定要取消此订单吗？")
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doCancelOrder(view, order);
                    }
                }).show();
    }

    private void doCancelOrder(final View view, Order order) {
        progressDialog.show();
        new OrderModel().cancelOrderByOrderId(new ObjectCallback<ResponseMessage>() {
            @Override
            public void onObject(ResponseMessage data) {
                if (data.getCode() == ResponseStateCode.SUCCESS) {
                    showMsg(view, "订单已取消");
                    refreshData();
                } else {
                    showMsg(view, data.getMsg());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                showMsg(view, "订单取消失败");
            }

            @Override
            public void onFinished() {
                progressDialog.dismiss();
            }
        }, order.getOrderId());
    }

    /**
     * 订单支付（只有为支付订单才有此功能）
     *
     * @param order
     * @param view
     */
    @Override
    public void onPayClick(final Order order, final View view) {
        if (order.getOrderState() != Constant.ORDER_STATE_NO_PAYMENT) {
            showMsg(view, "只有未付款的订单才支持此操作");
            return;
        }
        new AlertDialog.Builder(this)
                .setTitle(R.string.tip)
                .setMessage("确定要支付此订单吗？")
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doPayOrder(view, order);
                    }
                }).show();
    }

    private void doPayOrder(final View view, Order order) {
        progressDialog.show();
        new OrderModel().getChargeByOrderId(new ResponseMessageCallback<String>() {
            @Override
            public void onResponseMessage(ResponseMessage<String> responseMessage) {
                if (responseMessage.getCode() == ResponseStateCode.SUCCESS) {
                    doPay(responseMessage.getData());
                } else {
                    new AlertDialog.Builder(getSelf()).setTitle(R.string.tip)
                            .setMessage(responseMessage.getMsg())
                            .setPositiveButton(R.string.ok, null)
                            .show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                showMsg(view, "操作失败，请稍后再试");
            }

            @Override
            public void onFinished() {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        }, order.getOrderId());
    }

    /**
     * 申请退款回调
     *
     * @param order
     * @param view
     */
    @Override
    public void onRefundClick(final Order order, final View view) {

        if (order.getOrderState() != Constant.ORDER_STATE_NO_DISPATCHER) {
            showMsg(view, "只有已付款且未发货的订单才能退款，若要退款请与商家联系");
            return;
        }
        new AlertDialog.Builder(this)
                .setTitle(R.string.tip)
                .setMessage("确定要申请退款吗？")
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doRefundOrder(view, order);
                    }
                }).show();
    }

    private void doRefundOrder(final View view, Order order) {
        progressDialog.show();
        new OrderModel().refundByOrderId(new ObjectCallback<ResponseMessage>() {
            @Override
            public void onObject(ResponseMessage data) {
                if (data.getCode() == ResponseStateCode.SUCCESS) {
                    showMsg(view, "申请退款成功，请等待商家确认！");
                    refreshData();
                } else {
                    new AlertDialog.Builder(getSelf())
                            .setTitle(R.string.tip)
                            .setMessage(data.getMsg())
                            .setPositiveButton(R.string.ok, null)
                            .show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                showMsg(view, "申请退款失败，请稍后重试！");
            }

            @Override
            public void onFinished() {
                progressDialog.dismiss();
            }
        }, order.getOrderId());
    }

    /**
     * 签收回调
     *
     * @param order
     * @param view
     */
    @Override
    public void onReceiptClick(final Order order, final View view) {
        if (order.getOrderState() != Constant.ORDER_STATE_ALREADY_DISPATCHER) {
            showMsg(view, "只有已发货的商品才可进行此操作！");
            return;
        }
        new AlertDialog.Builder(this)
                .setTitle(R.string.tip)
                .setMessage("确定要签收此订单吗，请确认收到货后才进行此操作？")
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doReceivedOrder(view, order);
                    }
                }).show();
    }

    private void doReceivedOrder(final View view, Order order) {
        progressDialog.show();
        new OrderModel().orderReceipt(new ObjectCallback<ResponseMessage>() {
            @Override
            public void onObject(ResponseMessage data) {
                if (data.getCode() == ResponseStateCode.SUCCESS) {
                    showMsg(view, "订单签收成功，请勿完了给予商家评价哦！");
                    refreshData();
                } else {
                    new AlertDialog.Builder(getSelf())
                            .setTitle(R.string.tip)
                            .setMessage(data.getMsg())
                            .setPositiveButton(R.string.ok, null)
                            .show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                showMsg(view, "签收失败，请重试");
            }

            @Override
            public void onFinished() {
                super.onFinished();
                progressDialog.dismiss();
            }
        }, order.getOrderId());
    }

    @Override
    public void onEvaluateClick(Order order, View view) {
        if (order.getOrderState() != Constant.ORDER_STATE_ALREADY_DELIVER) {
            showMsg(view, "只有已签收的商品才可已进行此操作！");
            return;
        }
        Intent intent = new Intent(this, EvaluateActivity.class);
        intent.putExtra(EvaluateActivity.ORDER_ID_KEY, order.getOrderId());
        startActivityForResult(intent, REQUEST_CODE_EVALUATE);
    }

    @Override
    public void onDeleteClick(final Order order, final View view) {
        int state = order.getOrderState();
        if (state != Constant.ORDER_STATE_ALREADY_CANCEL && state != Constant.ORDER_STATE_ALREADY_REFUND && state != Constant.ORDER_STATE_ALREADY_EVALUATE) {
            showMsg(view, "此订单不能删除");
            return;
        }
        new AlertDialog.Builder(this)
                .setTitle(R.string.tip)
                .setMessage("确定要删除此订单吗？")
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doDeleteOrder(view, order);
                    }
                }).show();
    }

    private void doDeleteOrder(final View view, Order order) {
        progressDialog.show();
        new OrderModel().orderDelete(new ObjectCallback<ResponseMessage>() {
            @Override
            public void onObject(ResponseMessage data) {
                if (data.getCode() != ResponseStateCode.SUCCESS) {
                    showMsg(view, data.getMsg());
                    return;
                }
                showMsg(view, "订单删除成功");
                refreshData();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                showMsg(view, "订单删除失败！");
            }

            @Override
            public void onFinished() {
                super.onFinished();
                progressDialog.dismiss();
            }
        }, order.getOrderId());
    }

    /**
     * 调用支付控件开始支付
     *
     * @param chargeInfo
     */
    private void doPay(String chargeInfo) {
        Intent intent = new Intent();
        String packageName = getPackageName();
        ComponentName componentName = new ComponentName(packageName, packageName + ".wxapi.WXPayEntryActivity");
        intent.setComponent(componentName);
        intent.putExtra(PaymentActivity.EXTRA_CHARGE, chargeInfo);
        startActivityForResult(intent, REQUEST_CODE_PAYMENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //支付页面返回处理
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getExtras().getString("pay_result");
                switch (result) {
                    case "success":
                        onPaySuccess();
                        break;
                    case "fail":
                        showMsg("支付失败");
                        break;
                    case "cancel":
                        showMsg("已取消支付");
                        break;
                    case "invalid":
                        showMsg("未检测到支付控件");
                        break;
                }
            } else {
                showMsg("未知错误！");
            }
        } else if (requestCode == REQUEST_CODE_EVALUATE && resultCode == Activity.RESULT_OK) {
            refreshData();
        }
    }

    /**
     * 支付成功回调
     */
    private void onPaySuccess() {
        showMsg("支付成功");
        refreshData();
    }
}
