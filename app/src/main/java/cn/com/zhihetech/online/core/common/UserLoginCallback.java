package cn.com.zhihetech.online.core.common;

import android.content.Context;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;

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
    private Token token;

    public UserLoginCallback(Context mContext) {
        this.mContext = mContext;
        this.preferenceUtils = SharedPreferenceUtils.getInstance(this.mContext);
    }

    /**
     * 环信登录回调
     */
    private EMCallBack emCallBack = new EMCallBack() {

        @Override
        public void onSuccess() {
            onLoginSuccess(token);
            onFinished();
        }

        @Override
        public void onError(int i, String s) {
            onLoginFail(new RuntimeException(s));
            onFinished();
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
        ZhiheApplication.getInstance().setUserId(token.getUserID());
        preferenceUtils.setUserToken(token.getToken());
        onLoginSuccess(this.token);
        //loginEMChat();
    }

    /**
     * 登录环信账号
     */
    private void loginEMChat() {
        if (!EMChat.getInstance().isLoggedIn()) {
            EMChatManager.getInstance().login(ZhiheApplication.getInstance().getEMChatUserName(), ZhiheApplication.getInstance().getEMChatPassword(), emCallBack);
        }
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        super.onError(ex, isOnCallback);
        onLoginFail(ex);
        //onFinished();
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
}
