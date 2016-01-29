package cn.com.zhihetech.online.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.ui.fragment.BaseFragment;
import cn.com.zhihetech.online.ui.fragment.CategoryActivityFragment;
import cn.com.zhihetech.online.ui.fragment.CategoryGoodsFragment;
import cn.com.zhihetech.online.ui.fragment.CategoryMerchantFragment;

/**
 * Created by ShenYunjie on 2016/1/21.
 */
@ContentView(R.layout.activity_category_detail)
public class CategoryDetailActivity extends BaseActivity {
    public final static String CATETGORY_ID_KEY = "categorieId";
    public final static String CATEGORY_NAME_KEY = "categorieName";

    @ViewInject(R.id.category_detail_tl)
    private TabLayout tabLayout;
    @ViewInject(R.id.category_detail_vp)
    private ViewPager viewPager;

    private String[] tabs = {"活动", "商品", "商家"};

    private String categorieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categorieId = getIntent().getStringExtra(CATETGORY_ID_KEY);
        initViews();
    }

    private void initViews() {
        viewPager.setOffscreenPageLimit(2);
        final BaseFragment[] fragments = {new CategoryActivityFragment().getInstance(categorieId),
                new CategoryGoodsFragment().getInstance(categorieId),
                new CategoryMerchantFragment().getInstance(categorieId)};
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
    }

    @Override
    protected CharSequence getToolbarTile() {
        return getIntent().getStringExtra(CATEGORY_NAME_KEY);
    }
}
