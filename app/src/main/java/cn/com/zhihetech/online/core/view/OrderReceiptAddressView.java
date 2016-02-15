package cn.com.zhihetech.online.core.view;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.ReceivedGoodsAddress;
import cn.com.zhihetech.online.core.common.ResponseMessage;
import cn.com.zhihetech.online.core.common.ResponseStateCode;
import cn.com.zhihetech.online.core.common.ZhiheApplication;
import cn.com.zhihetech.online.core.http.ResponseMessageCallback;
import cn.com.zhihetech.online.model.ReceiptAddressModel;
import cn.com.zhihetech.online.ui.activity.OrderConfirmActivity;
import cn.com.zhihetech.online.ui.activity.ReceiptAddressActivity;

/**
 * Created by ShenYunjie on 2016/1/27.
 */
public class OrderReceiptAddressView extends FrameLayout {
    @ViewInject(R.id.order_confirm_header_receipt_person_name_tv)
    private TextView personNameTv;
    @ViewInject(R.id.order_confirm_header_receipt_tell_tv)
    private TextView contactNumTv;
    @ViewInject(R.id.order_confirm_header_receipt_detail_info_tv)
    private TextView detailInfoTv;

    private ReceivedGoodsAddress address;
    ProgressDialog dialog;

    private ResponseMessageCallback<ReceivedGoodsAddress> callback = new ResponseMessageCallback<ReceivedGoodsAddress>() {
        @Override
        public void onResponseMessage(ResponseMessage<ReceivedGoodsAddress> responseMessage) {
            if (responseMessage.getCode() == ResponseStateCode.SUCCESS) {
                bindReceiptAddress(responseMessage.getData());
            } else if (responseMessage.getCode() == ResponseStateCode.NOT_FOUND) {
                new AlertDialog.Builder(getContext()).setTitle("提示")
                        .setMessage("您还没有收货地址，请先添加收货地址")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (getContext() instanceof OrderConfirmActivity) {
                                    ((OrderConfirmActivity) getContext()).finish();
                                }
                                Intent intent = new Intent(getContext(), ReceiptAddressActivity.class);
                                getContext().startActivity(intent);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            super.onError(ex, isOnCallback);
        }

        @Override
        public void onFinished() {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    };

    public OrderReceiptAddressView(Context context) {
        super(context);
        init();
    }

    private void init() {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.content_order_confirm_receipt_address, null);
        x.view().inject(this, contentView);
        addView(contentView);
        dialog = new ProgressDialog(getContext());
        dialog.setCancelable(false);
        loadDefaultAddress();
    }

    private void loadDefaultAddress() {
        dialog.show();
        new ReceiptAddressModel().getDefaultReceiptAdress(callback, ZhiheApplication.getInstance().getUserId());
    }

    public void bindReceiptAddress(ReceivedGoodsAddress address) {
        this.address = address;
        personNameTv.setText(this.address.getReceiverName());
        contactNumTv.setText(this.address.getReceiverPhone());
        detailInfoTv.setText(this.address.getDetailAddress());
    }

    public ReceivedGoodsAddress getReceiptAddress() {
        return this.address;
    }
}
