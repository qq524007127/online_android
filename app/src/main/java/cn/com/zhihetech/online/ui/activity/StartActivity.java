package cn.com.zhihetech.online.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.ImgInfo;
import cn.com.zhihetech.online.bean.Merchant;
import cn.com.zhihetech.online.bean.Token;
import cn.com.zhihetech.online.core.common.Constant;
import cn.com.zhihetech.online.core.common.MerchantLoginCallback;
import cn.com.zhihetech.online.core.common.ResponseMessage;
import cn.com.zhihetech.online.core.common.ResponseStateCode;
import cn.com.zhihetech.online.core.common.UserLoginCallback;
import cn.com.zhihetech.online.core.http.ResponseMessageCallback;
import cn.com.zhihetech.online.service.CheckUpdateService;
import cn.com.zhihetech.online.core.util.ImageLoader;
import cn.com.zhihetech.online.core.util.SharedPreferenceUtils;
import cn.com.zhihetech.online.core.util.StringUtils;
import cn.com.zhihetech.online.model.MerchantModel;
import cn.com.zhihetech.online.model.SystemConfigModel;
import cn.com.zhihetech.online.model.UserModel;
import cn.jpush.android.api.JPushInterface;

@ContentView(R.layout.activity_start)
public class StartActivity extends BaseActivity {
    @ViewInject(R.id.start_img)
    private ImageView startImg;

    private boolean isFinished = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startService(new Intent(this, CheckUpdateService.class));
        loadStartImage();
        waitAndLogin();
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    /**
     * 加载启动页面
     */
    private void loadStartImage() {
        new SystemConfigModel().getStartImg(new ResponseMessageCallback<ImgInfo>() {
            @Override
            public void onResponseMessage(ResponseMessage<ImgInfo> responseMessage) {
                if (responseMessage.getCode() == ResponseStateCode.SUCCESS) {
                    ImageLoader.disPlayImage(startImg, responseMessage.getData());
                }
            }
        });
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
                /*runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(getSelf(), MerchantMainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });*/
                Intent intent = new Intent(getSelf(), MerchantMainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onLoginError(final Exception e) {
                /*runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        navigationLoginView();
                    }
                });*/
                navigationLoginView();
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
