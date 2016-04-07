package cn.com.zhihetech.online.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.EMUserInfo;
import cn.com.zhihetech.online.core.common.ActivityStack;
import cn.com.zhihetech.online.core.emchat.EMMessageHelper;
import cn.com.zhihetech.online.core.emchat.helpers.EMChatHelper;
import cn.com.zhihetech.online.core.emchat.helpers.EMEventHandle;
import cn.com.zhihetech.online.core.service.EMChatConnectionService;
import cn.com.zhihetech.online.core.service.EMChatEventService;
import cn.com.zhihetech.online.core.util.NotificationHelper;
import cn.com.zhihetech.online.ui.fragment.BaseFragment;
import cn.com.zhihetech.online.ui.fragment.HomeFragment;
import cn.com.zhihetech.online.ui.fragment.MessageFragment;
import cn.com.zhihetech.online.ui.fragment.MyFragment;
import cn.com.zhihetech.online.ui.fragment.ShoppingCartFragment;

/**
 * Created by ShenYunjie on 2016/1/15.
 */
@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    private final String HOME_FRAGMENT_TAG = "home_tag";
    private final String MESSAGE_FRAGMENT_TAG = "message_tag";
    private final String MY_FRAGMENT_TAG = "my_tag";
    private final String SHOPPING_CART_FRAGMENT_TAG = "shopping_cart_tag";

    private boolean isExit = false;

    private BaseFragment homeFragment;
    private BaseFragment messageFragment;
    private BaseFragment myFragment;
    private BaseFragment shoppingCartFragment;

    @ViewInject(R.id.main_tab_layout)
    private TabLayout mainTabLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initMainTab();
        initEMChatSettings();
    }

    /**
     * 首次初始化Tab和显示主界面
     */
    private void initMainTab() {
        int[] tabs = {R.layout.content_home_tab, R.layout.content_message_tab, R.layout.content_shopping_cart_tab, R.layout.content_my_tab};
        for (int resId : tabs) {
            View tab = LayoutInflater.from(this).inflate(resId, null);
            mainTabLayout.addTab(mainTabLayout.newTab().setCustomView(tab));
        }
        mainTabLayout.getTabAt(0).select();
        mainTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            int lastPosition = 0;

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 1) {
                    Intent intent = new Intent(getSelf(), MessageActivity.class);
                    intent.putExtra(BaseActivity.CUSTOM_TITLE_KEY, tab.getText());
                    startActivity(intent);
                    mainTabLayout.getTabAt(lastPosition).select();
                    return;
                }
                hideFragments();
                showFragmentByTabIndex(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                lastPosition = tab.getPosition();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        homeFragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.frame_content, homeFragment, HOME_FRAGMENT_TAG).commit();
    }

    /**
     * 隐藏所有Fragment
     */
    private void hideFragments() {
        FragmentTransaction localFragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (this.homeFragment != null) {
            localFragmentTransaction.hide(this.homeFragment);
        }
        if (this.messageFragment != null) {
            localFragmentTransaction.hide(this.messageFragment);
        }
        if (this.shoppingCartFragment != null) {
            localFragmentTransaction.hide(this.shoppingCartFragment);
        }
        if (this.myFragment != null) {
            localFragmentTransaction.hide(this.myFragment);
        }
        localFragmentTransaction.commit();
    }

    /**
     * 根据所选中Tab显示对应的Fragment
     *
     * @param tabPosition
     */
    private void showFragmentByTabIndex(int tabPosition) {
        FragmentManager localFragmentManager = getSupportFragmentManager();
        FragmentTransaction localFragmentTransaction = localFragmentManager.beginTransaction();
        switch (tabPosition) {
            case 0:
                if (this.homeFragment == null) {
                    this.homeFragment = (HomeFragment) localFragmentManager.findFragmentByTag(HOME_FRAGMENT_TAG);
                }
                if (homeFragment == null) {
                    this.homeFragment = new HomeFragment();
                    localFragmentTransaction.add(R.id.frame_content, homeFragment, HOME_FRAGMENT_TAG);
                }
                localFragmentTransaction.show(this.homeFragment);
                break;
            case 1:
                if (this.messageFragment == null) {
                    this.messageFragment = (MessageFragment) localFragmentManager.findFragmentByTag(MESSAGE_FRAGMENT_TAG);
                }
                if (messageFragment == null) {
                    this.messageFragment = new MessageFragment();
                    localFragmentTransaction.add(R.id.frame_content, messageFragment, MESSAGE_FRAGMENT_TAG);
                }
                localFragmentTransaction.show(this.messageFragment);
                break;
            case 2:
                if (this.shoppingCartFragment == null) {
                    this.shoppingCartFragment = (ShoppingCartFragment) localFragmentManager.findFragmentByTag(SHOPPING_CART_FRAGMENT_TAG);
                }
                if (shoppingCartFragment == null) {
                    this.shoppingCartFragment = new ShoppingCartFragment();
                    localFragmentTransaction.add(R.id.frame_content, shoppingCartFragment, SHOPPING_CART_FRAGMENT_TAG);
                }
                localFragmentTransaction.show(this.shoppingCartFragment);
                break;
            case 3:
                if (this.myFragment == null) {
                    this.myFragment = (MyFragment) localFragmentManager.findFragmentByTag(MY_FRAGMENT_TAG);
                }
                if (myFragment == null) {
                    this.myFragment = new MyFragment();
                    localFragmentTransaction.add(R.id.frame_content, myFragment, MY_FRAGMENT_TAG);
                }
                localFragmentTransaction.show(this.myFragment);
                break;
            default:
        }
        localFragmentTransaction.commit();
    }

    private void initEMChatSettings() {
        Intent connectionIntent = new Intent(this, EMChatConnectionService.class);
        startService(connectionIntent);

        Intent eventIntent = new Intent(this, EMChatEventService.class);
        startService(eventIntent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            doubleClickBackKeyExitApp();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 双击返回键退出APP
     */
    private void doubleClickBackKeyExitApp() {
        if (isExit) {
            exitApp();
        } else {
            isExit = true;
            Toast.makeText(this, R.string.reclick_exit_app, Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isExit = false;
                }
            }, 1000);
        }
    }

    /**
     * 退出程序
     */
    private void exitApp() {
        EMChatManager.getInstance().logout(null);
        finish();
        ActivityStack.getInstance().clearActivity();
    }
}
