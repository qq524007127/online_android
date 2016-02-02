package cn.com.zhihetech.online.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ListView;

import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.Conversation;
import cn.com.zhihetech.online.core.adapter.ConversationAdapter;
import cn.com.zhihetech.online.core.common.Constant;

/**
 * Created by ShenYunjie on 2016/1/28.
 */
@ContentView(R.layout.content_my_message)
public class MyMessageFragment extends BaseFragment {
    @ViewInject(R.id.my_conversation_lv)
    private ListView converListView;

    private ConversationAdapter adapter;

    NewMessageBroadcastReceiver msgReceiver;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        IntentFilter intentFilter = new IntentFilter(EMChatManager.getInstance().getNewMessageBroadcastAction());
        intentFilter.setPriority(3);
        msgReceiver = new NewMessageBroadcastReceiver();
        getContext().registerReceiver(msgReceiver, intentFilter);
        //EMChatManager.getInstance().registerEventListener(eventListener);
        EMChat.getInstance().setAppInited();
        initViewAndData();
    }

    private void initViewAndData() {
        adapter = new ConversationAdapter(getContext());
        converListView.setAdapter(adapter);
        loadCoversations();
    }

    private void loadCoversations() {
        Hashtable<String, EMConversation> conversations = EMChatManager.getInstance().getAllConversations();
        if (conversations == null || conversations.isEmpty()) {
            return;
        }
        List<Conversation> convers = new ArrayList<>();
        for (String userName : conversations.keySet()) {
            EMConversation conversation = conversations.get(userName);
            if (conversation.getAllMessages() == null || conversation.getAllMessages().isEmpty()) {
                continue;
            }
            Conversation conver = new Conversation();
            conver.setEmConver(conversation);
            EMMessage message = conversation.getLastMessage();
            conver.setNickName(message.getStringAttribute(Constant.EXTEND_USER_NICK_NAME, "未知用户"));
            convers.add(conver);
        }
        Collections.sort(convers, new Comparator<Conversation>() {
            @Override
            public int compare(Conversation lhs, Conversation rhs) {
                EMConversation emCon1 = lhs.getEmConver();
                EMConversation emCon2 = rhs.getEmConver();
                EMMessage msg1 = emCon1.getLastMessage();
                EMMessage msg2 = emCon2.getLastMessage();
                return (int) (msg2.getMsgTime() - msg1.getMsgTime());
            }
        });
        adapter.refreshData(convers);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadCoversations();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (msgReceiver != null) {
            getContext().unregisterReceiver(msgReceiver);
        }
    }

    EMEventListener eventListener = new EMEventListener() {
        @Override
        public void onEvent(EMNotifierEvent event) {
            switch (event.getEvent()) {
                case EventNewMessage: // 接收新消息
                    loadCoversations();
                    break;
            }
        }
    };


    private class NewMessageBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 注销广播
            abortBroadcast();
            loadCoversations();
            // 消息id（每条消息都会生成唯一的一个id，目前是SDK生成）
            String msgId = intent.getStringExtra("msgid");
            //发送方
            String username = intent.getStringExtra("from");
            // 收到这个广播的时候，message已经在db和内存里了，可以通过id获取mesage对象
            EMMessage message = EMChatManager.getInstance().getMessage(msgId);
            EMConversation conversation = EMChatManager.getInstance().getConversation(username);
            // 如果是群聊消息，获取到group id
            if (message.getChatType() == EMMessage.ChatType.GroupChat) {
                username = message.getTo();
            }
            if (!username.equals(username)) {
                // 消息不是发给当前会话，return
                return;
            }
        }
    }
}
