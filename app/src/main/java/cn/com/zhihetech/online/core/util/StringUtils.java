package cn.com.zhihetech.online.core.util;

/**
 * Created by ShenYunjie on 2016/1/15.
 */
public class StringUtils {
    public static boolean isEmpty(String target) {
        return target == null ? true : target.trim().equals("");
    }

    public static String Object2String(Object target) {
        return target == null ? "" : String.valueOf(target);
    }
}
