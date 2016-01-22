package cn.com.zhihetech.online.ui.widget;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import cn.com.zhihetech.online.R;
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
    }

    private void initMainTab() {
        int[] tabs = {R.layout.content_home_tab, R.layout.content_message_tab, R.layout.content_shopping_cart_tab, R.layout.content_my_tab};
        for (int resId : tabs) {
            View tab = LayoutInflater.from(this).inflate(resId, null);
            mainTabLayout.addTab(mainTabLayout.newTab().setCustomView(tab));
        }
        mainTabLayout.getTabAt(0).select();
        mainTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                hideFragments();
                showFragmentByTabIndex(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        homeFragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.frame_content, homeFragment, HOME_FRAGMENT_TAG).commit();
    }

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
}
