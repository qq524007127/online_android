package cn.com.zhihetech.online.ui.widget;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.Serializable;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.ReceivedGoodsAddress;
import cn.com.zhihetech.online.bean.User;
import cn.com.zhihetech.online.core.common.ResponseMessage;
import cn.com.zhihetech.online.core.common.ResponseStateCode;
import cn.com.zhihetech.online.core.http.ResponseMessageCallback;
import cn.com.zhihetech.online.core.util.StringUtils;
import cn.com.zhihetech.online.model.ReceiptAddressModel;

/**
 * Created by ShenYunjie on 2016/1/25.
 */
@ContentView(R.layout.activity_add_or_edit_receipt_address)
public class AddOrEditReceiptActivity extends BaseActivity {

    public final static String ADDRESS_KEY = "_address";

    private ReceivedGoodsAddress address;

    @ViewInject(R.id.receipt_person_name_et)
    private EditText personNameEt;
    @ViewInject(R.id.receipt_tell_num_et)
    private EditText tellNumEt;
    @ViewInject(R.id.receipt_detail_address_et)
    private EditText detailAddressEt;
    @ViewInject(R.id.receipt_info_submit_btn)
    private Button submitBtn;

    private ResponseMessageCallback<ReceivedGoodsAddress> callback = new ResponseMessageCallback<ReceivedGoodsAddress>() {
        @Override
        public void onResponseMessage(ResponseMessage<ReceivedGoodsAddress> responseMessage) {
            if (responseMessage.getCode() == ResponseStateCode.SUCCESS) {
                Intent intent = new Intent();
                intent.putExtra(ADDRESS_KEY, responseMessage.getData());
                setResult(Activity.RESULT_OK, intent);
                finish();
            } else {
                showMsg(submitBtn, responseMessage.getMsg());
            }
        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            showMsg(submitBtn, "操作失败，请重试");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Serializable obj = getIntent().getSerializableExtra(ADDRESS_KEY);
        if (obj != null && obj instanceof ReceivedGoodsAddress) {
            address = (ReceivedGoodsAddress) obj;
        }
        super.onCreate(savedInstanceState);
        if (address != null) {
            bindData();
        }
    }

    @Override
    protected CharSequence getToolbarTile() {
        if (address != null) {
            return "收货地址修改";
        }
        return super.getToolbarTile();
    }

    private void bindData() {
        personNameEt.setText(address.getReceiverName());
        tellNumEt.setText(address.getReceiverPhone());
        detailAddressEt.setText(address.getDetailAddress());
    }

    @Event({R.id.receipt_info_submit_btn})
    private void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.receipt_info_submit_btn:
                initAndSubmit();
                break;
        }
    }

    private void initAndSubmit() {
        String personName = personNameEt.getText().toString();
        String tellNum = tellNumEt.getText().toString();
        String detail = detailAddressEt.getText().toString();
        if (StringUtils.isEmpty(personName)) {
            personNameEt.setError("收货人姓名不能为空");
            return;
        }
        if (StringUtils.isEmpty(tellNum)) {
            tellNumEt.setError("收货人联系电话不能为空");
            return;
        }
        if (StringUtils.isEmpty(detail)) {
            detailAddressEt.setError("收货地址不能为空");
            return;
        }
        if (address == null) {
            address = new ReceivedGoodsAddress();
        }
        User user = new User();
        user.setUserId(getUserId());
        address.setReceiverName(personName);
        address.setDetailAddress(detail);
        address.setReceiverPhone(tellNum);
        address.setUser(user);
        new ReceiptAddressModel().updateOrSaveReceiptAddress(callback, address);
    }
}
