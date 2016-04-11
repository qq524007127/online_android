package cn.com.zhihetech.online.core.common;

import android.content.Context;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;

import org.xutils.ex.DbException;

import java.util.HashSet;
import java.util.Set;

import cn.com.zhihetech.online.bean.EMUserInfo;
import cn.com.zhihetech.online.bean.Merchant;
import cn.com.zhihetech.online.bean.MerchantToken;
import cn.com.zhihetech.online.core.ZhiheApplication;
import cn.com.zhihetech.online.core.db.DBUtils;
import cn.com.zhihetech.online.core.http.ResponseMessageCallback;
import cn.com.zhihetech.online.core.util.SharedPreferenceUtils;
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

    private EMCallBack emCallback = new EMCallBack() {
        @Override
        public void onSuccess() {
            onLoginSuccess(token.getMerchant());
            onLoginFinish();
        }

        @Override
        public void onError(int i, String s) {
            onLoginError(new RuntimeException(s));
            onLoginFinish();
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
        Set<String> tags = new HashSet<>();
        JPushInterface.setAliasAndTags(mContext, merchant.getMerchantId().replaceAll("-", ""), tags);
    }

    @Override
    public void onResponseMessage(ResponseMessage<MerchantToken> responseMessage) {
        if (responseMessage.getCode() == ResponseStateCode.SUCCESS) {
            this.token = responseMessage.getData();
            initJPushAliasAndTags(this.token.getMerchant());
        } else {
            onLoginError(new RuntimeException(responseMessage.getMsg()));
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
        ZhiheApplication application = ZhiheApplication.getInstance().onMerchantLoged(this.token.getMerchant());
        /*application.setMerchant(this.token.getMerchant());
        application.setUserType(ZhiheApplication.MERCHANT_USER_TYPE);
        application.setEmChatUserName(this.token.getMerchant().getMerchantId().replaceAll("-", ""));*/
        saveMerchantInfo2File(this.token);
        try {
            saveMerchantInfo2DB(this.token.getMerchant());
        } catch (DbException e) {
            e.printStackTrace();
        }
        updateEMUserNick(this.token.getMerchant().getMerchName());
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
        String header = null;
        if (merchant.getCoverImg() != null) {
            header = merchant.getCoverImg().getUrl();
        }
        EMUserInfo userInfo = new EMUserInfo(merchant.getEMUserId(), merchant.getMerchName(), header, merchant.getEMUserId(), Constant.EXTEND_MERCHANT_USER);
        new DBUtils().saveUserInfo(userInfo);
    }

    /**
     * 登录商家对应的环信账号
     */
    protected void loginEMChat(String userName, String password) {
        EMChatManager.getInstance().login(userName, password, emCallback);
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
