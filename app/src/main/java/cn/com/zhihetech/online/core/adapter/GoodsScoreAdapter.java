package cn.com.zhihetech.online.core.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import org.xutils.view.annotation.ViewInject;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.GoodsScore;
import cn.com.zhihetech.online.core.util.DateUtils;

/**
 * Created by ShenYunjie on 2016/3/16.
 */
public class GoodsScoreAdapter extends ZhiheAdapter<GoodsScore, GoodsScoreAdapter.GoodsScoreHolder> {

    public GoodsScoreAdapter(Context mContext) {
        super(mContext, R.layout.content_goods_comment_item);
    }

    @Override
    public GoodsScoreHolder onCreateViewHolder(View itemView) {
        return new GoodsScoreHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GoodsScoreHolder holder, GoodsScore data) {
        holder.userNameTv.setText(data.getUser().getUserName());
        holder.commentDateTv.setText(DateUtils.formatDateTime(data.getCreateDate()));
        holder.commentDetailTv.setText(data.getEvaluate());
    }

    public class GoodsScoreHolder extends ZhiheAdapter.BaseViewHolder {

        @ViewInject(R.id.goods_comment_user_name_tv)
        public TextView userNameTv;
        @ViewInject(R.id.goods_comment_date_tv)
        public TextView commentDateTv;
        @ViewInject(R.id.goods_comment_detail_tv)
        public TextView commentDetailTv;

        public GoodsScoreHolder(View itemView) {
            super(itemView);
        }
    }
}
