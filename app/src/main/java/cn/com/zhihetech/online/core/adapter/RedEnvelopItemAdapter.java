package cn.com.zhihetech.online.core.adapter;

import android.content.Context;
import android.view.View;

import cn.com.zhihetech.online.bean.RedEnvelopItem;

/**
 * Created by ShenYunjie on 2016/3/8.
 */
public class RedEnvelopItemAdapter extends ZhiheAdapter<RedEnvelopItem, RedEnvelopItemAdapter.RedEnvelopItemHolder> {

    public RedEnvelopItemAdapter(Context mContext) {
        super(mContext, 0);
    }

    @Override
    public RedEnvelopItemHolder onCreateViewHolder(View itemView) {
        return new RedEnvelopItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RedEnvelopItemHolder holder, RedEnvelopItem data) {

    }

    public class RedEnvelopItemHolder extends ZhiheAdapter.BaseViewHolder {

        public RedEnvelopItemHolder(View itemView) {
            super(itemView);
        }
    }
}
