package cn.com.zhihetech.online.ui.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.text.MessageFormat;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.core.common.ResponseMessage;
import cn.com.zhihetech.online.core.common.ResponseStateCode;
import cn.com.zhihetech.online.core.http.ObjectCallback;
import cn.com.zhihetech.online.core.http.ResponseMessageCallback;
import cn.com.zhihetech.online.core.util.StringUtils;
import cn.com.zhihetech.online.model.SMSVerCodeModel;

@ContentView(R.layout.activity_regist_vercode)
public class RegisterVerCodeActivity extends BaseActivity {

    public static final String USER_MOBILE_NUM = "_user_mobile_num";
    private final int MSG_OK_CODE = 2;
    private final int MSG_WAIT_CODE = 1;

    @ViewInject(R.id.user_code_et)
    private EditText mobileNumEt;
    @ViewInject(R.id.ver_code_et)
    private EditText verCodeET;
    @ViewInject(R.id.get_code_btn)
    private Button verCodeBtn;
    @ViewInject(R.id.next_step_btn)
    private Button nextBtn;

    private ProgressDialog progressDialog;
    private Callback.Cancelable cancelable;

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_WAIT_CODE:
                    verCodeBtn.setText(StringUtils.object2String(msg.obj));
                    break;
                case MSG_OK_CODE:
                    verCodeBtn.setText(getString(R.string.get_ver_code));
                    verCodeBtn.setClickable(true);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (cancelable != null) {
                    cancelable.cancel();
                }
            }
        });
    }

    /**
     * 获取注册验证码
     *
     * @param mobileNum
     */
    private void getRegisterVerCode(String mobileNum) {
        progressDialog.show();
        new SMSVerCodeModel().getRegisterVerCode(new ResponseMessageCallback<Integer>() {
            @Override
            public void onResponseMessage(final ResponseMessage<Integer> responseMessage) {
                verCodeBtn.setClickable(false);
                final String textTpl = getString(R.string.waiting_and_get_ver_code);
                final int waitTime = responseMessage.getData() / 1000;
                String waitText = MessageFormat.format(textTpl, waitTime);
                RegisterVerCodeActivity.this.verCodeBtn.setText(waitText);
                new Thread(new Runnable() {
                    int waitCount = 0;

                    @Override
                    public void run() {
                        while (true) {
                            if (waitCount < waitTime) {
                                try {
                                    Thread.sleep(1000);
                                    waitCount++;
                                    Message msg = new Message();
                                    msg.what = MSG_WAIT_CODE;
                                    msg.obj = MessageFormat.format(textTpl, (waitTime - waitCount));
                                    handler.sendMessage(msg);
                                } catch (Exception e) {
                                }
                            } else {
                                Message msg = new Message();
                                msg.what = MSG_OK_CODE;
                                handler.sendMessage(msg);
                                break;
                            }
                        }
                    }
                }).start();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                showMsg(verCodeBtn, "获取验证码失败");
            }

            @Override
            public void onFinished() {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        }, mobileNum);
    }

    /**
     * 检查验证码输入是否正确
     *
     * @param mobileNum
     * @param verCode
     */
    private void verifyMobileNum(final String mobileNum, String verCode) {
        progressDialog.show();
        new SMSVerCodeModel().verifyMobileNum(new ObjectCallback<ResponseMessage>() {
            @Override
            public void onObject(ResponseMessage data) {
                if (data.getCode() != ResponseStateCode.SUCCESS) {
                    showMsg(nextBtn, data.getMsg());
                    return;
                }
                Intent intent = new Intent(getSelf(), RegisterUserInfoActivity.class);
                intent.putExtra(USER_MOBILE_NUM, mobileNum);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                String err = "出错了！";
                if (!StringUtils.isEmpty(ex.getMessage())) {
                    err = ex.getMessage();
                }
                showMsg(nextBtn, err);
            }

            @Override
            public void onFinished() {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        }, mobileNum, verCode);
    }

    @Event({R.id.next_step_btn, R.id.get_code_btn})
    private void onViewClick(View view) {
        String mobileNum = StringUtils.object2String(this.mobileNumEt.getText());
        String verCode = StringUtils.object2String(this.verCodeET.getText());
        switch (view.getId()) {
            case R.id.next_step_btn:
                if (!StringUtils.isMobileNum(mobileNum)) {
                    this.mobileNumEt.setError("请输入正确的手机号码");
                    this.mobileNumEt.requestFocus();
                    return;
                }
                if (StringUtils.isEmpty(verCode)) {
                    this.verCodeET.setError("请输入正确的验证码");
                    this.verCodeET.requestFocus();
                    return;
                }
                progressDialog.setMessage("号码验证中...");
                verifyMobileNum(mobileNum, verCode);
                return;
            case R.id.get_code_btn:
                if (!StringUtils.isMobileNum(mobileNum)) {
                    this.mobileNumEt.setError("请输入正确的手机号码");
                    return;
                }
                progressDialog.setMessage("正在获取验证码...");
                getRegisterVerCode(mobileNum);
                break;
        }
    }
}