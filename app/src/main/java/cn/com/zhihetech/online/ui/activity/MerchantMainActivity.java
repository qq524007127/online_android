package cn.com.zhihetech.online.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.widget.Toast;

import com.easemob.chat.EMChatManager;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.core.ZhiheApplication;
import cn.com.zhihetech.online.core.common.ActivityStack;
import cn.com.zhihetech.online.core.service.EMChatConnectionService;
import cn.com.zhihetech.online.core.service.EMChatEventService;
import cn.com.zhihetech.online.ui.fragment.MerchantActivityChatRoomsFragment;
import cn.com.zhihetech.online.ui.fragment.MerchantSettingsFragment;
import cn.com.zhihetech.online.ui.fragment.MyContactListFragment;

/**
 * Created by ShenYunjie on 2016/2/29.
 */
@ContentView(R.layout.activity_merchant_main)
public class MerchantMainActivity extends MerchantBaseActivity {

    @ViewInject(R.id.merchant_main_view_pager)
    private ViewPager mainViewPager;
    @ViewInject(R.id.merchant_main_tab_layout)
    private TabLayout mainTablayout;

    private boolean isExit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setToolbarHomeAsUp(false);
        super.onCreate(savedInstanceState);
        initViewAndData();
        initEMChatConnectionAndEventService();
    }

    private void initEMChatConnectionAndEventService() {
        Intent connectionIntent = new Intent(this, EMChatConnectionService.class);
        startService(connectionIntent);
        Intent eventIntent = new Intent(this, EMChatEventService.class);
        startService(eventIntent);
    }

    private void initViewAndData() {
        this.toolbar.setSubtitle(ZhiheApplication.getInstance().getLogedMerchant().getMerchName());
        this.toolbar.setLogo(R.mipmap.ic_launcher);
        initViews();
    }

    private void initViews() {
        mainViewPager.setOffscreenPageLimit(2);
        final Fragment[] fragments = {new MyContactListFragment(), new MerchantActivityChatRoomsFragment(), new MerchantSettingsFragment()};
        final String[] titles = {"会话", "我的活动", "设置"};
        mainViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments[position];
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titles[position];
            }

            @Override
            public int getCount() {
                return fragments.length;
            }
        });
        mainTablayout.setupWithViewPager(mainViewPager);
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
