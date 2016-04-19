package cn.com.zhihetech.online.core.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import cn.com.zhihetech.online.core.common.Constant;

public class SharedPreferenceUtils {
    private static final String SHARED_PREFERENCES_CONFIG = "zhihe_config";
    private static final String USER_TYPE = "zhihe_user_type";
    private static final String MOBILE_KEY = "zhihe_user_mobile";
    private static final String USER_PASSWORD_KEY = "zhihe_user_login_password";
    private static final String TOKEN_KEY = "zhihe_login_token";
    private Context mContext;

    private SharedPreferenceUtils(Context paramContext) {
        this.mContext = paramContext;
    }

    public static SharedPreferenceUtils getInstance(Context paramContext) {
        return new SharedPreferenceUtils(paramContext);
    }

    public Editor getEditor() {
        return getSharedPreferences().edit();
    }

    public SharedPreferences getSharedPreferences() {
        return this.mContext.getSharedPreferences(SHARED_PREFERENCES_CONFIG, Context.MODE_PRIVATE);
    }

    public int getUserType() {
        return getSharedPreferences().getInt(USER_TYPE, Constant.COMMON_USER);
    }

    public void setUserType(int userType) {
        getEditor().putInt(USER_TYPE, userType).commit();
    }

    public String getUserCode() {
        return getSharedPreferences().getString(MOBILE_KEY, null);
    }

    public String getUserPassword() {
        return PasswordUtils.decodePassword(getSharedPreferences().getString(USER_PASSWORD_KEY, null));
    }

    public void setUserCode(String paramString) {
        getEditor().putString(MOBILE_KEY, paramString).commit();
    }

    public void setUserPassword(String paramString) {
        String str = PasswordUtils.encodePassword(paramString);
        getEditor().putString(USER_PASSWORD_KEY, str).commit();
    }

    public void setUserToken(String token) {
        getEditor().putString(TOKEN_KEY, token).commit();
    }

    public String getUserToken() {
        return getSharedPreferences().getString(TOKEN_KEY, null);
    }

    /**
     * 清除所用保存的信息
     */
    public void clear() {
        getEditor().clear().commit();
    }

    /**
     * 清除登录token
     */
    public void clearToken() {
        getEditor().remove(TOKEN_KEY).commit();
    }
}
