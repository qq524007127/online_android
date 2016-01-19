package cn.com.zhihetech.online.model;

import android.support.annotation.NonNull;

import java.text.MessageFormat;

import cn.com.zhihetech.online.bean.Activity;
import cn.com.zhihetech.online.core.common.Constant;
import cn.com.zhihetech.online.core.common.Pager;
import cn.com.zhihetech.online.core.http.PageDataCallback;

/**
 * Created by ShenYunjie on 2016/1/18.
 */
public class ActivityModel extends BaseModel<Activity> {
    /**
     * 获取所有已开始和即将开始的活动
     *
     * @param callback
     * @param pager
     */
    public void getActivities(PageDataCallback<Activity> callback, Pager pager) {
        ModelParams params = new ModelParams().addPager(pager);
        getPageData(Constant.ACTIVITY_LIST_URL, params, callback);
    }

    /**
     * 根据指定ID商家的已开始和即将开始的活动
     *
     * @param callback
     * @param pager
     * @param merchantId
     */
    public void getActivitiesByMerchantId(PageDataCallback<Activity> callback, Pager pager, @NonNull String merchantId) {
        ModelParams params = new ModelParams().addPager(pager);
        String url = MessageFormat.format(Constant.MERCHANT_ACTIVITIES_URL, merchantId);
        getPageData(url, params, callback);
    }
}
