package cn.com.zhihetech.online.ui.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.core.ZhiheApplication;
import cn.com.zhihetech.online.core.common.ResponseMessage;
import cn.com.zhihetech.online.core.common.ResponseStateCode;
import cn.com.zhihetech.online.core.http.ObjectCallback;
import cn.com.zhihetech.online.core.http.ResponseMessageCallback;
import cn.com.zhihetech.online.core.util.StringUtils;
import cn.com.zhihetech.online.model.SMSVerCodeModel;
import cn.com.zhihetech.online.model.UserModel;

/**
 * Created by ShenYunjie on 2016/3/11.
 */
@ContentView(R.layout.activity_my_wallet)
public class MyWalletActivity extends BaseActivity {

    @ViewInject(R.id.wallet_amount_of_money_info_tv)
    private TextView amountOfMoneyInfoTv;
    @ViewInject(R.id.amount_of_money_one)
    private Button moneyOneBtn;
    @ViewInject(R.id.amount_of_money_two)
    private Button moneyTwoBtn;
    @ViewInject(R.id.amount_of_money_three)
    private Button moneyThreeBtn;
    @ViewInject(R.id.amount_of_money_four)
    private Button moneyFourBtn;
    @ViewInject(R.id.wallet_vercode_et)
    private EditText verCodeEt;
    @ViewInject(R.id.wallet_get_vercode_btn)
    private Button getVerCodeBtn;
    @ViewInject(R.id.wallet_alipay_account_et)
    private EditText alipayAccountEt;
    @ViewInject(R.id.wallet_take_money_btn)
    private Button takeMoneyBtn;

    private ProgressDialog progressDialog;

    private Button[] moneyBtns = new Button[4];
    private float takeAmountOfMoney = 0f;
    private float walletTotalMoney = 0f;

    private ResponseMessageCallback<Float> walletMoneyCallback = new ResponseMessageCallback<Float>() {
        @Override
        public void onResponseMessage(ResponseMessage<Float> responseMessage) {
            if (responseMessage.getCode() != ResponseStateCode.SUCCESS) {
                showMsg("出错了");
                finish();
                return;
            }
            walletTotalMoney = responseMessage.getData();
            amountOfMoneyInfoTv.setText("可用金额：" + walletTotalMoney + "元");
        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            showMsg("出错了");
            finish();
        }

        @Override
        public void onFinished() {
            progressDialog.dismiss();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViewAndData();
    }

    private void initViewAndData() {
        initMoneyBtns();
        initWalletTotalMoney();
    }

    /**
     * 初始化用户钱包中的余额
     */
    private void initWalletTotalMoney() {
        progressDialog = ProgressDialog.show(this, "", getString(R.string.data_loading));
        new UserModel().getWalletTotalMoney(walletMoneyCallback, getUserId());
    }

    private void initMoneyBtns() {
        moneyBtns[0] = moneyOneBtn;
        moneyBtns[1] = moneyTwoBtn;
        moneyBtns[2] = moneyThreeBtn;
        moneyBtns[3] = moneyFourBtn;
    }

    @Event({R.id.amount_of_money_one, R.id.amount_of_money_two, R.id.amount_of_money_three,
            R.id.amount_of_money_four, R.id.wallet_get_vercode_btn, R.id.wallet_take_money_btn,
            R.id.view_wallet_take_money_result_btn})
    private void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.amount_of_money_one:
                resetMoneyBtn(view);
                break;
            case R.id.amount_of_money_two:
                resetMoneyBtn(view);
                break;
            case R.id.amount_of_money_three:
                resetMoneyBtn(view);
                break;
            case R.id.amount_of_money_four:
                resetMoneyBtn(view);
                break;
            case R.id.wallet_get_vercode_btn:
                if (takeAmountOfMoney <= 0) {
                    showMsg("请选择提现金额！");
                    return;
                } else if (takeAmountOfMoney > walletTotalMoney) {
                    showMsg("提现金额不能大于钱包总金额！");
                    return;
                }
                view.setClickable(false);
                getVerCode();
                break;
            case R.id.wallet_take_money_btn:
                checkDataAndTakeMoney();
                break;
            case R.id.view_wallet_take_money_result_btn:
                Intent resultIntent = new Intent(this, WalletTakeMoneyResultActivity.class);
                startActivity(resultIntent);
                break;
        }
    }

    /**
     * 校验数据并提现
     */
    private void checkDataAndTakeMoney() {
        if (!checkData()) {
            return;
        }
        new AlertDialog.Builder(this)
                .setTitle(R.string.tip)
                .setMessage("确定要转到指定支付宝账号吗，转账后金额不可恢复？")
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        takeMoney();
                    }
                }).show();
    }

    /**
     * 提现
     */
    private void takeMoney() {
        String verCode = verCodeEt.getText().toString().trim();
        String alipayCode = alipayAccountEt.getText().toString().trim();
        final ProgressDialog progress = ProgressDialog.show(this, "", "正在提现...");
        progress.setCancelable(true);
        final Callback.Cancelable cancelable = new UserModel().takeWalletMoney(new ObjectCallback<ResponseMessage>() {

            @Override
            public void onObject(ResponseMessage res) {
                if (res.getCode() != ResponseStateCode.SUCCESS) {
                    showMsg(takeMoneyBtn, res.getMsg());
                    return;
                }
                showMsg(takeMoneyBtn, res.getMsg());
                initWalletTotalMoney();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                showMsg(takeMoneyBtn, "申请提现失败，请重试！");
            }

            @Override
            public void onFinished() {
                progress.dismiss();
            }
        }, getUserId(), verCode, alipayCode, takeAmountOfMoney);

        progress.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (!cancelable.isCancelled()) {
                    cancelable.cancel();
                }
            }
        });
    }

    /**
     * 校验数据
     */
    private boolean checkData() {
        if (takeAmountOfMoney <= 0) {
            showMsg(takeMoneyBtn, "请选择提现金额");
            return false;
        }
        if (takeAmountOfMoney > walletTotalMoney) {
            showMsg(takeMoneyBtn, "提现金额不足");
            return false;
        }
        String verCode = verCodeEt.getText().toString().trim();
        String alipayCode = alipayAccountEt.getText().toString().trim();
        if (StringUtils.isEmpty(verCode) || verCode.length() < 4) {
            showMsg(takeMoneyBtn, "请输入正确的验证码");
            verCodeEt.requestFocus();
            return false;
        }
        if (StringUtils.isEmpty(alipayCode)) {
            showMsg(takeMoneyBtn, "请输入正确的支付宝账号");
            alipayAccountEt.requestFocus();
            return false;
        }

        return true;
    }

    /**
     * 获取验证码
     */
    private void getVerCode() {
        String num = ZhiheApplication.getInstance().getLogedUser().getUserPhone();
        final boolean success = false;    //验证码是否获取成功
        final ProgressDialog progress = ProgressDialog.show(this, "", "正在获取验证码");
        progress.setCancelable(true);
        final Callback.Cancelable cancelable = new SMSVerCodeModel().getTakeWalletMoneyVerifyCode(new ResponseMessageCallback<Integer>() {
            @Override
            public void onResponseMessage(ResponseMessage<Integer> responseMessage) {
                if (responseMessage.getCode() != ResponseStateCode.SUCCESS) {
                    showMsg("验证码获取失败，请重试！");
                    getVerCodeBtn.setClickable(true);
                    return;
                }
                showMsg("验证码获取成功");
                displayWaitting(responseMessage.getData());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                getVerCodeBtn.setClickable(true);
            }

            @Override
            public void onFinished() {
                progress.dismiss();
            }
        }, num);
        progress.setOnCancelListener(new DialogInterface.OnCancelListener() {
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

    /**
     * 重置金额选择
     *
     * @param view
     */
    private void resetMoneyBtn(View view) {
        for (Button btn : moneyBtns) {
            if (view.getId() == btn.getId()) {
                try {
                    takeAmountOfMoney = Float.valueOf(btn.getTag().toString());
                } catch (Exception e) {
                }
                if (takeAmountOfMoney > walletTotalMoney) {
                    showMsg("余额不足");
                    return;
                }
                btn.setSelected(true);
            } else {
                btn.setSelected(false);
            }
        }
    }
}
