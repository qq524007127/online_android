package cn.com.zhihetech.online.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ListView;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.Conversation;
import cn.com.zhihetech.online.core.adapter.ConversationAdapter;

/**
 * Created by ShenYunjie on 2016/1/28.
 */
@ContentView(R.layout.content_my_message)
public class MyMessageFragment extends BaseFragment {
    @ViewInject(R.id.my_conversation_lv)
    private ListView converListView;

    private NewMessageBroadcastReceiver newMsgReceiver;

    private ConversationAdapter adapter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewAndData();
    }

    private void initViewAndData() {
        Hashtable<String, EMConversation> conversations = EMChatManager.getInstance().getAllConversations();
        if (conversations == null || conversations.isEmpty()) {
            return;
        }
        List<Conversation> convers = new ArrayList<>();
        for (String userName : conversations.keySet()) {
            EMConversation conversation = conversations.get(userName);
            Conversation conver = new Conversation();
            conver.setEmConver(conversation);
            EMMessage message = conversation.getLastMessage();
            conver.setNickName(conversation.getLastMessage().getStringAttribute("nickName", "未知用户"));
            convers.add(conver);
        }
        adapter = new ConversationAdapter(getContext(), convers);
        converListView.setAdapter(adapter);
    }


    private class NewMessageBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //消息id
            String msgId = intent.getStringExtra("msgid");
            //发消息的人的username(userid)
            String msgFrom = intent.getStringExtra("from");
            //消息类型，文本，图片，语音消息等,这里返回的值为msg.type.ordinal()。
            //所以消息type实际为是enum类型
            int msgType = intent.getIntExtra("type", 0);
            log("new message id:" + msgId + " from:" + msgFrom + " type:" + msgType);
            //更方便的方法是通过msgId直接获取整个message
            EMMessage message = EMChatManager.getInstance().getMessage(msgId);
            EMConversation conversation = EMChatManager.getInstance().getConversation(msgFrom);
            if (msgType == EMMessage.Type.TXT.ordinal()) {
                showMsg(converListView, ((TextMessageBody) message.getBody()).getMessage());
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (newMsgReceiver != null) {
            getContext().unregisterReceiver(newMsgReceiver);
        }
    }
}
