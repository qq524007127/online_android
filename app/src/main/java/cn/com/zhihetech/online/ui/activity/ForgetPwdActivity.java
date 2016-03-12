package cn.com.zhihetech.online.ui.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.core.common.ResponseMessage;
import cn.com.zhihetech.online.core.common.ResponseStateCode;
import cn.com.zhihetech.online.core.http.ObjectCallback;
import cn.com.zhihetech.online.core.http.ResponseMessageCallback;
import cn.com.zhihetech.online.core.util.StringUtils;
import cn.com.zhihetech.online.model.SMSVerCodeModel;
import cn.com.zhihetech.online.model.UserModel;

/**
 * Created by ShenYunjie on 2016/3/12.
 */
@ContentView(R.layout.activity_forget_password)
public class ForgetPwdActivity extends BaseActivity {

    @ViewInject(R.id.mobile_num_et)
    private EditText mobileNumEt;
    @ViewInject(R.id.ver_code_et)
    private EditText verCodeEt;
    @ViewInject(R.id.get_ver_code_btn)
    private Button getVerCodeBtn;
    @ViewInject(R.id.find_password_btn)
    private Button findPwdBtn;

    @Event({R.id.get_ver_code_btn, R.id.find_password_btn})
    private void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.get_ver_code_btn:
                getVerCode();
                break;
            case R.id.find_password_btn:
                findPwd();
                break;
        }
    }

    /**
     * 找回密码
     */
    private void findPwd() {
        String mobileNum = mobileNumEt.getText().toString().trim();
        String verCode = verCodeEt.getText().toString().trim();
        if (!StringUtils.isMobileNum(mobileNum)) {
            showMsg("请输入正确的手机号码！");
            mobileNumEt.requestFocus();
            return;
        }
        if (StringUtils.isEmpty(verCode) || verCode.length() < 4) {
            showMsg("请输入正确的验证码");
            verCodeEt.requestFocus();
            return;
        }
        final ProgressDialog progressDialog = ProgressDialog.show(this, "", "正在获取验证码...");
        progressDialog.setCancelable(true);
        final Callback.Cancelable cancelable = new UserModel().findPassword(new ObjectCallback<ResponseMessage>() {
            @Override
            public void onObject(ResponseMessage data) {
                if (data.getCode() != ResponseStateCode.SUCCESS) {
                    showMsg(data.getMsg());
                    return;
                }
                new AlertDialog.Builder(getSelf())
                        .setTitle(R.string.tip)
                        .setMessage("新密码已经以短信形式发送到您的手机，请注意查收！")
                        .setPositiveButton(R.string.ok, null)
                        .show();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                showMsg("密码找回失败，请重试！");
            }

            @Override
            public void onFinished() {
                progressDialog.dismiss();
            }
        }, mobileNum, verCode);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (!cancelable.isCancelled()) {
                    cancelable.cancel();
                }
            }
        });
    }

    /**
     * 获取短信验证码
     */
    private void getVerCode() {
        String mobileNum = mobileNumEt.getText().toString().trim();
        if (!StringUtils.isMobileNum(mobileNum)) {
            showMsg(getVerCodeBtn, "请输入正确的手机号码！");
            mobileNumEt.requestFocus();
            return;
        }
        final ProgressDialog progressDialog = ProgressDialog.show(this, "", "正在获取验证码...");
        progressDialog.setCancelable(true);
        final Callback.Cancelable cancelable = new SMSVerCodeModel().getFindPwdVerifyCode(new ResponseMessageCallback<Integer>() {
            @Override
            public void onResponseMessage(ResponseMessage<Integer> res) {
                if (res.getCode() != ResponseStateCode.SUCCESS) {
                    showMsg(getVerCodeBtn, res.getMsg());
                    getVerCodeBtn.setClickable(true);
                    return;
                }
                showMsg("获取验证码成功！");
                displayWaitting(res.getData());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                getVerCodeBtn.setClickable(true);
                showMsg("验证码获取失败，请重试！");
            }

            @Override
            public void onFinished() {
                progressDialog.dismiss();
            }
        }, mobileNum);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (!cancelable.isCancelled()) {
                    cancelable.cancel();
                }
            }
        });
    }

    /**
     * 显示再次获取验证码等待时间
     *
     * @param data
     */
    private void displayWaitting(final Integer data) {
        final int MSG_WAITTING_CODE = 1;
        final int MSG_SUCCESS_CODE = 2;
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MSG_WAITTING_CODE:
                        getVerCodeBtn.setText(msg.obj.toString());
                        break;
                    case MSG_SUCCESS_CODE:
                        getVerCodeBtn.setClickable(true);
                        getVerCodeBtn.setText("获取验证码");
                        break;
                }
            }
        };

        new Thread(new Runnable() {
            int waitSecond = data / 1000;

            @Override
            public void run() {
                while (waitSecond > 0) {
                    Message msg = new Message();
                    try {
                        Thread.sleep(1000);
                        waitSecond--;
                        String txt = waitSecond + "秒后重新获取";
                        msg.what = MSG_WAITTING_CODE;
                        msg.obj = txt;
                        handler.sendMessage(msg);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Message msg = new Message();
                msg.what = MSG_SUCCESS_CODE;
                handler.sendMessage(msg);
            }
        }).start();
    }
}
