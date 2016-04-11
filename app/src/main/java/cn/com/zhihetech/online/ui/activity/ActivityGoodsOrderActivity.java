package cn.com.zhihetech.online.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.pingplusplus.android.PaymentActivity;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.Serializable;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.ActivityGoods;
import cn.com.zhihetech.online.bean.ChargeInfo;
import cn.com.zhihetech.online.bean.Goods;
import cn.com.zhihetech.online.bean.Order;
import cn.com.zhihetech.online.bean.ReceivedGoodsAddress;
import cn.com.zhihetech.online.core.ZhiheApplication;
import cn.com.zhihetech.online.core.common.ResponseMessage;
import cn.com.zhihetech.online.core.common.ResponseStateCode;
import cn.com.zhihetech.online.core.http.ResponseMessageCallback;
import cn.com.zhihetech.online.core.util.ImageLoader;
import cn.com.zhihetech.online.core.view.OrderReceiptAddressView;
import cn.com.zhihetech.online.model.OrderModel;
import de.greenrobot.event.EventBus;

/**
 * Created by ShenYunjie on 2016/3/15.
 */
@ContentView(R.layout.activity_seckill_goods_order)
public class ActivityGoodsOrderActivity extends BaseActivity {

    public final static int REQUEST_CODE_PAYMENT = 0x001;
    public final static String ACTIVITY_GOODS_KEY = "_ACTIVITY_GOODS_KEY";

    @ViewInject(R.id.order_receipt_address_tv)
    private OrderReceiptAddressView receiptAddressView;
    @ViewInject(R.id.activity_goods_cover_iv)
    private ImageView goodsCoverImg;
    @ViewInject(R.id.activity_goods_name_tv)
    private TextView goodsNameTv;
    @ViewInject(R.id.activity_goods_seckill_price_tv)
    private TextView seckillPriceTv;
    @ViewInject(R.id.activity_goods_stock_tv)
    private TextView seckillGoodsStockTv;
    @ViewInject(R.id.order_user_msg_et)
    private EditText orderUserMsg;
    @ViewInject(R.id.activity_goods_carriage_tv)
    private TextView goodsCarriageTv;
    @ViewInject(R.id.activity_goods_order_total_tv)
    private TextView goodsTotalPriceTv;
    @ViewInject(R.id.order_pay_type_rg)
    private RadioGroup payTypeRg;

    private ActivityGoods activityGoods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        Serializable obj = getIntent().getSerializableExtra(ACTIVITY_GOODS_KEY);
        if (obj == null || !(obj instanceof ActivityGoods)) {
            showMsg("出错了，请重试");
            finish();
            return;
        }
        activityGoods = (ActivityGoods) obj;
        initViewAndData();
    }

    public void onEvent(ReceivedGoodsAddress address) {
        receiptAddressView.bindReceiptAddress(address);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initViewAndData() {
        initViews();
    }

    private void initViews() {
        Goods goods = activityGoods.getGoods();
        ImageLoader.disPlayImage(goodsCoverImg, goods.getCoverImg());
        this.seckillPriceTv.setText("秒杀价:" + activityGoods.getActivityPrice());
        //this.seckillGoodsStockTv.setText("剩余:(" + goods.getCurrentStock() + ")");
        this.goodsCarriageTv.setText("邮费:" + goods.getCarriage());
        this.goodsTotalPriceTv.setText("合计:" + (goods.getCarriage() + activityGoods.getActivityPrice()));
        this.goodsNameTv.setText(goods.getGoodsName());
    }


    @Event({R.id.order_receipt_address_tv, R.id.submit_order_btn})
    private void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.order_receipt_address_tv:
                Intent intent = new Intent(getSelf(), ReceiptAddressActivity.class);
                intent.putExtra(ReceiptAddressActivity.REQUEST_RECEIPT_ADDRESS, ReceiptAddressActivity.REQUEST_RECEIPT_ADDRESS);
                startActivity(intent);
                break;
            case R.id.submit_order_btn:
                if (receiptAddressView.getReceiptAddress() == null) {
                    showMsg("请先选择收货地址");
                    return;
                }
                submitOrder(activityGoods);
                break;
        }
    }


    /**
     * 提交活动商品订单
     *
     * @param activityGoods
     */
    private void submitOrder(ActivityGoods activityGoods) {
        ReceivedGoodsAddress address = receiptAddressView.getReceiptAddress();
        Goods goods = activityGoods.getGoods();
        ChargeInfo.PayChannel payType;
        switch (payTypeRg.getCheckedRadioButtonId()) {
            case R.id.wxpay_rb:
                payType = ChargeInfo.PayChannel.WXPAY;
                break;
            default:
                payType = ChargeInfo.PayChannel.ALIPAY;
        }
        String userMsg = this.orderUserMsg.getText().toString();
        Order order = new Order();
        order.setPayChannel(payType);
        order.setUser(ZhiheApplication.getInstance().getLogedUser());
        order.setReceiptAddress(address);
        order.setUserMsg(userMsg);
        order.setCarriage(goods.getCarriage());
        order.setOrderTotal(activityGoods.getActivityPrice() + goods.getCarriage());
        order.setOrderName(goods.getGoodsName());
        final ProgressDialog progressDialog = ProgressDialog.show(this, "", "正在提交订单...");
        progressDialog.setCancelable(true);
        final Callback.Cancelable cancelable = new OrderModel().getActivityGoodsChargeByOrderAndActivityGoods(new ResponseMessageCallback<String>() {
            @Override
            public void onResponseMessage(ResponseMessage<String> responseMessage) {
                if (responseMessage.getCode() != ResponseStateCode.SUCCESS) {
                    showMsg(responseMessage.getMsg());
                    return;
                }
                doPay(responseMessage.getData());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                showMsg("支付失败，请重试！");
            }

            @Override
            public void onFinished() {
                progressDialog.dismiss();
            }
        }, order, activityGoods);
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
                        showMsg("支付失败");
                        break;
                    case "cancel":
                        showMsg("已取消支付");
                        break;
                    case "invalid":
                        showMsg("未检测到支付控件");
                        break;
                }
                String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
                String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息
            } else {
                showMsg("未知错误！");
            }
        }
    }

    /**
     * 支付成功回调
     */
    private void onPaySuccess() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.tip)
                .setMessage("秒杀成功！")
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .show();
    }
}
