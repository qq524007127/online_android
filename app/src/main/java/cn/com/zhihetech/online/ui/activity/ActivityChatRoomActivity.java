package cn.com.zhihetech.online.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatRoom;
import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.controller.EaseUI;
import com.easemob.easeui.domain.EaseUser;
import com.easemob.exceptions.EaseMobException;

import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;


import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.EMUserInfo;
import cn.com.zhihetech.online.core.ZhiheApplication;
import cn.com.zhihetech.online.core.db.DBUtils;
import cn.com.zhihetech.online.core.util.StringUtils;
import cn.com.zhihetech.online.ui.fragment.UpgradeChatFragment;

/**
 * 活动聊天室
 * Created by ShenYunjie on 2016/2/25.
 */
@ContentView(R.layout.activity_chat_room)
public class ActivityChatRoomActivity extends BaseActivity {

    public static String CHAT_ROOM_NAME = "CHAT_ROOM_NAME";
    public static String ACTIVITY_ID = "ACTIVITY_ID";

    private String activityId;

    private UpgradeChatFragment chatFragment;
    private String toChatRoom;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        activityId = getIntent().getStringExtra(ACTIVITY_ID);
        //聊天室ID
        toChatRoom = getIntent().getExtras().getString(EaseConstant.EXTRA_USER_ID);
        if (StringUtils.isEmpty(activityId) || StringUtils.isEmpty(toChatRoom)) {
            showMsg("出错了！");
            finish();
            return;
        }
        initChatFragment();
    }

    private void initChatFragment() {
        initChatRoomMemberCount(toChatRoom);
        chatFragment = new UpgradeChatFragment();
        chatFragment.hideTitleBar();
        Bundle args = getIntent().getExtras();
        if (args == null) {
            args = new Bundle();
        }

        //传入参数
        chatFragment.setArguments(args);
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
        getSupportFragmentManager().beginTransaction().add(R.id.chat_room_container_fl, chatFragment).commit();
        chatFragment.setOnChatRoomChangeListenr(new UpgradeChatFragment.OnChatRoomChangeListener() {
            @Override
            public void onMemberJoined(String roomId, String participant) {
                initChatRoomMemberCount(roomId);
            }

            @Override
            public void onMemberExited(String roomId, String roomName, String participant) {
                initChatRoomMemberCount(roomId);
            }
        });
    }

    /**
     * 聊天室人员数量统计Handler
     */
    private final int SUCCESS_CODE = 1;
    final Handler chaRoomHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == SUCCESS_CODE) {
                setToolbarSubTitle("当前人数：" + msg.obj);
            }
        }
    };

    /**
     * 首次进入初始化聊天室人数
     *
     * @param toChatRoom
     */
    private void initChatRoomMemberCount(final String toChatRoom) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMChatRoom chatRoom = EMChatManager.getInstance().fetchChatRoomFromServer(toChatRoom);
                    Message msg = new Message();
                    msg.what = SUCCESS_CODE;
                    msg.obj = chatRoom.getMembers().size();
                    chaRoomHandler.sendMessage(msg);
                } catch (EaseMobException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 设置Toolbar的副标题
     *
     * @param subTitle
     */
    protected void setToolbarSubTitle(String subTitle) {
        this.toolbar.setSubtitle(subTitle);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // 点击notification bar进入聊天页面，保证只有一个聊天页面
        String username = intent.getStringExtra(EaseConstant.EXTRA_USER_ID);
        if (toChatRoom.equals(username))
            super.onNewIntent(intent);
        else {
            finish();
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        chatFragment.onBackPressed();
    }

    public String getToChatRoom() {
        return toChatRoom;
    }

    @Override
    protected CharSequence getToolbarTile() {
        String title = getIntent().getStringExtra(CHAT_ROOM_NAME);
        if (!StringUtils.isEmpty(title)) {
            return title;
        }
        return super.getToolbarTile();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isMerchant()) {
            getMenuInflater().inflate(R.menu.chat_room_options, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 当前登录的用户是商家用户还是普通用户
     *
     * @return boolean
     */
    protected boolean isMerchant() {
        return ZhiheApplication.getInstance().getUserType() == ZhiheApplication.MERCHANT_USER_TYPE;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.chat_room_members) {
            //跳转到参加活动的会员列表
            Intent intent = new Intent(this, ActivityFansActivity.class);
            intent.putExtra(ActivityFansActivity.ACTIVITY_ID_KEY, activityId);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
