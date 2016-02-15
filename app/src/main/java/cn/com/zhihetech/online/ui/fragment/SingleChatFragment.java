package cn.com.zhihetech.online.ui.fragment;

import android.view.View;
import android.widget.Toast;

import com.easemob.chat.EMMessage;
import com.easemob.easeui.ui.EaseChatFragment;
import com.easemob.easeui.widget.chatrow.EaseCustomChatRowProvider;

import cn.com.zhihetech.online.core.common.Constant;
import cn.com.zhihetech.online.core.common.ZhiheApplication;


/**
 * 单人聊天界面
 * Created by ShenYunjie on 2016/2/2.
 */
public class SingleChatFragment extends EaseChatFragment {


    @Override
    protected void initView() {
        super.initView();
        hideTitleBar();
        messageList.setShowUserNick(true);
        setChatFragmentListener(new EaseChatFragmentListener() {
            @Override
            public void onSetMessageAttributes(EMMessage message) {
                message.setAttribute(Constant.EXTEND_USER_NICK_NAME, ZhiheApplication.getInstance().getUser().getUserName());
                message.setAttribute(Constant.EXTEND_USER_ID, ZhiheApplication.getInstance().getUser().getUserId());
                message.setAttribute(Constant.EXTEND_USER_TYPE, Constant.EXTEND_NORMAL_USER);
            }

            @Override
            public void onEnterToChatDetails() {

            }

            @Override
            public void onAvatarClick(String username) {
                Toast.makeText(getContext(), username, Toast.LENGTH_LONG).show();
            }

            @Override
            public boolean onMessageBubbleClick(EMMessage message) {
                return false;
            }

            @Override
            public void onMessageBubbleLongClick(EMMessage message) {

            }

            @Override
            public boolean onExtendMenuItemClick(int itemId, View view) {
                return false;
            }

            @Override
            public EaseCustomChatRowProvider onSetCustomChatRowProvider() {
                return null;
            }
        });
    }

    @Override
    protected void registerExtendMenuItem() {
        super.registerExtendMenuItem();
    }

    /**
     * 清空聊天记录
     */
    public void clearChatHistory() {
        emptyHistory();
    }
}
