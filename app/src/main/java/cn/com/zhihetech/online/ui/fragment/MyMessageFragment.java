package cn.com.zhihetech.online.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ListView;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import cn.com.zhihetech.online.R;

/**
 * Created by ShenYunjie on 2016/1/28.
 */
@ContentView(R.layout.content_my_message)
public class MyMessageFragment extends BaseFragment {
    @ViewInject(R.id.my_message_lv)
    private ListView msgListView;

    private final String pwd = "123456";

    private NewMessageBroadcastReceiver msgReceiver;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Event({R.id.test_btn, R.id.add_user})
    private void onViewClick(final View view) {
        switch (view.getId()) {
            case R.id.test_btn:
                EMChatManager.getInstance().login(getUseId().replaceAll("-", ""), pwd, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        showMsg(view, "登录成功");
                        register();
                    }

                    @Override
                    public void onError(int i, String s) {
                        showMsg(view, "登录失败：" + s);
                    }

                    @Override
                    public void onProgress(int i, String s) {

                    }
                });
                break;
            case R.id.add_user:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            EMChatManager.getInstance().createAccountOnServer("汉子用户", pwd);
                            log("注册成功");
                        } catch (Exception e) {
                            log("注册失败");
                        }
                    }
                }).start();
                break;
        }
    }

    private void register() {
        msgReceiver = new NewMessageBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(EMChatManager.getInstance().getNewMessageBroadcastAction());
        intentFilter.setPriority(3);
        getContext().registerReceiver(msgReceiver, intentFilter);

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
                showMsg(msgListView, ((TextMessageBody) message.getBody()).getMessage());
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContext().unregisterReceiver(msgReceiver);
    }
}
