package cn.com.zhihetech.online.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.GoodsEvaluate;
import cn.com.zhihetech.online.bean.OrderDetail;
import cn.com.zhihetech.online.bean.OrderEvaluate;
import cn.com.zhihetech.online.core.adapter.EvaluateAdapter;
import cn.com.zhihetech.online.core.common.ResponseMessage;
import cn.com.zhihetech.online.core.common.ResponseStateCode;
import cn.com.zhihetech.online.core.http.ArrayCallback;
import cn.com.zhihetech.online.core.http.ObjectCallback;
import cn.com.zhihetech.online.core.util.StringUtils;
import cn.com.zhihetech.online.core.view.ZhiheProgressDialog;
import cn.com.zhihetech.online.model.OrderDetailModel;
import cn.com.zhihetech.online.model.OrderEvaluateModel;

/**
 * Created by ShenYunjie on 2016/2/18.
 */
@ContentView(R.layout.activity_evaluate)
public class EvaluateActivity extends BaseActivity {

    public static final String ORDER_ID_KEY = "_order_id";

    @ViewInject(R.id.order_detail_goods_list_view)
    private ListView orderGoodsListView;
    @ViewInject(R.id.evaluate_merchant_rb)
    private RatingBar merchantRb;
    @ViewInject(R.id.evaluate_submit_btn)
    private Button submitBtn;
    private ZhiheProgressDialog progressDialog;

    private EvaluateAdapter adapter;
    private String orderId = null;

    /**
     * 加载订单详情回调
     */
    private ArrayCallback<OrderDetail> orderDetailLoadCallback = new ArrayCallback<OrderDetail>() {
        @Override
        public void onArray(List<OrderDetail> datas) {
            adapter.refreshData(datas);
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

    /**
     * 订单评价回调
     */
    private ObjectCallback<ResponseMessage> orderEvaluateCallback = new ObjectCallback<ResponseMessage>() {
        @Override
        public void onObject(ResponseMessage data) {
            if (data.getCode() != ResponseStateCode.SUCCESS) {
                showMsg(submitBtn, data.getMsg());
                return;
            }
            showMsg("评价成功！");
            setResult(RESULT_OK);
            finish();
        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            super.onError(ex, isOnCallback);
            showMsg(submitBtn, "评价失败，请重试");
        }

        @Override
        public void onFinished() {
            super.onFinished();
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
        adapter = new EvaluateAdapter(this);
        orderGoodsListView.setAdapter(adapter);
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
        new OrderDetailModel().getOrderDetailByOrderId(orderDetailLoadCallback, orderId);
    }

    @Event(R.id.evaluate_submit_btn)
    private void onViewClick(View view) {
        orderEvaluate();
    }

    private void orderEvaluate() {
        OrderEvaluate orderEvaluate = new OrderEvaluate(orderId, merchantRb.getRating());
        for (int i = 0; i < adapter.getCount(); i++) {
            String goodsId = adapter.getItem(i).getGoods().getGoodsId();
            GoodsEvaluate goodsEvaluate = new GoodsEvaluate(goodsId, adapter.getGoodsScore(i), adapter.getGoodsEvaluate(i));
            orderEvaluate.addGoodsEvaluate(goodsEvaluate);
        }
        progressDialog.show();
        new OrderEvaluateModel().orderEvaluate(orderEvaluateCallback, orderEvaluate);
    }

    private void appError(boolean finished) {
        showMsg("运行出错了");
        if (finished) {
            finish();
        }
    }
}
