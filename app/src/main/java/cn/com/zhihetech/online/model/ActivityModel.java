package cn.com.zhihetech.online.model;

import cn.com.zhihetech.online.bean.Activity;
import cn.com.zhihetech.online.core.common.Constant;
import cn.com.zhihetech.online.core.common.Pager;
import cn.com.zhihetech.online.core.http.PageDataCallback;

/**
 * Created by ShenYunjie on 2016/1/18.
 */
public class ActivityModel extends BaseModel<Activity> {
    public void getActivityPageData(PageDataCallback<Activity> callback, Pager pager) {
        ModelParams params = new ModelParams().addPager(pager);
        getPageData(Constant.ACTIVITY_LIST_URL, params, callback);
    }
}
