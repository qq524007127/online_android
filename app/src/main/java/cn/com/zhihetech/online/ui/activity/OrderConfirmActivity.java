package cn.com.zhihetech.online.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.pingplusplus.android.PaymentActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.ChargeInfo;
import cn.com.zhihetech.online.bean.Order;
import cn.com.zhihetech.online.bean.OrderDetail;
import cn.com.zhihetech.online.bean.OrderDetailGroup;
import cn.com.zhihetech.online.bean.ReceivedGoodsAddress;
import cn.com.zhihetech.online.bean.User;
import cn.com.zhihetech.online.core.adapter.OrderDetailGroupAdapter;
import cn.com.zhihetech.online.core.common.ResponseMessage;
import cn.com.zhihetech.online.core.common.ResponseStateCode;
import cn.com.zhihetech.online.core.http.ResponseMessageCallback;
import cn.com.zhihetech.online.core.view.OrderReceiptAddressView;
import cn.com.zhihetech.online.model.OrderModel;
import cn.com.zhihetech.online.ui.fragment.ShoppingCartFragment;
import de.greenrobot.event.EventBus;

/**
 * Created by ShenYunjie on 2016/1/26.
 */
@ContentView(R.layout.activity_order_confirm)
public class OrderConfirmActivity extends BaseActivity {

    public final static int REQUEST_CODE_PAYMENT = 0x001;
    public final static String ORDER_DETAILS_KEY = "_order_details_key";

    @ViewInject(R.id.order_confirm_detail_lv)
    private ListView detailLv;
    @ViewInject(R.id.order_confirm_total_tv)
    private TextView totalPriceTv;
    @ViewInject(R.id.order_confirm_submit_btn)
    private Button submitBtn;
    @ViewInject(R.id.pay_type_rg)
    private RadioGroup payTypeRg;

    private OrderReceiptAddressView addressView;    //收货地址
    private ProgressDialog progressDialog;

    private OrderDetailGroupAdapter adapter;
    private ArrayList<OrderDetail> details;
    private ArrayList<OrderDetailGroup> detailGroups = new ArrayList<>();

    /**
     * 购物车ID如果购买的为购物车中的商品则订单提交成功后需要将购物车中对应的数据删除
     */
    private List<String> shoppingCartIds;

    private float orderTotalPrice = 0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        details = (ArrayList<OrderDetail>) getIntent().getSerializableExtra(ORDER_DETAILS_KEY);
        if (getIntent().getSerializableExtra(ShoppingCartFragment.SHOPPING_CART_IDS_KEY) != null) {
            shoppingCartIds = (ArrayList<String>) getIntent().getSerializableExtra(ShoppingCartFragment.SHOPPING_CART_IDS_KEY);
        }
        initData();
        initViews();
    }

    public void onEvent(ReceivedGoodsAddress address) {
        addressView.bindReceiptAddress(address);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initData() {
        initDetailGroups(details);
        adapter = new OrderDetailGroupAdapter(this, detailGroups);
        initOrderTotalPrice();
    }

    private void initViews() {
        initAddressHeaderView();
        detailLv.setAdapter(adapter);
        totalPriceTv.setText("￥" + orderTotalPrice);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
    }

    private void initOrderTotalPrice() {
        for (int i = 0; i < adapter.getCount(); i++) {
            orderTotalPrice += adapter.getTotalPrice(i);
        }
    }

    /**
     * 根据订单详情列表初始化订单详情组列表
     *
     * @param details
     */
    private void initDetailGroups(ArrayList<OrderDetail> details) {
        if (details == null || details.isEmpty()) {
            return;
        }
        for (OrderDetail detail : details) {
            if (detailGroups.isEmpty()) {
                detailGroups.add(new OrderDetailGroup(detail));
            } else {
                boolean isAdd = false;
                for (OrderDetailGroup group : detailGroups) {
                    if (group.getMerchant().getMerchantId().equals(detail.getGoods().getMerchant().getMerchantId())) {
                        group.addOrderDetail(detail);
                        isAdd = true;
                        break;
                    }
                }
                if (!isAdd) {
                    detailGroups.add(new OrderDetailGroup(detail));
                }
            }
        }
    }

    private void initAddressHeaderView() {
        addressView = new OrderReceiptAddressView(this);
        detailLv.addHeaderView(addressView);
        addressView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getSelf(), ReceiptAddressActivity.class);
                intent.putExtra(ReceiptAddressActivity.REQUEST_RECEIPT_ADDRESS, ReceiptAddressActivity.REQUEST_RECEIPT_ADDRESS);
                startActivity(intent);
            }
        });
    }

    @Event({R.id.order_confirm_submit_btn})
    private void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.order_confirm_submit_btn:
                getChargeAndPay();
                break;
        }
    }

    private void getChargeAndPay() {
        List<Order> orders = createOrders();
        progressDialog.show();
        new OrderModel().getChargeWithOrders(orderCallback, orders);
    }

    private List<Order> createOrders() {
        List<Order> orders = new ArrayList<>();
        for (int i = 0; i < adapter.getCount(); i++) {
            Order order = createOrderByOrderDetailGroup(adapter.getItem(i));
            order.setCarriage(adapter.getTotalCarrige(i));
            order.setOrderTotal(adapter.getTotalPrice(i));
            order.setUserMsg(adapter.getUserMessage(i));
            switch (payTypeRg.getCheckedRadioButtonId()) {
                case R.id.wxpay_rb:
                    order.setPayChannel(ChargeInfo.PayChannel.WXPAY);
                    break;
                default:
                    order.setPayChannel(ChargeInfo.PayChannel.ALIPAY);
            }
            orders.add(order);
        }
        return orders;
    }

    /**
     * 根据一个订单详情组生成一个订单
     *
     * @param group
     * @return
     */
    private Order createOrderByOrderDetailGroup(OrderDetailGroup group) {
        Order order = new Order();
        order.setOrderName(group.getMerchant().getMerchName());
        order.setReceiptAddress(addressView.getReceiptAddress());
        order.setUser(new User(getUserId()));
        String[] goodsIds = new String[group.getOrderDetails().size()];
        int[] counts = new int[group.getOrderDetails().size()];
        float[] prices = new float[group.getOrderDetails().size()];
        for (int i = 0; i < group.getOrderDetails().size(); i++) {
            goodsIds[i] = group.getOrderDetails().get(i).getGoods().getGoodsId();
            counts[i] = group.getOrderDetails().get(i).getCount();
            prices[i] = group.getOrderDetails().get(i).getPrice();
        }
        order.createOrderDetailInfo(goodsIds, counts, prices);
        return order;
    }

    /**
     * 获取支付Charge之后回调，获取成功开始支付
     */
    private ResponseMessageCallback<String> orderCallback = new ResponseMessageCallback<String>() {

        @Override
        public void onResponseMessage(ResponseMessage<String> responseMessage) {
            if (responseMessage.getCode() == ResponseStateCode.SUCCESS) {
                if (shoppingCartIds != null && !shoppingCartIds.isEmpty()) {
                    EventBus.getDefault().post(shoppingCartIds);    //订单提交成功，通知购物车删除已提交订单的购物车数据
                }
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
            showMsg(submitBtn, "订单提交失败");
        }

        @Override
        public void onFinished() {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    };

    /**
     * 开始支付
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
                        showMsg(submitBtn, "支付失败");
                        break;
                    case "cancel":
                        showMsg(submitBtn, "已取消支付");
                        break;
                    case "invalid":
                        showMsg(submitBtn, "未检测到支付控件");
                        break;
                }
                String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
                String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息
            } else {
                showMsg(submitBtn, "未知错误！");
            }
        }
    }

    /**
     * 支付成功回调
     */
    private void onPaySuccess() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.tip)
                .setMessage("购买成功")
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .show();
    }
}
