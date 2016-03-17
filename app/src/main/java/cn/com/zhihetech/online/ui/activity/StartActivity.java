package cn.com.zhihetech.online.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.Merchant;
import cn.com.zhihetech.online.bean.Token;
import cn.com.zhihetech.online.core.common.Constant;
import cn.com.zhihetech.online.core.common.MerchantLoginCallback;
import cn.com.zhihetech.online.core.common.UserLoginCallback;
import cn.com.zhihetech.online.core.service.CheckAppUpdateService;
import cn.com.zhihetech.online.core.util.ImageLoader;
import cn.com.zhihetech.online.core.util.SharedPreferenceUtils;
import cn.com.zhihetech.online.core.util.StringUtils;
import cn.com.zhihetech.online.model.MerchantModel;
import cn.com.zhihetech.online.model.UserModel;

@ContentView(R.layout.activity_start)
public class StartActivity extends BaseActivity {
    @ViewInject(R.id.start_img)
    private ImageView startImg;

    private String url = "http://7xofn0.com1.z0.glb.clouddn.com/144947890994513979.jpg";

    private boolean isFinished = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startService(new Intent(this, CheckAppUpdateService.class));
        //loadStartImage();
        waitAndLogin();
    }

    /**
     * 加载启动页面
     */
    private void loadStartImage() {
        ImageLoader.disPlayImage(startImg, url);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isFinished = true;
    }

    private void autoLogin() {
        final SharedPreferenceUtils localSharedPreferenceUtils = SharedPreferenceUtils.getInstance(this);
        final String code = localSharedPreferenceUtils.getUserCode();
        final String password = localSharedPreferenceUtils.getUserPassword();
        int userType = localSharedPreferenceUtils.getUserType();
        if ((StringUtils.isEmpty(code)) || (StringUtils.isEmpty(password))) {
            navigationLoginView();
            return;
        }
        switch (userType) {
            case Constant.COMMON_USER:
                userLogin(code, password);
                break;
            case Constant.MERCHANT_USER:
                merchantLogin(code, password);
                break;
        }
    }

    /**
     * 商家登录
     *
     * @param code
     * @param password
     */
    private void merchantLogin(String code, String password) {
        new MerchantModel().login(new MerchantLoginCallback(this, code, password) {
            @Override
            public void onLoginSuccess(Merchant merchant) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(getSelf(), MerchantMainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }

            @Override
            public void onLoginError(final Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        navigationLoginView();
                    }
                });
            }

            @Override
            public void onLoginFinish() {
            }
        }, code, password);
    }

    /**
     * 普通用户登录
     *
     * @param code
     * @param password
     */
    private void userLogin(String code, String password) {
        new UserModel().login(new UserLoginCallback(this, code, password) {
            @Override
            public void onLoginSuccess(Token token) {
                navigationMainView();
            }

            @Override
            public void onLoginFail(Throwable ex) {
                navigationLoginView();
            }

            @Override
            public void onLoginFinished() {

            }
        }, code, password);
    }

    /**
     * 跳转到登录界面
     */
    private void navigationLoginView() {
        if (!isFinished) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void navigationMainView() {
        if (!isFinished) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    /**
     * 等待展示页面后自动登录
     */
    private void waitAndLogin() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isFinished) {
                    autoLogin();
                }
            }
        }, 3000);
    }
}
