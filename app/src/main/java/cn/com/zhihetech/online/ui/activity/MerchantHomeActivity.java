package cn.com.zhihetech.online.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.easemob.easeui.EaseConstant;

import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.EMUserInfo;
import cn.com.zhihetech.online.bean.Merchant;
import cn.com.zhihetech.online.core.common.Constant;
import cn.com.zhihetech.online.core.common.ResponseMessage;
import cn.com.zhihetech.online.core.common.ResponseStateCode;
import cn.com.zhihetech.online.core.db.DBUtils;
import cn.com.zhihetech.online.core.http.ObjectCallback;
import cn.com.zhihetech.online.core.http.ResponseMessageCallback;
import cn.com.zhihetech.online.core.util.ImageLoader;
import cn.com.zhihetech.online.core.ZhiheApplication;
import cn.com.zhihetech.online.core.util.StringUtils;
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
    public final static String MERCHANT_NAME_KEY = "MERCHANT_NAME";

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

    ResponseMessageCallback<Merchant> merchantCallback = new ResponseMessageCallback<Merchant>() {
        @Override
        public void onResponseMessage(ResponseMessage<Merchant> responseMessage) {
            if (responseMessage.getCode() != ResponseStateCode.SUCCESS) {
                showMsg(responseMessage.getMsg());
                return;
            }
            merchant = responseMessage.getData();
            ImageLoader.disPlayImage(merchantTopImg, merchant.getHeaderImg());
            ImageLoader.disPlayImage(merchantHeaderImg, merchant.getCoverImg());
        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            showMsg("加载商家信息失败，请重试！");
        }
    };

    ObjectCallback<ResponseMessage> checkFocusCallback = new ObjectCallback<ResponseMessage>() {
        @Override
        public void onObject(ResponseMessage data) {
            if (data.getCode() != ResponseStateCode.SUCCESS) {
                addFriendBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addFriendBtn.setClickable(false);
                        new MerchantModel().focusMerchant(addFriendCallback, ZhiheApplication.getInstance().getUserId(), merchantId);
                    }
                });
            } else {
                addFriendBtn.setText("已是好友");
                addFriendBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showMsg("此商家已是您的好友！");
                    }
                });
            }
        }
    };

    ObjectCallback<ResponseMessage> addFriendCallback = new ObjectCallback<ResponseMessage>() {
        @Override
        public void onObject(ResponseMessage data) {
            if (data.getCode() == 200) {
                addFriendBtn.setText("已是好友");
                addFriendBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showMsg("此商家已是您的好友！");
                    }
                });
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

    @Override
    protected CharSequence getToolbarTile() {
        String merchantName = getIntent().getStringExtra(MERCHANT_NAME_KEY);
        if (!StringUtils.isEmpty(merchantName)) {
            return merchantName;
        }
        return super.getToolbarTile();
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

    @Event({R.id.merchant_home_contact_btn})
    private void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.merchant_home_contact_btn:    //点击跳转到与商家发送消息
                if (this.merchant != null) {
                    saveMerchantInfo(this.merchant);
                    Intent intent = new Intent(this, SingleChatActivity.class);
                    intent.putExtra(EaseConstant.EXTRA_USER_ID, this.merchant.getEMUserId());
                    startActivity(intent);
                }
                break;
        }
    }

    /**
     * 将商家的环信用户基本信息保存到本地数据库
     *
     * @param merchant
     * @throws DbException
     */
    private void saveMerchantInfo(Merchant merchant) {
        EMUserInfo userInfo = new EMUserInfo(merchant.getEMUserId(), merchant.getMerchName(),
                merchant.getCoverImg().getUrl(), merchantId, Constant.EXTEND_MERCHANT_USER);
        try {
            new DBUtils().saveUserInfo(userInfo);
        } catch (DbException e) {
            e.printStackTrace();
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
