package cn.com.zhihetech.online.ui.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.Token;
import cn.com.zhihetech.online.core.common.UserLoginCallback;
import cn.com.zhihetech.online.core.util.SharedPreferenceUtils;
import cn.com.zhihetech.online.core.util.StringUtils;
import cn.com.zhihetech.online.model.UserModel;

@ContentView(R.layout.activity_login)
public class LoginActivity extends BaseActivity {
    @ViewInject(R.id.user_code_et)
    private EditText userCodeEt;
    @ViewInject(R.id.user_pwd_et)
    private EditText userPwdEt;
    @ViewInject(R.id.login_btn)
    private Button loginBtn;

    private ProgressDialog progressDialog;
    private Callback.Cancelable cancelable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }

    private void initViews() {
        this.progressDialog = new ProgressDialog(this);
        this.progressDialog.setCanceledOnTouchOutside(false);
        this.progressDialog.setMessage("正在登录...");
        this.progressDialog.setOnDismissListener(new OnDismissListener() {
            public void onDismiss(DialogInterface paramAnonymousDialogInterface) {
                if (cancelable != null && !cancelable.isCancelled()) {
                    cancelable.cancel();
                }
            }
        });
    }

    @Event({R.id.register_btn, R.id.login_btn})
    private void onViewClick(View paramView) {
        switch (paramView.getId()) {
            case R.id.register_btn:
                navigation(RegisterVerCodeActivity.class, false);
                break;
            case R.id.login_btn:
                userLogin();
                break;
        }
    }

    private void userLogin() {
        final SharedPreferenceUtils preferenceUtils = SharedPreferenceUtils.getInstance(this);
        final String userCode = StringUtils.object2String(this.userCodeEt.getText());
        final String password = StringUtils.object2String(this.userPwdEt.getText());
        if (!StringUtils.isMobileNum(userCode)) {
            this.userCodeEt.setError("手机号码格式不正确");
            this.userCodeEt.requestFocus();
            return;
        }
        if (StringUtils.isEmpty(password)) {
            this.userPwdEt.setError("登录密码不能为空");
            this.userPwdEt.requestFocus();
            return;
        }
        progressDialog.show();
        this.cancelable = new UserModel().login(new UserLoginCallback(this) {
            @Override
            public void onLoginSuccess(Token token) {
                preferenceUtils.setUserMobileNum(userCode);
                preferenceUtils.setUserPassword(password);
                Intent intent = new Intent(getSelf(), MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onLoginFail(Throwable ex) {
                String msg = "登录失败，未知错误";
                if (ex.getMessage() != null) {
                    msg = ex.getMessage();
                }
                showMsg(loginBtn, msg);
            }

            @Override
            public void onFinished() {
                progressDialog.dismiss();
            }
        }, userCode, password);
    }
}