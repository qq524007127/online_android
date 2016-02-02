package cn.com.zhihetech.online.core.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.easemob.easeui.EaseConstant;

import org.xutils.view.annotation.ViewInject;

import java.util.List;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.Merchant;
import cn.com.zhihetech.online.core.util.ImageLoader;
import cn.com.zhihetech.online.ui.activity.ChatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ShenYunjie on 2016/2/1.
 */
public class MyFriendAdapter extends ZhiheAdapter<Merchant, MyFriendAdapter.MyFriendViewHolder> {

    public MyFriendAdapter(Context mContext) {
        super(mContext, R.layout.content_my_friends_item);
    }

    @Override
    public MyFriendViewHolder onCreateViewHolder(View itemView) {
        return new MyFriendViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyFriendViewHolder holder, final Merchant data) {
        ImageLoader.disPlayImage(holder.headerCiv, data.getCoverImg());
        holder.friendNameTv.setText(data.getMerchName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ChatActivity.class);
                intent.putExtra(ChatActivity.USER_NAME_KEY, data.getMerchName());
                intent.putExtra(EaseConstant.EXTRA_USER_ID, data.getMerchantId().replaceAll("-", ""));
                mContext.startActivity(intent);
            }
        });
    }

    public class MyFriendViewHolder extends ZhiheAdapter.BaseViewHolder {
        @ViewInject(R.id.friend_header_civ)
        public CircleImageView headerCiv;
        @ViewInject(R.id.friend_name_tv)
        public TextView friendNameTv;

        public MyFriendViewHolder(View itemView) {
            super(itemView);
        }
    }
}
