package cn.com.zhihetech.online.model;

import cn.com.zhihetech.online.bean.Merchant;
import cn.com.zhihetech.online.core.common.Constant;
import cn.com.zhihetech.online.core.common.Pager;
import cn.com.zhihetech.online.core.http.PageDataCallback;

/**
 * Created by ShenYunjie on 2016/1/18.
 */
public class MerchantModel extends BaseModel<Merchant> {
    public void getDailyNewList(PageDataCallback<Merchant> callback, Pager pager) {
        ModelParams params = new ModelParams().addPager(pager);
        getPageData(Constant.DAILY_NEW_URL, params, callback);
    }
}
