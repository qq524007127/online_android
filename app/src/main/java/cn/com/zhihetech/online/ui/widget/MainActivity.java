package cn.com.zhihetech.online.ui.widget;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.ui.fragment.HomeFragment;

/**
 * Created by ShenYunjie on 2016/1/15.
 */
@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    private String HOME_FRGMENT_FLAG = "homeFragmentFlag";

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
        mainTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        getSupportFragmentManager().beginTransaction().add(R.id.frame_content, new HomeFragment(), HOME_FRGMENT_FLAG).commit();
    }
}
