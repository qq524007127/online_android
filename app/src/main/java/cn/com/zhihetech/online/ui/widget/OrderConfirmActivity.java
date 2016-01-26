package cn.com.zhihetech.online.ui.widget;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.pingplusplus.android.PaymentActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.ChargeInfo;
import cn.com.zhihetech.online.bean.Goods;
import cn.com.zhihetech.online.bean.Order;
import cn.com.zhihetech.online.bean.OrderConfirm;
import cn.com.zhihetech.online.bean.OrderDetail;
import cn.com.zhihetech.online.bean.User;
import cn.com.zhihetech.online.core.adapter.OrderConfirmAdapter;
import cn.com.zhihetech.online.core.common.ResponseMessage;
import cn.com.zhihetech.online.core.http.ResponseMessageCallback;
import cn.com.zhihetech.online.model.OrderModel;

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

    private OrderConfirmAdapter adapter;
    private ArrayList<OrderDetail> details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        details = (ArrayList<OrderDetail>) getIntent().getSerializableExtra(ORDER_DETAILS_KEY);
        initViews();
    }

    private void initViews() {
        adapter = new OrderConfirmAdapter(this, details);
        detailLv.setAdapter(adapter);
    }

    @Event({R.id.order_confirm_submit_btn})
    private void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.order_confirm_submit_btn:
                getChargeInfo();
                break;
        }
    }

    private void getChargeInfo() {
        List<Order> orders = new ArrayList<>();
        for (OrderDetail detail : details) {
            Order order = testOrder(detail.getGoods(), detail.getCount());
            order.setPayChannel(ChargeInfo.PayChannel.ALIPAY);
            orders.add(order);
        }
        new OrderModel().getChargeWithOrders(orderCallback, orders);
    }

    private Order testOrder(Goods goods, int amount) {
        Order order = new Order(goods);
        order.createOrderDetailInfo(new String[]{goods.getGoodsId()}, new int[]{amount});
        User user = new User();
        user.setUserId(getUserId());
        order.setUser(user);
        order.setUserMsg("来自安卓的测试订单");
        order.setOrderTotal((float) (goods.getPrice() * amount));
        order.setReceiverName("aaaaa");
        order.setReceiverPhone("123456748912");
        order.setReceiverAdd("昆明市官渡区关上顺新时代");
        return order;
    }

    private ResponseMessageCallback<String> orderCallback = new ResponseMessageCallback<String>() {

        @Override
        public void onResponseMessage(ResponseMessage<String> responseMessage) {
            log(responseMessage.getData());
            payTest(responseMessage.getData());
        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            super.onError(ex, isOnCallback);
        }
    };

    private void payTest(String chargeInfo) {
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

                        break;
                    case "fail":

                        break;
                    case "cancel":

                        break;
                    case "invalid":

                        break;
                }
            /* 处理返回值
             * "success" - payment succeed
             * "fail"    - payment failed
             * "cancel"  - user canceld
             * "invalid" - payment plugin not installed
             *
             */
                String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
                String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息
            } else {
                //showMsg(buyView, "支付失败！");
            }
        }
    }
}
