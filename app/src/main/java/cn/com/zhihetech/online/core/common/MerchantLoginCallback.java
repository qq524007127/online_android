package cn.com.zhihetech.online.core.common;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;

import org.xutils.ex.DbException;

import cn.com.zhihetech.online.bean.ChatUserInfo;
import cn.com.zhihetech.online.bean.EMUserInfo;
import cn.com.zhihetech.online.bean.Merchant;
import cn.com.zhihetech.online.bean.MerchantToken;
import cn.com.zhihetech.online.core.ZhiheApplication;
import cn.com.zhihetech.online.core.db.DBUtils;
import cn.com.zhihetech.online.core.http.ResponseMessageCallback;
import cn.com.zhihetech.online.core.util.SharedPreferenceUtils;
import cn.com.zhihetech.online.core.util.StringUtils;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by ShenYunjie on 2016/2/29.
 */
public abstract class MerchantLoginCallback extends ResponseMessageCallback<MerchantToken> {

    private MerchantToken token;
    private Context mContext;
    private String adminCode;
    private String adminPwd;

    SharedPreferenceUtils preferenceUtils;
    private final int LOGIN_SUCCESS_CODE = 1;
    private final int LOGIN_ERROR_CODE = 2;

    Handler emLoginHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOGIN_SUCCESS_CODE:
                    onEMLoginSuccess(token.getMerchant().getMerchName());
                    onLoginSuccess(token.getMerchant());
                    onLoginFinish();
                    break;
                case LOGIN_ERROR_CODE:
                    String s = String.valueOf(msg.obj);
                    onLoginError(new RuntimeException(s));
                    onLoginFinish();
                    break;
            }
        }
    };
    private EMCallBack emCallback = new EMCallBack() {
        @Override
        public void onSuccess() {
            Message msg = new Message();
            msg.what = LOGIN_SUCCESS_CODE;
            emLoginHandler.sendMessage(msg);
        }

        @Override
        public void onError(int i, String s) {
            Message msg = new Message();
            msg.what = LOGIN_ERROR_CODE;
            msg.obj = s;
            emLoginHandler.sendMessage(msg);
        }

        @Override
        public void onProgress(int i, String s) {

        }
    };

    public MerchantLoginCallback(Context mContext, String adminCode, String adminPwd) {
        this.mContext = mContext;
        this.adminCode = adminCode;
        this.adminPwd = adminPwd;
        preferenceUtils = SharedPreferenceUtils.getInstance(mContext);
    }

    /**
     * 设置极光推送的别名和标签
     *
     * @param merchant
     */
    protected void initJPushAliasAndTags(Merchant merchant) {
        JPushInterface.setAlias(mContext, merchant.getMerchantId().replaceAll("-", ""), null);
    }

    @Override
    public void onResponseMessage(ResponseMessage<MerchantToken> responseMessage) {
        if (responseMessage.getCode() == ResponseStateCode.SUCCESS) {
            this.token = responseMessage.getData();
            initJPushAliasAndTags(this.token.getMerchant());
        } else {
            String msg = "登录失败，请重试！";
            if (!StringUtils.isEmpty(responseMessage.getMsg())) {
                onLoginError(new RuntimeException(responseMessage.getMsg()));
            } else {
                onLoginError(new RuntimeException(msg));
            }
        }
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        super.onError(ex, isOnCallback);
        if (!isOnCallback) {
            onLoginError(new RuntimeException(ex));
        }
    }

    @Override
    public void onFinished() {
        if (this.token == null) {
            onLoginFinish();
            return;
        }
        ZhiheApplication application = ZhiheApplication.getInstance();
        Merchant merchant = this.token.getMerchant();
        merchant.setChatUserInfo(this.token.getChatUser());
        application.onMerchantLoged(merchant);
        saveMerchantInfo2File(this.token);
        try {
            saveMerchantInfo2DB(merchant);
        } catch (DbException e) {
            e.printStackTrace();
        }
        loginEMChat(application.getChatUserId(), application.getChatPassword());
    }

    /**
     * 将当前登录的商家信息保存到本地
     *
     * @param token
     */
    private void saveMerchantInfo2File(MerchantToken token) {
        preferenceUtils.setUserType(Constant.MERCHANT_USER);
        preferenceUtils.setUserToken(token.getToken());
        preferenceUtils.setUserCode(adminCode);
        preferenceUtils.setUserPassword(adminPwd);
    }

    /**
     * 保存当前登录商家的环信用户信息到本地数据库
     */
    protected void saveMerchantInfo2DB(Merchant merchant) throws DbException {
        ChatUserInfo chatUserInfo = merchant.getChatUserInfo();
        if (chatUserInfo == null) {
            return;
        }
        String header = null;
        if (!StringUtils.isEmpty(chatUserInfo.getPortraitUrl())) {
            header = chatUserInfo.getPortraitUrl();
        }
        EMUserInfo userInfo = new EMUserInfo(merchant.getEMUserId(), chatUserInfo.getNickName(), header, chatUserInfo.getAppUserId(), Constant.EXTEND_MERCHANT_USER);
        new DBUtils().saveUserInfo(userInfo);
    }

    /**
     * 登录商家对应的环信账号
     */
    protected void loginEMChat(String userName, String password) {
        EMChatManager.getInstance().login(userName, password, emCallback);
    }

    /**
     * 环信登录成功后回调
     *
     * @param userNick
     */
    private void onEMLoginSuccess(String userNick) {
        updateEMUserNick(userNick);
        EMGroupManager.getInstance().loadAllGroups();
        EMChatManager.getInstance().loadAllConversations();
    }

    protected void updateEMUserNick(final String userNick) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                EMChatManager.getInstance().updateCurrentUserNick(userNick);
            }
        }).start();
    }

    public abstract void onLoginSuccess(Merchant merchant);

    public abstract void onLoginError(Exception e);

    public abstract void onLoginFinish();
}
