package cn.com.zhihetech.online.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.Token;
import cn.com.zhihetech.online.core.common.UserLoginCallback;
import cn.com.zhihetech.online.core.util.ImageLoader;
import cn.com.zhihetech.online.core.util.SharedPreferenceUtils;
import cn.com.zhihetech.online.core.util.StringUtils;
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
        loadStartImage();
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
        final String userNum = localSharedPreferenceUtils.getUserMobileNum();
        final String password = localSharedPreferenceUtils.getUserPassword();
        if ((StringUtils.isEmpty(userNum)) || (StringUtils.isEmpty(password))) {
            navigationLoginView();
            return;
        }
        new UserModel().login(new UserLoginCallback(this, userNum, password) {
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
        }, userNum, password);
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
