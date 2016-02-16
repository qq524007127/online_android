package cn.com.zhihetech.online.ui.fragment;

import android.content.Intent;
import android.view.View;

import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.controller.EaseUI;
import com.easemob.easeui.domain.EaseUser;
import com.easemob.easeui.ui.EaseConversationListFragment;

import org.xutils.ex.DbException;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.EMUserInfo;
import cn.com.zhihetech.online.core.common.Constant;
import cn.com.zhihetech.online.core.db.DBUtils;
import cn.com.zhihetech.online.ui.activity.ChatActivity;

/**
 * Created by ShenYunjie on 2016/2/16.
 */
public class MyContactListFragment extends EaseConversationListFragment {

    private EMEventListener eventListener = new EMEventListener() {
        @Override
        public void onEvent(EMNotifierEvent emNotifierEvent) {
            switch (emNotifierEvent.getEvent()) {
                case EventNewMessage:
                    EMMessage message = (EMMessage) emNotifierEvent.getData();
                    try {
                        new DBUtils().saveOrUpdateUserInfo(createEMUserInfo(message));
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                    EaseUI.getInstance().getNotifier().onNewMsg(message);
                    refresh();
                    break;
            }
        }
    };

    private boolean isRegisterEventListener = false;    //是否已注册事件监听

    @Override
    protected void initView() {
        super.initView();
        getView().findViewById(R.id.conversation_list_search_bar).setVisibility(View.GONE);
        initConversationListItemClickListener();
        EaseUI.getInstance().setUserProfileProvider(new EaseUI.EaseUserProfileProvider() {
            @Override
            public EaseUser getUser(String username) {
                EaseUser easeUser = new EaseUser(username);
                EMUserInfo userInfo = null;
                try {
                    userInfo = new DBUtils().getUserInfoByUserName(username);
                } catch (DbException e) {
                    e.printStackTrace();
                }
                if (userInfo != null) {
                    easeUser.setNick(userInfo.getUserNick());
                    easeUser.setAvatar(userInfo.getAvatarUrl());
                }
                return easeUser;
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        if (!isRegisterEventListener) {
            EMChatManager.getInstance().registerEventListener(eventListener);
            isRegisterEventListener = true;
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        if (isRegisterEventListener) {
            EMChatManager.getInstance().registerEventListener(eventListener);
            isRegisterEventListener = false;
        }
    }

    @Override
    protected void setUpView() {
        super.setUpView();
    }

    /**
     * 设置回话点击事件
     */
    private void initConversationListItemClickListener() {
        setConversationListItemClickListener(new EaseConversationListItemClickListener() {
            @Override
            public void onListItemClicked(final EMConversation conversation) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra(EaseConstant.EXTRA_USER_ID, conversation.getUserName());
                getActivity().startActivity(intent);
            }
        });
    }

    /**
     * 收到新消息后更新或保存消息发送人基本信息
     *
     * @param message
     * @return
     */
    private EMUserInfo createEMUserInfo(EMMessage message) {
        EMUserInfo userInfo = new EMUserInfo();
        userInfo.setUserName(message.getUserName());
        userInfo.setUserNick(message.getStringAttribute(Constant.EXTEND_USER_NICK_NAME, "未知用户"));
        userInfo.setUserType(message.getIntAttribute(Constant.EXTEND_USER_TYPE, Constant.EXTEND_MERCHANT_USER));
        return userInfo;
    }
}
