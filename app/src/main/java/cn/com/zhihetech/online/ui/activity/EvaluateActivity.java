package cn.com.zhihetech.online.ui.activity;

import android.os.Bundle;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;

import org.xutils.view.annotation.ContentView;

import java.util.List;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.OrderDetail;
import cn.com.zhihetech.online.core.http.ArrayCallback;
import cn.com.zhihetech.online.core.util.StringUtils;
import cn.com.zhihetech.online.core.view.ZhiheProgressDialog;
import cn.com.zhihetech.online.model.OrderDetailModel;

/**
 * Created by ShenYunjie on 2016/2/18.
 */
@ContentView(R.layout.activity_evaluate)
public class EvaluateActivity extends BaseActivity {

    public static final String ORDER_ID_KEY = "_order_id";

    private ZhiheProgressDialog progressDialog;

    private String orderId = null;

    private ArrayCallback<OrderDetail> orderDetailCallback = new ArrayCallback<OrderDetail>() {
        @Override
        public void onArray(List<OrderDetail> datas) {
            log(JSONObject.toJSONString(datas));
        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            super.onError(ex, isOnCallback);
            appError(false);
        }

        @Override
        public void onFinished() {
            progressDialog.dismiss();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orderId = getIntent().getStringExtra(ORDER_ID_KEY);
        initViewAndData();
    }

    private void initViewAndData() {
        initProgress();
        loadOrderDetail();
    }

    private void initProgress() {
        progressDialog = new ZhiheProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.data_loading));
    }

    private void loadOrderDetail() {
        if (StringUtils.isEmpty(orderId)) {
            appError(true);
            return;
        }
        progressDialog.show();
        new OrderDetailModel().getOrderDetailByOrderId(orderDetailCallback, orderId);
    }

    private void appError(boolean finished) {
        showMsg("运行出错了");
        if (finished) {
            finish();
        }
    }
}
