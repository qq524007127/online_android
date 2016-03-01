package cn.com.zhihetech.online.core.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.xutils.view.annotation.ViewInject;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.Activity;
import cn.com.zhihetech.online.core.util.ImageLoader;

/**
 * Created by ShenYunjie on 2016/3/1.
 */
public class ActivityChatRoomAdapter extends ZhiheAdapter<Activity, ActivityChatRoomAdapter.ActivityChatRoomHolder> {

    public ActivityChatRoomAdapter(Context mContext) {
        super(mContext, R.layout.content_merchant_activity_chat_room_item, null);
    }

    @Override
    public ActivityChatRoomHolder onCreateViewHolder(View itemView) {
        return new ActivityChatRoomHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ActivityChatRoomHolder holder, Activity data) {
        ImageLoader.disPlayImage(holder.chatRoomCoverIv, data.getCoverImg());
        holder.chatRoomNameTv.setText(data.getActivitName());
    }

    public class ActivityChatRoomHolder extends ZhiheAdapter.BaseViewHolder {

        @ViewInject(R.id.activity_chat_room_cover_iv)
        public ImageView chatRoomCoverIv;
        @ViewInject(R.id.activity_chat_room_name_tv)
        public TextView chatRoomNameTv;

        public ActivityChatRoomHolder(View itemView) {
            super(itemView);
        }
    }
}
