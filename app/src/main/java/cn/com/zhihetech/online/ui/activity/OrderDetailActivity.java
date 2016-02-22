package cn.com.zhihetech.online.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.Goods;
import cn.com.zhihetech.online.bean.OrderDetail;
import cn.com.zhihetech.online.bean.OrderDetailGroup;
import cn.com.zhihetech.online.core.adapter.OrderDetailAdapter;
import cn.com.zhihetech.online.core.adapter.OrderDetailGroupAdapter;
import cn.com.zhihetech.online.core.http.ArrayCallback;
import cn.com.zhihetech.online.core.util.StringUtils;
import cn.com.zhihetech.online.core.view.ZhiheProgressDialog;
import cn.com.zhihetech.online.model.OrderDetailModel;
import cn.com.zhihetech.online.model.OrderModel;

/**
 * Created by ShenYunjie on 2016/1/28.
 */
@ContentView(R.layout.activity_order_detail)
public class OrderDetailActivity extends BaseActivity {

    public final static String ORDER_ID_KEY = "_ORDER_ID";

    @ViewInject(R.id.order_detail_lv)
    private ListView orderDetailListView;

    private String orderId;
    private OrderDetailAdapter adapter;
    private ZhiheProgressDialog progressDialog;

    private ArrayCallback<OrderDetail> orderDetailCallback = new ArrayCallback<OrderDetail>() {
        @Override
        public void onArray(List<OrderDetail> datas) {
            adapter.refreshData(datas);
        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            if (!isOnCallback) {
                showMsg("数据加载失败");
            }
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
        if (StringUtils.isEmpty(orderId)) {
            showMsg("出错了");
            finish();
            return;
        }
        initViewAndData();
    }

    private void initViewAndData() {
        adapter = new OrderDetailAdapter(this, R.layout.content_order_detail_item);
        orderDetailListView.setAdapter(adapter);
        orderDetailListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Goods goods = adapter.getItem(position).getGoods();
                Intent intent = new Intent(getSelf(), GoodsInfoActivity.class);
                intent.putExtra(GoodsInfoActivity.GOODS_ID_KEY, goods.getGoodsId());
                intent.putExtra(GoodsInfoActivity.GOODS_NAME_KEY, goods.getGoodsName());
                startActivity(intent);
            }
        });
        initAndShowProgress();
        new OrderDetailModel().getOrderDetailByOrderId(orderDetailCallback, orderId);
    }

    private void initAndShowProgress() {
        progressDialog = new ZhiheProgressDialog(this);
        progressDialog.setMessage(getString(R.string.data_loading));
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
}
