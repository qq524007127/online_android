package cn.com.zhihetech.online.core.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.com.zhihetech.online.core.common.Constant;

/**
 * Created by ShenYunjie on 2016/1/18.
 */
public class DateUtils {
    private final static String DEFUALT_DATE_TIME_FORMAT = Constant.DEFAULT_DATE_TIME_FORMAT;
    private final static String DEFUALT_DATE_FORMAT = Constant.DEFAULT_DATE_FORMAT;

    public static String formatDate(Date date) {
        return formatDateByFormat(date, DEFUALT_DATE_FORMAT);
    }

    public static String formatDateTime(Date date) {
        return formatDateByFormat(date, DEFUALT_DATE_TIME_FORMAT);
    }

    public static String formatDateByFormat(Date date, String format) {
        return getDateFormat(format).format(date);
    }

    public static Date parseDate(String target, String format) {
        try {
            return getDateFormat(format).parse(target);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static SimpleDateFormat getDateFormat(String format) {
        return new SimpleDateFormat(format);
    }
}
