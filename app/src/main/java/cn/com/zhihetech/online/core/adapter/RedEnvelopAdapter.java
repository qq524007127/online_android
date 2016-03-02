package cn.com.zhihetech.online.core.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import org.xutils.view.annotation.ViewInject;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.RedEnvelop;

/**
 * Created by ShenYunjie on 2016/3/2.
 */
public class RedEnvelopAdapter extends ZhiheAdapter<RedEnvelop, RedEnvelopAdapter.RedEnvelopHolder> {

    public RedEnvelopAdapter(Context mContext) {
        super(mContext, R.layout.content_red_envelop_item);
    }

    @Override
    public RedEnvelopHolder onCreateViewHolder(View itemView) {
        return new RedEnvelopHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RedEnvelopHolder holder, RedEnvelop data) {
        holder.redEnvelopAmount.setText("红包总金额：" + data.getTotalMoney() + "元");
        holder.redEnvelopCount.setText("红包个数：" + data.getNumbers() + "个");
        holder.redEnvelopMsg.setText(data.getEnvelopMsg());
    }

    public class RedEnvelopHolder extends ZhiheAdapter.BaseViewHolder {

        @ViewInject(R.id.red_envelop_amount_tv)
        public TextView redEnvelopAmount;
        @ViewInject(R.id.red_envelop_count_tv)
        public TextView redEnvelopCount;
        @ViewInject(R.id.red_envelop_msg_tv)
        public TextView redEnvelopMsg;

        public RedEnvelopHolder(View itemView) {
            super(itemView);
        }
    }
}
