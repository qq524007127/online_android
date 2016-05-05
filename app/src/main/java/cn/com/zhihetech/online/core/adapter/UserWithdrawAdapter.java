package cn.com.zhihetech.online.core.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import org.xutils.view.annotation.ViewInject;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.UserWithdraw;
import cn.com.zhihetech.online.core.util.DateUtils;

/**
 * Created by ShenYunjie on 2016/3/14.
 */
public class UserWithdrawAdapter extends ZhiheAdapter<UserWithdraw, UserWithdrawAdapter.UserWithDrawHolder> {

    public UserWithdrawAdapter(Context mContext) {
        super(mContext, R.layout.content_take_money_result_item);
    }

    @Override
    public UserWithDrawHolder onCreateViewHolder(View itemView) {
        return new UserWithDrawHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UserWithDrawHolder holder, UserWithdraw data) {
        holder.reallyMoneyTv.setText("实际到账金额：" + data.getRealMoney());
        holder.stateTv.setText(data.getDisplayState());
        holder.withDrawMoneyTv.setText("申请提现金额：" + data.getMoney());
        holder.proceMoneyTv.setText("手续费：" + data.getPoundage());
        holder.alipayCodeTv.setText("提现支付宝账号：" + data.getAliCode());
        holder.withDrawDateTv.setText("申请时间：" + DateUtils.formatDateTime(data.getApplyDate()));
        if (data.getWithdrawState() == UserWithdraw.WITHDRAW_ERR) {
            holder.failReasonTv.setVisibility(View.VISIBLE);
            holder.failReasonTv.setText(data.getReason());
        } else {
            holder.failReasonTv.setVisibility(View.GONE);
        }
    }

    public class UserWithDrawHolder extends ZhiheAdapter.BaseViewHolder {

        @ViewInject(R.id.with_draw_really_money_tv)
        private TextView reallyMoneyTv;
        @ViewInject(R.id.with_draw_state_tv)
        private TextView stateTv;
        @ViewInject(R.id.with_draw_money_tv)
        private TextView withDrawMoneyTv;
        @ViewInject(R.id.with_draw_proc_money_tv)
        private TextView proceMoneyTv;
        @ViewInject(R.id.with_draw_alipay_code_tv)
        private TextView alipayCodeTv;
        @ViewInject(R.id.with_draw_date_tv)
        private TextView withDrawDateTv;
        @ViewInject(R.id.with_draw_fail_reason_tv)
        private TextView failReasonTv;

        public UserWithDrawHolder(View itemView) {
            super(itemView);
        }
    }
}
