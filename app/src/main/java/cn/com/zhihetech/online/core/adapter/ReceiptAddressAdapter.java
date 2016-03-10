package cn.com.zhihetech.online.core.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.xutils.view.annotation.ViewInject;

import java.text.MessageFormat;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.ReceivedGoodsAddress;

/**
 * Created by ShenYunjie on 2016/1/25.
 */
public class ReceiptAddressAdapter extends ZhiheAdapter<ReceivedGoodsAddress, ReceiptAddressAdapter.ReceiptAddressHolder> {

    private OnButtonClickListener onButtonClickListener;

    public ReceiptAddressAdapter(Context mContext, int layoutId) {
        super(mContext, layoutId);
    }

    @Override
    public ReceiptAddressHolder onCreateViewHolder(View itemView) {
        return new ReceiptAddressHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ReceiptAddressHolder holder, final ReceivedGoodsAddress data) {
        holder.defaultFlagTv.setVisibility(data.isDefaultAddress() ? View.VISIBLE : View.GONE);
        holder.receiverNameTv.setText(MessageFormat.format(mContext.getString(R.string.receiver_person_name), data.getReceiverName()));
        holder.receiverTellTv.setText(MessageFormat.format(mContext.getString(R.string.contact_num), data.getReceiverPhone()));
        holder.detailAddress.setText(MessageFormat.format(mContext.getString(R.string.detail_address), data.getDetailAddress()));
        if (onButtonClickListener != null) {
            holder.editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onButtonClickListener.onEditClick(data);
                }
            });
            holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(mContext)
                            .setTitle(R.string.tip)
                            .setMessage("确定要删除此收货地址吗，删除后不可恢复？")
                            .setNegativeButton(R.string.cancel, null)
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    onButtonClickListener.onDeleteClick(data);
                                }
                            }).show();
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onButtonClickListener.onItemClick(data);
                }
            });
        }
    }

    @Override
    public void update(ReceivedGoodsAddress data) {
        for (int i = 0; i < mDatas.size(); i++) {
            ReceivedGoodsAddress address = mDatas.get(i);
            if (address.getAddressId().equals(data.getAddressId())) {
                mDatas.remove(i);
                mDatas.add(i, data);
                notifyDataSetChanged();
                return;
            }
        }
    }

    public void setOnButtonClickListener(OnButtonClickListener onButtonClickListener) {
        this.onButtonClickListener = onButtonClickListener;
    }

    public class ReceiptAddressHolder extends ZhiheAdapter.BaseViewHolder {

        @ViewInject(R.id.receipt_address_default_flag_tv)
        public TextView defaultFlagTv;
        @ViewInject(R.id.receipt_receiver_name_tv)
        public TextView receiverNameTv;
        @ViewInject(R.id.receipt_tell_tv)
        public TextView receiverTellTv;
        @ViewInject(R.id.receipt_detail_address_tv)
        public TextView detailAddress;
        @ViewInject(R.id.receipt_address_edit_btn)
        public Button editBtn;
        @ViewInject(R.id.receipt_address_delete_btn)
        public Button deleteBtn;

        public ReceiptAddressHolder(View itemView) {
            super(itemView);
        }
    }

    public interface OnButtonClickListener {
        void onEditClick(ReceivedGoodsAddress address);

        void onDeleteClick(ReceivedGoodsAddress address);

        void onItemClick(ReceivedGoodsAddress address);
    }
}
