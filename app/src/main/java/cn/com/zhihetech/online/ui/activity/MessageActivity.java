package cn.com.zhihetech.online.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.service.EMChatEventService;
import cn.com.zhihetech.online.ui.fragment.MyContactListFragment;
import cn.com.zhihetech.online.ui.fragment.MyFocusMerchantsFragment;

/**
 * Created by ShenYunjie on 2016/4/6.
 */
@ContentView(R.layout.activity_message)
public class MessageActivity extends BaseActivity {

    @ViewInject(R.id.message_tab_layout)
    private TabLayout tabLayout;
    @ViewInject(R.id.message_content_viewpager)
    private ViewPager viewPager;

    final String[] tabs = {"消息", "我的好友"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        EMChatEventService.setStoped(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EMChatEventService.setStoped(false);
    }

    private void initViews() {
        final Fragment[] fragments = {new MyContactListFragment(), new MyFocusMerchantsFragment()};
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments[position];
            }

            @Override
            public int getCount() {
                return fragments.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return tabs[position];
            }
        });
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                toolbar.setTitle(tabs[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
