package cn.com.zhihetech.online.core;

import android.app.Application;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.easemob.chat.EMChatManager;

import java.util.HashMap;
import java.util.Map;

import cn.com.zhihetech.online.bean.Merchant;
import cn.com.zhihetech.online.bean.User;
import cn.com.zhihetech.online.core.common.ActivityStack;
import cn.com.zhihetech.online.core.common.Constant;
import cn.com.zhihetech.online.core.common.CrashHandler;
import cn.com.zhihetech.online.core.common.ServiceStack;
import cn.com.zhihetech.online.core.emchat.helpers.EMChatHelper;
import cn.com.zhihetech.online.core.util.SharedPreferenceUtils;
import cn.com.zhihetech.online.core.util.XUtilHelper;
import cn.com.zhihetech.online.ui.activity.LoginActivity;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by ShenYunjie on 2016/1/15.
 */
public class ZhiheApplication extends Application {

    private final static String LOGIN_MERCHANT_EXT_KEY = "__current_loged_merchant_";
    private final static String LOGIN_USER_EXT_KEY = "_current_loged_user_";

    public final static int COMMON_USER_TYPE = Constant.COMMON_USER;    //普通用户
    public final static int MERCHANT_USER_TYPE = Constant.MERCHANT_USER;    //商家用户

    protected static ZhiheApplication instance;
    protected int userType = COMMON_USER_TYPE;
    protected boolean loged = false;    //是否已登录

    private Map<String, Object> extAttribute = new HashMap<>();

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initAppFragment();
    }

    /**
     * 初始化App框架
     */
    private void initAppFragment() {
        initCrashHandler();
        XUtilHelper.getInstance().init(this, Constant.DEBUG);
        EMChatHelper.getInstance().initEMChat(this);
        initJPush();
    }

    /**
     * 程序崩溃处理
     */
    private void initCrashHandler() {
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
    }

    /**
     * 初始化极光推送
     */
    private void initJPush() {
        JPushInterface.setDebugMode(Constant.DEBUG);
        JPushInterface.init(this);
        JPushInterface.requestPermission(this);
    }

    public static ZhiheApplication getInstance() {
        return instance;
    }

    /**
     * 获取当前登录用户（商家）的ID
     *
     * @return
     */
    public String getLogedUserId() {
        if (!loged) {
            return null;
        }
        switch (userType) {
            case MERCHANT_USER_TYPE:
                return getLogedMerchant().getMerchantId();
            case COMMON_USER_TYPE:
                return this.getLogedUser().getUserId();
        }
        return null;
    }

    /**
     * 获取当前登录普通用户
     *
     * @return
     */
    public User getLogedUser() {
        if (!loged || userType != COMMON_USER_TYPE) {
            return null;
        }
        Object tmp = getExtAttribute(LOGIN_USER_EXT_KEY);
        if (tmp == null || !(tmp instanceof User)) {
            return null;
        }
        return (User) tmp;
    }

    /**
     * 获取当前登录的商家
     *
     * @return
     */
    public Merchant getLogedMerchant() {
        if (!loged || userType != MERCHANT_USER_TYPE) {
            return null;
        }
        return (Merchant) getExtAttribute(LOGIN_MERCHANT_EXT_KEY);
    }

    /**
     * 获取环信用户名
     *
     * @return
     */
    public String getChatUserId() {
        if (!loged) {
            return null;
        }
        if (userType == MERCHANT_USER_TYPE) {
            return getLogedMerchant().getEMUserId();
        } else {
            return getLogedUser().getEMUserId();
        }
    }

    /**
     * 获取环信登录密码
     *
     * @return
     */
    public String getChatPassword() {
        String chatPwd = null;
        if (loged) {
            switch (userType) {
                case COMMON_USER_TYPE:
                    chatPwd = getLogedUser().getEMUserPwd();
                    break;
                case MERCHANT_USER_TYPE:
                    chatPwd = getLogedMerchant().getEMUserPwd();
                    break;
            }
        }
        return chatPwd;
    }

    public int getUserType() {
        return userType;
    }

    /**
     * 添加扩展信息（如果key已经存在则覆盖之前的数据）
     *
     * @param key
     * @param val
     */
    public ZhiheApplication addExtAttribute(String key, Object val) {
        this.extAttribute.put(key, val);
        return this;
    }


    /**
     * 移除扩展消息
     *
     * @param key
     * @return
     */
    public ZhiheApplication removeExtAttribute(String key) {
        this.extAttribute.remove(key);
        return this;
    }

    /**
     * 获取扩展信息
     *
     * @param key
     * @return
     */
    public Object getExtAttribute(String key) {
        return this.extAttribute.get(key);
    }


    /**
     * 替换当前登录的User（用户）
     *
     * @param user
     */
    public void replaceUser(User user) {
        addExtAttribute(LOGIN_USER_EXT_KEY, user);
    }

    /**
     * 替换当前登录的Merchant(商家）
     *
     * @param merchant
     */
    public void replaceMerchatn(Merchant merchant) {
        addExtAttribute(LOGIN_MERCHANT_EXT_KEY, merchant);
    }

    /**
     * 普通用户登录成功之后回调此方法
     *
     * @param user
     */
    public ZhiheApplication onUserLoged(@NonNull User user) {
        addExtAttribute(LOGIN_USER_EXT_KEY, user);
        this.userType = COMMON_USER_TYPE;
        onLoged();
        return this;
    }


    /**
     * 商家登录成功回调
     *
     * @param merchant
     * @return
     */
    public ZhiheApplication onMerchantLoged(@NonNull Merchant merchant) {
        addExtAttribute(LOGIN_MERCHANT_EXT_KEY, merchant);
        this.userType = MERCHANT_USER_TYPE;
        onLoged();
        return this;
    }

    public ZhiheApplication onLoged() {
        loged = true;
        if (JPushInterface.isPushStopped(this)) {
            JPushInterface.resumePush(this);
        }
        return this;
    }

    /**
     * 程序崩溃是调用
     *
     * @return
     */
    public ZhiheApplication onAppCrash() {
        ActivityStack.getInstance().clearActivity();
        ServiceStack.getInstance().forceClearService();
        this.extAttribute.clear();
        loged = false;
        return this;
    }

    /**
     * 退出当前账号
     */
    public ZhiheApplication onExitAccount() {
        ServiceStack.getInstance().clearService();  //停止后台服务
        SharedPreferenceUtils.getInstance(this).clear();    //清除用户记录
        EMChatManager.getInstance().logout(null);   //退出环信账号
        JPushInterface.stopPush(this);  //停止推送服务
        this.extAttribute.clear();
        loged = false;
        return this;
    }

    /**
     * 退出程序
     *
     * @return
     */
    public ZhiheApplication onExitApp() {
        ActivityStack.getInstance().clearActivity();
        //SharedPreferenceUtils.getInstance(this).clearToken();
        return this;
    }
}
