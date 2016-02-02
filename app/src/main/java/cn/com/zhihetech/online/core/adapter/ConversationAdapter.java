package cn.com.zhihetech.online.core.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.easemob.easeui.EaseConstant;
import com.readystatesoftware.viewbadger.BadgeView;

import org.xutils.view.annotation.ViewInject;

import java.nio.channels.Channel;
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

    public ConversationAdapter(Context mContext) {
        super(mContext, R.layout.content_my_conversation_item);
    }

    public ConversationAdapter(Context mContext, List<Conversation> mDatas) {
        super(mContext, R.layout.content_my_conversation_item, mDatas);
    }

    @Override
    public ConversationHolder onCreateViewHolder(View itemView) {
        return new ConversationHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ConversationHolder holder, final Conversation data) {
        final EMConversation emConversation = data.getEmConver();
        if (!StringUtils.isEmpty(data.getHeaderImg())) {
            ImageLoader.disPlayImage(holder.headerCiv, data.getUserId());
        }
        holder.badge.setVisibility(View.INVISIBLE);
        int unReadCount = emConversation.getUnreadMsgCount();
        if (unReadCount > 0) {
            String text = String.valueOf(unReadCount);
            if (unReadCount > 99) {
                text += "+";
            }
            holder.badge.setText(text);
            holder.badge.setVisibility(View.VISIBLE);
        }
        holder.userNickTv.setText(data.getNickName());
        EMMessage message = emConversation.getLastMessage();
        holder.lastMsgTv.setText(getMessageType(message));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ChatActivity.class);
                intent.putExtra(ChatActivity.USER_NAME_KEY, data.getNickName());
                intent.putExtra(EaseConstant.EXTRA_USER_ID, data.getEmConver().getUserName());
                mContext.startActivity(intent);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        emConversation.markAllMessagesAsRead();
                    }
                }).start();
            }
        });
    }

    /**
     * 根据消息获取消息类型
     *
     * @param message
     * @return
     */
    private String getMessageType(EMMessage message) {
        String typeText = "";
        switch (message.getType()) {
            case TXT:
                TextMessageBody body = (TextMessageBody) message.getBody();
                typeText = body.getMessage();
                break;
            case IMAGE:
                typeText = "[图片]";
                break;
            case VIDEO:
                typeText = "[视频]";
                break;
            case VOICE:
                typeText = "[语音]";
                break;
            case LOCATION:
                typeText = "[地理位置]";
                break;
            case FILE:
                typeText = "[文件]";
                break;
        }
        return typeText;
    }

    public class ConversationHolder extends ZhiheAdapter.BaseViewHolder {
        @ViewInject(R.id.conversation_header_civ)
        public CircleImageView headerCiv;
        @ViewInject(R.id.conversation_user_name_tv)
        public TextView userNickTv;
        @ViewInject(R.id.conversation_content_tv)
        public TextView lastMsgTv;
        @ViewInject(R.id.conversation_bagde_tv)
        public TextView badge;

        public ConversationHolder(View itemView) {
            super(itemView);
        }
    }
}
