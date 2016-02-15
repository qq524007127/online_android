package cn.com.zhihetech.online.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.Merchant;
import cn.com.zhihetech.online.core.common.ResponseMessage;
import cn.com.zhihetech.online.core.http.ObjectCallback;
import cn.com.zhihetech.online.core.util.ImageLoader;
import cn.com.zhihetech.online.core.common.ZhiheApplication;
import cn.com.zhihetech.online.model.MerchantModel;
import cn.com.zhihetech.online.ui.fragment.BaseFragment;
import cn.com.zhihetech.online.ui.fragment.MerchantActivityFragment;
import cn.com.zhihetech.online.ui.fragment.MerchantGoodsFragment;
import cn.com.zhihetech.online.ui.fragment.MerchantInfoFragment;

/**
 * Created by ShenYunjie on 2016/1/19.
 */
@ContentView(R.layout.activity_merchant_home)
public class MerchantHomeActivity extends BaseActivity {

    public final static String MERCHANT_ID_KEY = "_merchant_id";

    private String merchantId;
    private Merchant merchant;

    @ViewInject(R.id.merchant_home_topimg_iv)
    private ImageView merchantTopImg;
    @ViewInject(R.id.merchant_home_header_iv)
    private ImageView merchantHeaderImg;
    @ViewInject(R.id.merchant_home_add_friend_btn)
    private Button addFriendBtn;
    @ViewInject(R.id.merchant_home_contact_btn)
    private Button contactMerchanter;
    @ViewInject(R.id.merchant_home_tl)
    private TabLayout tabLayout;
    @ViewInject(R.id.merchant_home_vp)
    private ViewPager viewPager;

    String[] tabs = {"活动", "宝贝", "详情"};

    ObjectCallback<Merchant> merchantCallback = new ObjectCallback<Merchant>() {
        @Override
        public void onObject(Merchant data) {
            merchant = data;
            ImageLoader.disPlayImage(merchantTopImg, data.getHeaderImg());
            ImageLoader.disPlayImage(merchantHeaderImg, data.getCoverImg());
        }
    };

    ObjectCallback<ResponseMessage> checkFocusCallback = new ObjectCallback<ResponseMessage>() {
        @Override
        public void onObject(ResponseMessage data) {
            if (data.getCode() != 200) {
                addFriendBtn.setVisibility(View.VISIBLE);
            }
        }
    };

    ObjectCallback<ResponseMessage> addFriendCallback = new ObjectCallback<ResponseMessage>() {
        @Override
        public void onObject(ResponseMessage data) {
            log(JSON.toJSONString(data));
            if (data.getCode() == 200) {
                addFriendBtn.setVisibility(View.GONE);
                showMsg(addFriendBtn, R.string.add_friend_success);
            }
        }

        @Override
        public void onFinished() {
            addFriendBtn.setClickable(true);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        merchantId = getIntent().getStringExtra(MERCHANT_ID_KEY);
        initViews();
    }

    private void initViews() {
        initMerchantInfo();
        initViewPager();
    }

    private void initMerchantInfo() {
        MerchantModel model = new MerchantModel();
        model.getMerchantById(merchantCallback, merchantId);
        model.checkFucosState(checkFocusCallback, ZhiheApplication.getInstance().getUserId(), merchantId);
    }

    @Event({R.id.merchant_home_add_friend_btn, R.id.merchant_home_contact_btn})
    private void onAddFriendBtnClick(View view) {
        switch (view.getId()) {
            case R.id.merchant_home_add_friend_btn: //关注商家
                addFriendBtn.setClickable(false);
                new MerchantModel().focusMerchant(addFriendCallback, ZhiheApplication.getInstance().getUserId(), merchantId);
                break;
            case R.id.merchant_home_contact_btn:    //点击跳转到与商家发送消息

                break;
        }
    }

    private void initViewPager() {
        viewPager.setOffscreenPageLimit(2);
        final BaseFragment[] fragments = {new MerchantActivityFragment().getInstance(merchantId),
                new MerchantGoodsFragment().getInstance(merchantId),
                new MerchantInfoFragment().getInstance(merchantId)};
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
}
