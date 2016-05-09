package cn.com.zhihetech.online.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import cn.com.zhihetech.online.R;

/**
 * Created by ShenYunjie on 2016/1/22.
 */
@ContentView(R.layout.content_message_fragment)
public class MessageFragment extends BaseFragment {
    @ViewInject(R.id.message_tablayout)
    private TabLayout tabLayout;
    @ViewInject(R.id.message_viewpager)
    private ViewPager viewPager;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
    }

    private void initViews() {
        final Fragment[] fragments = {new MyContactListFragment(), new MyFocusMerchantsFragment()};
        final String[] tabs = {"消息", "我的好友"};
        viewPager.setAdapter(new FragmentPagerAdapter(getActivity().getSupportFragmentManager()) {
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
    }
}
