package cn.com.zhihetech.online.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.io.Serializable;
import java.util.List;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.ShowImageInfo;
import cn.com.zhihetech.online.ui.fragment.BigImageFragment;

/**
 * Created by ShenYunjie on 2016/3/31.
 */
@ContentView(R.layout.activity_show_big_image)
public class ShowBigImageActivity extends BaseActivity {

    public final static String IMAGE_LIST_KEY = "image_list";
    public final static String CURRENT_POSITION = "current_position";

    @ViewInject(R.id.big_image_vp)
    private ViewPager imageViewPager;
    @ViewInject(R.id.show_bit_img_mark_tv)
    private TextView markTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if (bundle == null || bundle.getSerializable(IMAGE_LIST_KEY) == null) {
            return;
        }
        Serializable _tmp = bundle.getSerializable(IMAGE_LIST_KEY);
        if (!(_tmp instanceof List)) {
            showMsg("出错了！");
            finish();
            return;
        }
        showBigImageInfo((List<ShowImageInfo>) _tmp, bundle.getInt(CURRENT_POSITION, 0));
    }

    private void showBigImageInfo(final List<ShowImageInfo> imageInfos, int position) {
        if (imageInfos.size() > 0) {
            markTv.setVisibility(View.VISIBLE);
            markTv.setText((position + 1) + "/" + imageInfos.size());
        }
        imageViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                markTv.setText((position + 1) + "/" + imageInfos.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        final BigImageFragment[] fragments = new BigImageFragment[imageInfos.size()];
        for (int i = 0; i < imageInfos.size(); i++) {
            ShowImageInfo showImgInfo = imageInfos.get(i);
            BigImageFragment fragment = new BigImageFragment();
            Bundle args = new Bundle();
            args.putString(BigImageFragment.IMAGE_URL, showImgInfo.getUrl());
            args.putString(BigImageFragment.IMAGE_DESC, showImgInfo.getDesc());
            args.putBoolean(BigImageFragment.SHOW_IMAGE_DESC, showImgInfo.isShowDesc());
            fragment.setArguments(args);
            fragments[i] = fragment;
        }
        imageViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments[position];
            }

            @Override
            public int getCount() {
                return fragments.length;
            }
        });
        imageViewPager.setCurrentItem(position, false);
    }
}
