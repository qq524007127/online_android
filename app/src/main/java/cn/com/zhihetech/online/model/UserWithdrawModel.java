package cn.com.zhihetech.online.model;


import android.support.annotation.NonNull;

import org.xutils.common.Callback;

import java.text.MessageFormat;

import cn.com.zhihetech.online.bean.UserWithdraw;
import cn.com.zhihetech.online.core.common.Constant;
import cn.com.zhihetech.online.core.common.Pager;
import cn.com.zhihetech.online.core.http.PageDataCallback;
import cn.com.zhihetech.online.model.BaseModel;
import cn.com.zhihetech.online.model.ModelParams;

/**
 * Created by ShenYunjie on 2016/3/14.
 */
public class UserWithdrawModel extends BaseModel<UserWithdraw> {

    /**
     * 获取用户申请提现记录
     *
     * @param callback
     * @param pager
     * @param userId
     * @return
     */
    public Callback.Cancelable getWithDrawResults(PageDataCallback<UserWithdraw> callback, Pager pager, @NonNull String userId) {
        String url = MessageFormat.format(Constant.USER_WITH_DRAW_RESULTS_URL, userId);
        ModelParams params = new ModelParams().addPager(pager);
        return getPageData(url, params, callback);
    }

}
