package cn.com.zhihetech.online.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.controller.EaseUI;
import com.easemob.easeui.domain.EaseUser;

import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;


import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.EMUserInfo;
import cn.com.zhihetech.online.core.db.DBUtils;
import cn.com.zhihetech.online.core.util.StringUtils;
import cn.com.zhihetech.online.ui.fragment.SingleChatFragment;

/**
 * 活动聊天室
 * Created by ShenYunjie on 2016/2/25.
 */
@ContentView(R.layout.activity_chat_room)
public class ActivityChatRoomActivity extends BaseActivity {

    public static String CHAT_ROOM_NAME = "CHAT_ROOM_NAME";
    public static String ACTIVITY_ID = "ACTIVITY_ID";

    private String activityId;

    public static ActivityChatRoomActivity activityInstance;
    private SingleChatFragment chatFragment;
    private String toChatRoom;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        activityId = getIntent().getStringExtra(ACTIVITY_ID);
        activityInstance = this;
        initChatFragment();
    }

    private void initChatFragment() {
        //聊天人或群id
        toChatRoom = getIntent().getExtras().getString(EaseConstant.EXTRA_USER_ID);
        chatFragment = new SingleChatFragment();
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityInstance = null;
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
        getMenuInflater().inflate(R.menu.chat_room_options, menu);
        return super.onCreateOptionsMenu(menu);
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
