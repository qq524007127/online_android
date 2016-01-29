package cn.com.zhihetech.online.core.util;

/**
 * Created by ShenYunjie on 2016/1/29.
 */

import android.util.Base64;

import java.io.UnsupportedEncodingException;

public class PasswordUtils {
    private static final String SALF_VALUE = "_zhihe_config";
    private static final String SEPARATE = "#";

    public static String decodePassword(String password) {
        if (StringUtils.isEmpty(password))
            return null;
        try {
            String str = new String(Base64.decode(new String(Base64.decode(password.getBytes("UTF-8"), 0), "UTF-8").split("#")[0].getBytes("UTF-8"), 0), "UTF-8");
            return str;
        } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
            localUnsupportedEncodingException.printStackTrace();
        }
        return null;
    }

    public static String encodePassword(String paramString) {
        try {
            String str1 = Base64.encodeToString(paramString.getBytes("UTF-8"), 0);
            String str2 = Base64.encodeToString((str1 + "#_zhihe_config").getBytes("UTF-8"), 0);
            return str2;
        } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
            localUnsupportedEncodingException.printStackTrace();
        }
        return null;
    }
}
