package cn.com.zhihetech.online.core.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.widget.EaseImageView;

import org.xutils.ex.DbException;
import org.xutils.view.annotation.ViewInject;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.EMUserInfo;
import cn.com.zhihetech.online.bean.Merchant;
import cn.com.zhihetech.online.core.common.Constant;
import cn.com.zhihetech.online.core.db.DBUtils;
import cn.com.zhihetech.online.core.util.ImageLoader;
import cn.com.zhihetech.online.model.MerchantModel;
import cn.com.zhihetech.online.ui.activity.SingleChatActivity;

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
    public void onBindViewHolder(final MyFriendViewHolder holder, final Merchant data) {
        ImageLoader.disPlayImage(holder.headerCiv, data.getCoverImg());
        holder.friendNameTv.setText(data.getMerchName());
    }

    public class MyFriendViewHolder extends ZhiheAdapter.BaseViewHolder {
        @ViewInject(R.id.friend_header_civ)
        public EaseImageView headerCiv;
        @ViewInject(R.id.friend_name_tv)
        public TextView friendNameTv;

        public MyFriendViewHolder(View itemView) {
            super(itemView);
        }
    }
}
