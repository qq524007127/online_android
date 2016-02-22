package cn.com.zhihetech.online.ui.fragment;

import android.view.View;
import android.widget.Toast;

import com.easemob.chat.EMMessage;
import com.easemob.easeui.ui.EaseChatFragment;
import com.easemob.easeui.widget.chatrow.EaseCustomChatRowProvider;

import cn.com.zhihetech.online.bean.ImgInfo;
import cn.com.zhihetech.online.bean.User;
import cn.com.zhihetech.online.core.common.Constant;
import cn.com.zhihetech.online.core.common.ZhiheApplication;
import cn.com.zhihetech.online.core.util.StringUtils;


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
                User user = ZhiheApplication.getInstance().getUser();
                message.setAttribute(Constant.EXTEND_USER_NICK_NAME, user.getUserName());
                message.setAttribute(Constant.EXTEND_USER_ID, user.getUserId());
                ImgInfo headerImg = user.getHeaderImg();
                if (headerImg != null && !StringUtils.isEmpty(headerImg.getUrl())) {
                    message.setAttribute(Constant.EXTEND_USER_HEAD_IMG, headerImg.getUrl());
                }
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
        for (int i = 0; i < itemStrings.length - 1; i++) {
            inputMenu.registerExtendMenuItem(itemStrings[i], itemdrawables[i], itemIds[i], extendMenuItemClickListener);
        }
    }

    /**
     * 清空聊天记录
     */
    public void clearChatHistory() {
        emptyHistory();
    }
}
