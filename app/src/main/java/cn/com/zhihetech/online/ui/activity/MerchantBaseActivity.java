package cn.com.zhihetech.online.ui.activity;

import cn.com.zhihetech.online.core.ZhiheApplication;

/**
 * Created by ShenYunjie on 2016/2/29.
 */
public class MerchantBaseActivity extends BaseActivity {

    /**
     * 获取当前登录商家的ID
     *
     * @return
     */
    @Override
    protected String getUserId() {
        return ZhiheApplication.getInstance().getLogedMerchant().getEMUserId();
    }
}
