package cn.com.zhihetech.online.core.adapter;

import android.content.Context;
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

    private OnButtonCickListener onButtonCickListener;

    public ReceiptAddressAdapter(Context mContext, int layoutId) {
        super(mContext, layoutId);
    }

    @Override
    public ReceiptAddressHolder onCreateViewHolder(View itemView) {
        return new ReceiptAddressHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ReceiptAddressHolder holder, final ReceivedGoodsAddress data) {
        holder.receiverNameTv.setText(MessageFormat.format(mContext.getString(R.string.receiver_person_name), data.getReceiverName()));
        holder.receiverTellTv.setText(MessageFormat.format(mContext.getString(R.string.contact_num), data.getReceiverPhone()));
        holder.detailAddress.setText(MessageFormat.format(mContext.getString(R.string.detail_address), data.getDetailAddress()));
        if (onButtonCickListener != null) {
            holder.editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onButtonCickListener.onEditClick(data);
                }
            });
            holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onButtonCickListener.onDeleteClick(data);
                }
            });
        }
    }

    public void setOnButtonCickListener(OnButtonCickListener onButtonCickListener) {
        this.onButtonCickListener = onButtonCickListener;
    }

    public class ReceiptAddressHolder extends ZhiheAdapter.BaseViewHolder {
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

    public interface OnButtonCickListener {
        void onEditClick(ReceivedGoodsAddress address);

        void onDeleteClick(ReceivedGoodsAddress address);
    }
}
