package cn.com.zhihetech.online.ui.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.core.ZhiheApplication;
import cn.com.zhihetech.online.core.common.ResponseMessage;
import cn.com.zhihetech.online.core.common.ResponseStateCode;
import cn.com.zhihetech.online.core.http.ObjectCallback;
import cn.com.zhihetech.online.core.util.SharedPreferenceUtils;
import cn.com.zhihetech.online.core.util.StringUtils;
import cn.com.zhihetech.online.model.MerchantModel;
import cn.com.zhihetech.online.model.UserModel;

/**
 * Created by ShenYunjie on 2016/3/4.
 */
@ContentView(R.layout.activity_change_password)
public class ChangePasswordActivity extends BaseActivity {

    @ViewInject(R.id.old_password)
    private EditText oldPwdEt;
    @ViewInject(R.id.new_password)
    private EditText newPwdEt;
    @ViewInject(R.id.re_new_password)
    private EditText reNewEt;
    @ViewInject(R.id.change_pwd_btn)
    private Button changeBtn;

    private ProgressDialog progressDialog;

    @Event({R.id.change_pwd_btn})
    private void onViewClick(View view) {
        final String oldPwd = this.oldPwdEt.getText().toString();
        final String newPwd = this.newPwdEt.getText().toString();
        String rePwd = this.reNewEt.getText().toString();
        if (!checkInputData(oldPwd, newPwd, rePwd)) {
            return;
        }
        new AlertDialog.Builder(this)
                .setMessage("确定要修改当前登录密码吗？")
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ZhiheApplication application = ZhiheApplication.getInstance();
                        int userType = application.getUserType();
                        switch (userType) {
                            case ZhiheApplication.COMMON_USER_TYPE:
                                changeUserPwd(oldPwd, newPwd);
                                break;
                            case ZhiheApplication.MERCHANT_USER_TYPE:
                                merchantChangePwd(oldPwd, newPwd);
                                break;
                        }
                    }
                })
                .show();
    }

    /**
     * 检查数据
     *
     * @param oldPwd
     * @param newPwd
     * @param rePwd
     * @return
     */
    private boolean checkInputData(String oldPwd, String newPwd, String rePwd) {
        String currentPwd = SharedPreferenceUtils.getInstance(this).getUserPassword();
        if (!oldPwd.equals(currentPwd)) {
            oldPwdEt.setError("原密码不正确，请重新输入");
            return false;
        }
        if (StringUtils.isEmpty(newPwd) || newPwd.length() < 6) {
            newPwdEt.setError("密码长度必须大于6位");
            return false;
        }
        if (!rePwd.equals(newPwd)) {
            reNewEt.setError("两次输入的新密码不相等，请重新输入");
            return false;
        }
        return true;
    }

    private ObjectCallback<ResponseMessage> changeCallback = new ObjectCallback<ResponseMessage>() {
        @Override
        public void onObject(ResponseMessage data) {
            if (data.getCode() == ResponseStateCode.SUCCESS) {
                showMsg(changeBtn, "密码修改成功");
            } else {
                showMsg(changeBtn, data.getMsg());
            }
        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            showMsg(changeBtn, "密码修改失败，请稍后再试！");
        }

        @Override
        public void onFinished() {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    };


    /**
     * 普通用户修改登录密码
     */
    private void changeUserPwd(String oldPwd, String newPwd) {
        progressDialog = ProgressDialog.show(this,"",getString(R.string.data_loading));
        new UserModel().changePwd(changeCallback, getUserId(), oldPwd, newPwd);
    }

    /**
     * 商家修改登录密码
     */
    private void merchantChangePwd(String oldPwd, String newPwd) {
        String adminCode = SharedPreferenceUtils.getInstance(this).getUserCode();
        progressDialog = ProgressDialog.show(this,"",getString(R.string.data_loading));
        new MerchantModel().changePwd(changeCallback, adminCode, oldPwd, newPwd);
    }
}
