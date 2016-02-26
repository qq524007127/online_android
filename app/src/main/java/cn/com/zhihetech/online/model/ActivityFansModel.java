package cn.com.zhihetech.online.model;

import android.support.annotation.NonNull;

import org.xutils.common.Callback;

import java.text.MessageFormat;

import cn.com.zhihetech.online.bean.ActivityFans;
import cn.com.zhihetech.online.bean.User;
import cn.com.zhihetech.online.core.common.Constant;
import cn.com.zhihetech.online.core.common.Pager;
import cn.com.zhihetech.online.core.http.PageDataCallback;

/**
 * Created by ShenYunjie on 2016/2/26.
 */
public class ActivityFansModel extends BaseModel<ActivityFans> {

    public Callback.Cancelable getFansByActivityId(PageDataCallback<User> callback, Pager pager, @NonNull String activityId) {
        String url = MessageFormat.format(Constant.ACTIVITY_FANS_LIST_URL, activityId);
        ModelParams params = new ModelParams().addPager(pager);
        return new SimpleModel(User.class).getPageData(url, params, callback);
    }
}
