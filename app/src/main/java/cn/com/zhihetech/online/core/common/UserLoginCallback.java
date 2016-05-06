package cn.com.zhihetech.online.core.common;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;

import org.xutils.ex.DbException;

import java.util.HashSet;
import java.util.Set;

import cn.com.zhihetech.online.bean.EMUserInfo;
import cn.com.zhihetech.online.bean.Token;
import cn.com.zhihetech.online.bean.User;
import cn.com.zhihetech.online.core.ZhiheApplication;
import cn.com.zhihetech.online.core.db.DBUtils;
import cn.com.zhihetech.online.core.http.ResponseMessageCallback;
import cn.com.zhihetech.online.core.util.SharedPreferenceUtils;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * 用户登录回调接口，登录成功后需再次登录环信、极光推送等第三方账号
 * Created by ShenYunjie on 2016/1/29.
 */
public abstract class UserLoginCallback extends ResponseMessageCallback<Token> {

    private Context mContext;
    private SharedPreferenceUtils preferenceUtils;
    private String userCode;
    private String userPwd;
    private Token token;

    public UserLoginCallback(Context mContext, @NonNull String userCode, @NonNull String userPwd) {
        this.mContext = mContext;
        this.userCode = userCode;
        this.userPwd = userPwd;
        this.preferenceUtils = SharedPreferenceUtils.getInstance(this.mContext);
    }

    private final int LOGIN_SUCCESS_CODE = 1;
    private final int LOGIN_ERROR_CODE = 2;

    Handler emLoginHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOGIN_SUCCESS_CODE:
                    onEMLoginSuccess(token.getUser().getUserName());
                    onLoginSuccess(token);
                    onLoginFinished();
                    break;
                case LOGIN_ERROR_CODE:
                    String s = String.valueOf(msg.obj);
                    onLoginFail(new RuntimeException(s));
                    onLoginFinished();
                    break;
            }
        }
    };

    /**
     * 环信登录回调
     */
    private EMCallBack emCallBack = new EMCallBack() {

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

    @Override
    public void onResponseMessage(ResponseMessage<Token> responseMessage) {
        if (responseMessage.getCode() != ResponseStateCode.SUCCESS) {
            onLoginFail(new RuntimeException(responseMessage.getMsg()));
            return;
        }
        this.token = responseMessage.getData();
        initApp(this.token.getUser());
    }

    protected void initApp(User user) {
        ZhiheApplication.getInstance().onUserLoged(user);

        preferenceUtils.setUserType(Constant.COMMON_USER);
        preferenceUtils.setUserToken(token.getToken());
        preferenceUtils.setUserCode(userCode);
        preferenceUtils.setUserPassword(userPwd);

        saveUserInfo(token.getUser());

        initJPushAliasAndTags(this.token.getUser());
    }

    /**
     * 设置极光推送的别名和标签
     *
     * @param user
     */
    protected void initJPushAliasAndTags(User user) {
        Set<String> tags = new HashSet<>();
        tags.add(user.isSex() ? "男" : "女");
        tags.add(user.getIncome());
        tags.add(user.getOccupation());
        tags.add(String.valueOf(user.getAge()));
        tags.add(user.getArea().getAreaId().replaceAll("-", ""));
        JPushInterface.setAliasAndTags(mContext, user.getUserId().replaceAll("-", ""), JPushInterface.filterValidTags(tags), new TagAliasCallback() {

            @Override
            public void gotResult(int i, String s, Set<String> set) {
                System.out.println(i);
                System.out.println(s);
            }
        });
    }

    /**
     * 保存当前登录用户的环信用户信息到本地数据库
     */
    protected void saveUserInfo(User user) {
        String header = null;
        if (user.getPortrait() != null) {
            header = user.getPortrait().getUrl();
        }
        EMUserInfo userInfo = new EMUserInfo(user.getEMUserId(), user.getUserName(), header,
                user.getUserId(), Constant.EXTEND_NORMAL_USER);
        try {
            new DBUtils().saveUserInfo(userInfo);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Override
    public final void onFinished() {
        if (this.token == null) { //如果平台账号未登录成功则回调onLoginFinish接口
            onLoginFinished();
        } else {   //如果平台账号登录成功，则登录环信账号
            loginEMChat(this.token.getUser());
        }
    }

    /**
     * 登录环信账号
     */
    private void loginEMChat(User user) {
        String emUserId = user.getEMUserId();
        String emUserPwd = user.getEMUserPwd();
        EMChatManager.getInstance().login(user.getEMUserId(), user.getEMUserPwd(), emCallBack);
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        super.onError(ex, isOnCallback);
        onLoginFail(ex);
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
