package cn.com.zhihetech.online.ui.activity;

import android.view.View;
import android.widget.EditText;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.core.util.StringUtils;

/**
 * Created by ShenYunjie on 2016/2/26.
 */
@ContentView(R.layout.activity_merchant_login)
public class MerchantLoginActivity extends BaseActivity {

    @ViewInject(R.id.merchant_code_et)
    private EditText merchantCodeEt;
    @ViewInject(R.id.merchant_pwd_et)
    private EditText merchantPwdEt;

    @Event({R.id.merchant_login_btn})
    private void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.merchant_login_btn:
                doLogin();
                break;
        }
    }

    private void doLogin() {
        String merchantCode = merchantCodeEt.getText().toString();
        String merchantPwd = merchantPwdEt.getText().toString();
        if (StringUtils.isEmpty(merchantCode) || merchantCode.length() < 2) {
            merchantCodeEt.setError("登录账号长度最小为2位");
            return;
        }
        if (StringUtils.isEmpty(merchantPwd) || merchantPwd.length() < 6) {
            merchantPwdEt.setError("登录密码长度最小为6位");
            return;
        }
    }
}
