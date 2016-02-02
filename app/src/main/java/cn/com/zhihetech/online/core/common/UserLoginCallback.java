package cn.com.zhihetech.online.core.common;

import android.content.Context;
import android.support.annotation.NonNull;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;

import cn.com.zhihetech.online.bean.Token;
import cn.com.zhihetech.online.core.http.ResponseMessageCallback;
import cn.com.zhihetech.online.core.util.SharedPreferenceUtils;
import cn.com.zhihetech.online.core.view.ZhiheApplication;

/**
 * 用户登录回调接口，登录成功后需再次登录环信、极光推送等第三方账号
 * Created by ShenYunjie on 2016/1/29.
 */
public abstract class UserLoginCallback extends ResponseMessageCallback<Token> {

    private Context mContext;
    SharedPreferenceUtils preferenceUtils;
    private String userCode;
    private String userPwd;
    private Token token;

    private boolean isLoged = false;

    public UserLoginCallback(Context mContext, @NonNull String userCode, @NonNull String userPwd) {
        this.mContext = mContext;
        this.userCode = userCode;
        this.userPwd = userPwd;
        this.preferenceUtils = SharedPreferenceUtils.getInstance(this.mContext);
    }

    /**
     * 环信登录回调
     */
    private EMCallBack emCallBack = new EMCallBack() {

        @Override
        public void onSuccess() {
            onEMLoginSuccess(token.getUser().getUserName());
            onLoginSuccess(token);
            onLoginFinished();
        }

        @Override
        public void onError(int i, String s) {
            onLoginFail(new RuntimeException(s));
            onLoginFinished();
        }

        @Override
        public void onProgress(int i, String s) {

        }
    };

    @Override
    public void onResponseMessage(ResponseMessage<Token> responseMessage) {
        if (responseMessage.getCode() != ResponseStateCode.SUCCESS) {
            onLoginFail(new RuntimeException(responseMessage.getMsg()));
            return;
        }
        this.token = responseMessage.getData();
        ZhiheApplication.getInstance().setUserId(token.getUser().getUserId()).setUser(responseMessage.getData().getUser());
        preferenceUtils.setUserToken(token.getToken());
        preferenceUtils.setUserMobileNum(userCode);
        preferenceUtils.setUserPassword(userPwd);
        isLoged = true;
        //onLoginSuccess(this.token);
    }

    @Override
    public final void onFinished() {
        if (!isLoged) { //如果平台账号未登录成功则回调onLoginFinish接口
            onLoginFinished();
        } else if (isLoged) {   //如果平台账号登录成功，则登录环信账号
            loginEMChat();
        }
    }

    /**
     * 登录环信账号
     */
    private void loginEMChat() {
        if (!EMChat.getInstance().isLoggedIn()) {
            String userName = ZhiheApplication.getInstance().getEMChatUserName();
            String pwd = ZhiheApplication.getInstance().getEMChatPassword();
            EMChatManager.getInstance().login(userName, pwd, emCallBack);
        } else {
            onLoginSuccess(this.token);
            onEMLoginSuccess(token.getUser().getUserName());
        }
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        super.onError(ex, isOnCallback);
        onLoginFail(ex);
        //onFinished();
    }

    /**
     * 环信登录成功后回调
     *
     * @param userNick
     */
    private void onEMLoginSuccess(String userNick) {
        updateUserNick(userNick);
        EMGroupManager.getInstance().loadAllGroups();
        EMChatManager.getInstance().loadAllConversations();
    }

    /**
     * 设置环信用户昵称
     *
     * @param userNick
     */
    private void updateUserNick(final String userNick) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                EMChatManager.getInstance().updateCurrentUserNick(userNick);
            }
        }).start();
    }

    /**
     * 登录成功后回调
     *
     * @param token
     */
    public abstract void onLoginSuccess(Token token);

    /**
     * 登录失败后回调
     *
     * @param ex
     */
    public abstract void onLoginFail(Throwable ex);

    /**
     * 登录完成回调
     */
    public abstract void onLoginFinished();
}
