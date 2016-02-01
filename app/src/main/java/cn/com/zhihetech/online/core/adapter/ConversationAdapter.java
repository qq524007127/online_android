package cn.com.zhihetech.online.core.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.easemob.easeui.EaseConstant;

import org.xutils.view.annotation.ViewInject;

import java.util.List;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.Conversation;
import cn.com.zhihetech.online.core.util.ImageLoader;
import cn.com.zhihetech.online.core.util.StringUtils;
import cn.com.zhihetech.online.ui.activity.ChatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ShenYunjie on 2016/2/1.
 */
public class ConversationAdapter extends ZhiheAdapter<Conversation, ConversationAdapter.ConversationHolder> {

    public ConversationAdapter(Context mContext, List<Conversation> mDatas) {
        super(mContext, R.layout.content_my_conversation_item, mDatas);
    }

    @Override
    public ConversationHolder onCreateViewHolder(View itemView) {
        return new ConversationHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ConversationHolder holder, final Conversation data) {
        if (!StringUtils.isEmpty(data.getHeaderImg())) {
            ImageLoader.disPlayImage(holder.headerCiv, data.getUserId());
        }
        holder.userNickTv.setText(data.getNickName());
        EMMessage message = data.getEmConver().getLastMessage();
        switch (message.getType()) {
            case TXT:
                TextMessageBody body = (TextMessageBody) message.getBody();
                holder.lastMsgTv.setText(body.getMessage());
                break;
            case IMAGE:
                holder.lastMsgTv.setText("图片");
                break;
            case VIDEO:
                holder.lastMsgTv.setText("语音");
                break;
            case LOCATION:
                holder.lastMsgTv.setText("未知信息");
                break;
            case FILE:
                holder.lastMsgTv.setText("文件");
                break;
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, data.getEmConver().getUserName()));
            }
        });
    }

    public class ConversationHolder extends ZhiheAdapter.BaseViewHolder {
        @ViewInject(R.id.conversation_header_civ)
        public CircleImageView headerCiv;
        @ViewInject(R.id.conversation_user_name_tv)
        public TextView userNickTv;
        @ViewInject(R.id.conversation_content_tv)
        public TextView lastMsgTv;

        public ConversationHolder(View itemView) {
            super(itemView);
        }
    }
}
