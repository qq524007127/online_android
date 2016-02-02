package cn.com.zhihetech.online.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.easemob.chat.EMChatManager;
import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.ui.EaseChatFragment;
import com.easemob.easeui.widget.EaseAlertDialog;
import com.easemob.easeui.widget.EaseChatInputMenu;
import com.easemob.easeui.widget.EaseChatPrimaryMenuBase;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.core.util.StringUtils;
import cn.com.zhihetech.online.ui.fragment.SingleChatFragment;

/**
 * Created by ShenYunjie on 2016/2/1.
 */
@ContentView(R.layout.activity_chat)
public class ChatActivity extends BaseActivity {

    public static String USER_NAME_KEY = "EMCHAT_USER_NAME";

    public static ChatActivity activityInstance;
    private SingleChatFragment chatFragment;
    private String toChatUsername;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        activityInstance = this;
        initChatFragment();
    }

    private void initChatFragment() {
        //聊天人或群id
        toChatUsername = getIntent().getExtras().getString(EaseConstant.EXTRA_USER_ID);
        chatFragment = new SingleChatFragment();
        chatFragment.hideTitleBar();
        //传入参数
        chatFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.chat_container_fl, chatFragment).commit();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chat_options, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.chat_recycle) {
            if (chatFragment != null) {
                chatFragment.clearChatHistory();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
