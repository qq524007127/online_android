package cn.com.zhihetech.online.core.service;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.User;
import cn.com.zhihetech.online.core.ZhiheApplication;
import cn.com.zhihetech.online.core.emchat.helpers.EMChatHelper;
import cn.com.zhihetech.online.core.emchat.helpers.EMConnectionHandle;

/**
 * Created by ShenYunjie on 2016/4/7.
 */
public class EMChatConnectionService extends BaseService {

    public final static int SERVICE_LEVEL = 5;

    protected final int LOGIN_FAIL_CODE = -1;
    protected final int LOGED_CODE = 1;
    protected final int RE_LOGIN_CODE = 2;

    private boolean isRetryLogin = true;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initEMChatConnectionListener();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EMChatHelper helper = EMChatHelper.getInstance();
        helper.removeConnectionListener(connectListener);
    }

    private void initEMChatConnectionListener() {
        EMChatHelper helper = EMChatHelper.getInstance();
        helper.addConnectionListener(connectListener);
    }

    private EMConnectionHandle.OnEMConnectionListener connectListener = new EMConnectionHandle.AbstractConnectionHandle() {
        @Override
        public int getLevel() {
            return SERVICE_LEVEL;
        }

        @Override
        public boolean onUserRemoved() {
            showMsg("当前用户已被删除，不能进行聊天对话！");
            return true;
        }

        @Override
        public boolean onConnectionError(boolean hasNetwork) {
            if (hasNetwork) {
                retryLogin();
            } else {
                showMsg("当前网络不可用，请链接网络！");
            }
            return true;
        }

        @Override
        public boolean onLoginConflict() {
            userLoginConflict();
            return true;
        }
    };

    private void retryLogin() {
        EMChatManager.getInstance().login(getEMChatUserName(), getEMChatUserPwd(), new EMCallBack() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(int i, String s) {
                if (!isRetryLogin) {
                    return;
                }
                Message msg = new Message();
                msg.what = RE_LOGIN_CODE;
                loginHandler.sendMessage(msg);
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }

    final Handler loginHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOGED_CODE:
                    showMsg("登录成功！");
                    break;
                case RE_LOGIN_CODE:
                    retryLogin();
                    break;
                case LOGIN_FAIL_CODE:
                    onLoginFail();
                    break;
            }
        }
    };

    private void onLoginFail() {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.tip)
                .setMessage("账号登录失败,将不能收发信息。是否重新登录？")
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isRetryLogin = false;
                    }
                })
                .setCancelable(false)
                .setPositiveButton("登录", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        reLoginEMchat();
                    }
                }).create();
        alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        alertDialog.show();
    }

    /**
     * 当前登录的环信账号已从其它设备登录
     */
    private void userLoginConflict() {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.tip)
                .setMessage("当前账号已从其它设备登录，将不能收发信息。是否重新登录？")
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isRetryLogin = false;
                    }
                })
                .setCancelable(false)
                .setPositiveButton("登录", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        reLoginEMchat();
                    }
                }).create();
        alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        alertDialog.show();
    }

    protected void reLoginEMchat() {
        User user = ZhiheApplication.getInstance().getUser();
        EMChatManager.getInstance().login(user.getEMUserId(), user.getEMPwd(), new EMCallBack() {
            Message msg = new Message();

            @Override
            public void onSuccess() {
                msg.what = LOGED_CODE;
                loginHandler.sendMessage(msg);
            }

            @Override
            public void onError(int i, String s) {
                msg.what = LOGIN_FAIL_CODE;
                loginHandler.sendMessage(msg);
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }

    protected String getEMChatUserName() {
        if (getUserType() == ZhiheApplication.COMMON_USER_TYPE) {
            return ZhiheApplication.getInstance().getUser().getEMUserId();
        }
        return ZhiheApplication.getInstance().getLogedMerchant().getEMUserId();
    }

    protected String getEMChatUserPwd() {
        if (getUserType() == ZhiheApplication.COMMON_USER_TYPE) {
            return ZhiheApplication.getInstance().getUser().getEMPwd();
        }
        return ZhiheApplication.getInstance().getLogedMerchant().getEMUserPwd();
    }

    protected int getUserType() {
        return ZhiheApplication.getInstance().getUserType();
    }
}
