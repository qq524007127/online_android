package cn.com.zhihetech.online.ui.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.Merchant;
import cn.com.zhihetech.online.core.common.MerchantLoginCallback;
import cn.com.zhihetech.online.core.util.StringUtils;
import cn.com.zhihetech.online.model.MerchantModel;

/**
 * Created by ShenYunjie on 2016/2/26.
 */
@ContentView(R.layout.activity_merchant_login)
public class MerchantLoginActivity extends MerchantBaseActivity {

    @ViewInject(R.id.merchant_code_et)
    private EditText merchantCodeEt;
    @ViewInject(R.id.merchant_pwd_et)
    private EditText merchantPwdEt;

    private ProgressDialog progressDialog;
    private boolean loginSuccess = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Event({R.id.merchant_login_btn})
    private void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.merchant_login_btn:
                doLogin();
                break;
        }
    }

    /**
     * 商家登录
     */
    private void doLogin() {
        String merchantCode = merchantCodeEt.getText().toString();
        String merchantPwd = merchantPwdEt.getText().toString();
        if (StringUtils.isEmpty(merchantCode) || merchantCode.length() < 2) {
            merchantCodeEt.setError("登录账号长度最小为2位");
            merchantCodeEt.selectAll();
            return;
        }
        if (StringUtils.isEmpty(merchantPwd) || merchantPwd.length() < 6) {
            merchantPwdEt.setError("登录密码长度最小为6位");
            merchantPwdEt.selectAll();
            return;
        }
        progressDialog = ProgressDialog.show(this, "", "正在登录，请稍等...");

        initProgress(new MerchantModel().login(new MerchantLoginCallback(this, merchantCode, merchantPwd) {
            @Override
            public void onLoginSuccess(Merchant merchant) {
                loginSuccess = true;
            }

            @Override
            public void onLoginError(final Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showMsg(e.getMessage());
                    }
                });
            }

            @Override
            public void onLoginFinish() {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                if (!loginSuccess) {
                    return;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setResult(RESULT_OK);
                        finish();
                    }
                });
            }
        }, merchantCode, merchantPwd));
    }

    private void initProgress(final Callback.Cancelable cancelable) {
        progressDialog.setCancelable(true);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                cancelable.cancel();
            }
        });
    }
}
