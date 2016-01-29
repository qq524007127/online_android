package cn.com.zhihetech.online.core.util;

import java.util.regex.Pattern;

/**
 * Created by ShenYunjie on 2016/1/15.
 */
public class StringUtils {

    public static final String REGEX_MOBILE = "^(1)[3,4,5,7,8]\\d{9}$";

    public static boolean isEmpty(String target) {
        return target == null ? true : target.trim().equals("");
    }

    public static String object2String(Object target) {
        return target == null ? "" : String.valueOf(target);
    }

    public static boolean isMobileNum(String paramString) {
        return Pattern.compile(REGEX_MOBILE).matcher(paramString).matches();
    }
}
