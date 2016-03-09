package cn.com.zhihetech.online.core.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import org.xutils.view.annotation.ViewInject;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.RedEnvelopItem;

/**
 * Created by ShenYunjie on 2016/3/8.
 */
public class RedEnvelopItemAdapter extends ZhiheAdapter<RedEnvelopItem, RedEnvelopItemAdapter.RedEnvelopItemHolder> {

    public RedEnvelopItemAdapter(Context mContext) {
        super(mContext, R.layout.user_envelop_item_list_item);
    }

    @Override
    public RedEnvelopItemHolder onCreateViewHolder(View itemView) {
        return new RedEnvelopItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RedEnvelopItemHolder holder, RedEnvelopItem data) {
        holder.envelopItemAmoutTv.setText("￥ " + data.getAmountOfMoney());
        holder.envelopItemMrechName.setText(data.getRedEnvelop().getMerchant().getMerchName() + "发送的红包");
        if (data.isExtractState()) {
            holder.envelopItemSateTv.setText("已存入钱包");
        } else {
            holder.envelopItemSateTv.setText("未存入钱包");
        }
    }

    public class RedEnvelopItemHolder extends ZhiheAdapter.BaseViewHolder {

        @ViewInject(R.id.red_envelop_item_amount_tv)
        public TextView envelopItemAmoutTv;
        @ViewInject(R.id.red_envelop_item_name_tv)
        public TextView envelopItemMrechName;
        @ViewInject(R.id.red_envelop_item_state_tv)
        public TextView envelopItemSateTv;

        public RedEnvelopItemHolder(View itemView) {
            super(itemView);
        }
    }
}
