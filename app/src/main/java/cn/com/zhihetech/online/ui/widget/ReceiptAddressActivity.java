package cn.com.zhihetech.online.ui.widget;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.io.Serializable;
import java.util.List;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.ReceivedGoodsAddress;
import cn.com.zhihetech.online.core.adapter.ReceiptAddressAdapter;
import cn.com.zhihetech.online.core.common.PageData;
import cn.com.zhihetech.online.core.common.ResponseMessage;
import cn.com.zhihetech.online.core.common.ResponseStateCode;
import cn.com.zhihetech.online.core.http.ObjectCallback;
import cn.com.zhihetech.online.core.http.PageDataCallback;
import cn.com.zhihetech.online.core.view.LoadMoreListView;
import cn.com.zhihetech.online.model.ReceiptAddressModel;

/**
 * 收货地址维护
 * Created by ShenYunjie on 2016/1/25.
 */
@ContentView(R.layout.activity_receipt_address)
public class ReceiptAddressActivity extends BaseActivity {

    private final int ADD_ADDRESS_REQUEST_CODE = 0x1;
    private final int EDIT_ADDRESS_REQUEST_CODE = 0x2;

    @ViewInject(R.id.receipt_address_lmlv)
    private LoadMoreListView listView;

    private ReceiptAddressAdapter adapter;

    /**
     * 加载回调
     */
    private PageDataCallback<ReceivedGoodsAddress> loadCallback = new PageDataCallback<ReceivedGoodsAddress>() {
        @Override
        public void onPageData(PageData<ReceivedGoodsAddress> result, List<ReceivedGoodsAddress> rows) {
            adapter.refreshData(rows);
        }
    };

    /**
     * 删除回调
     */
    private ObjectCallback<ResponseMessage> deleteCallback = new ObjectCallback<ResponseMessage>() {
        @Override
        public void onObject(ResponseMessage data) {
            if (data.getCode() == ResponseStateCode.SUCCESS) {
                loadData();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViewsAndData();
    }

    private void initViewsAndData() {
        adapter = new ReceiptAddressAdapter(this, R.layout.content_receipt_address_item);
        listView.setAdapter(adapter);
        adapter.setOnButtonCickListener(new ReceiptAddressAdapter.OnButtonCickListener() {
            @Override
            public void onEditClick(ReceivedGoodsAddress address) {
                Intent intent = new Intent(ReceiptAddressActivity.this, AddOrEditReceiptActivity.class);
                intent.putExtra(AddOrEditReceiptActivity.ADDRESS_KEY, address);
                startActivityForResult(intent, EDIT_ADDRESS_REQUEST_CODE);
            }

            @Override
            public void onDeleteClick(ReceivedGoodsAddress address) {
                new ReceiptAddressModel().deleteReceiptAddressBiId(deleteCallback, address.getAddressId());
            }
        });
        loadData();
    }

    private void loadData() {
        new ReceiptAddressModel().getReceiptAdressesByUserId(loadCallback, getUserId());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_receipt, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.receipt_address_menu_add:
                Intent intent = new Intent(this, AddOrEditReceiptActivity.class);
                startActivityForResult(intent, ADD_ADDRESS_REQUEST_CODE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_ADDRESS_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null && data.getSerializableExtra(AddOrEditReceiptActivity.ADDRESS_KEY) != null) {
                Serializable obj = data.getSerializableExtra(AddOrEditReceiptActivity.ADDRESS_KEY);
                if (obj instanceof ReceivedGoodsAddress) {
                    adapter.addData((ReceivedGoodsAddress) obj);
                }
            }
        } else if (requestCode == EDIT_ADDRESS_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            loadData();
        }
    }
}
