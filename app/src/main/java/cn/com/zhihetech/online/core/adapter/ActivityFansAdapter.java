package cn.com.zhihetech.online.core.adapter;

import android.content.Context;
import android.view.View;

import java.util.List;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.ActivityFans;
import cn.com.zhihetech.online.bean.User;

/**
 * Created by ShenYunjie on 2016/2/26.
 */
public class ActivityFansAdapter extends ZhiheAdapter<User, ActivityFansAdapter.ActivityFansHolder> {

    public ActivityFansAdapter(Context mContext) {
        this(mContext, R.layout.content_activity_fans_item, null);
    }

    public ActivityFansAdapter(Context mContext, int layoutId, List<User> mDatas) {
        super(mContext, layoutId, mDatas);
    }

    @Override
    public ActivityFansHolder onCreateViewHolder(View itemView) {
        return new ActivityFansHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ActivityFansHolder holder, User data) {

    }

    public class ActivityFansHolder extends ZhiheAdapter.BaseViewHolder {

        public ActivityFansHolder(View itemView) {
            super(itemView);
        }
    }
}
