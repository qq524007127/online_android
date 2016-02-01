package cn.com.zhihetech.online.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.ui.EaseChatFragment;

import org.xutils.view.annotation.ContentView;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.core.util.StringUtils;

/**
 * Created by ShenYunjie on 2016/2/1.
 */
@ContentView(R.layout.activity_chat)
public class ChatActivity extends BaseActivity {

    public static String USER_NAME_KEY = "EMCHAT_USER_NAME";

    public static ChatActivity activityInstance;
    private EaseChatFragment chatFragment;
    String toChatUsername;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        //setContentView(R.layout.activity_chat);
        activityInstance = this;
        //聊天人或群id
        toChatUsername = getIntent().getExtras().getString(EaseConstant.EXTRA_USER_ID);
        chatFragment = new EaseChatFragment();
        //传入参数
        chatFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.container, chatFragment).commit();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityInstance = null;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // 点击notification bar进入聊天页面，保证只有一个聊天页面
        String username = intent.getStringExtra("userId");
        if (toChatUsername.equals(username))
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

    public String getToChatUsername() {
        return toChatUsername;
    }

    @Override
    protected CharSequence getToolbarTile() {
        String userName = getIntent().getStringExtra(USER_NAME_KEY);
        if (!StringUtils.isEmpty(userName)) {
            return userName;
        }
        return super.getToolbarTile();
    }
}
