package cn.com.zhihetech.online.core.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.easemob.easeui.widget.EaseImageView;

import org.w3c.dom.Text;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.ActivityFans;
import cn.com.zhihetech.online.bean.User;
import cn.com.zhihetech.online.core.util.ImageLoader;

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
        ImageLoader.disPlayImage(holder.fansHeaderIv, data.getHeaderImg());
        holder.fansNameTv.setText(data.getUserName());
        holder.fanseInfoTv.setText("性别:" + (data.isSex() ? "男" : "女") + "      年龄:" + data.getAge());
    }

    public class ActivityFansHolder extends ZhiheAdapter.BaseViewHolder {

        @ViewInject(R.id.fans_header_iv)
        public EaseImageView fansHeaderIv;
        @ViewInject(R.id.fans_name_tv)
        public TextView fansNameTv;
        @ViewInject(R.id.fans_base_info_tv)
        public TextView fanseInfoTv;
        /*@ViewInject(R.id.fans_inviter_name_tv)
        public TextView fansInviterTv;*/

        public ActivityFansHolder(View itemView) {
            super(itemView);
        }
    }
}
