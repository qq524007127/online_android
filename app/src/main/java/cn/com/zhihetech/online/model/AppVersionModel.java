package cn.com.zhihetech.online.model;

import org.xutils.common.Callback;

import cn.com.zhihetech.online.bean.AppVersion;
import cn.com.zhihetech.online.core.common.Constant;
import cn.com.zhihetech.online.core.http.ResponseMessageCallback;

/**
 * Created by ShenYunjie on 2016/3/14.
 */
public class AppVersionModel extends BaseModel<AppVersion> {

    /**
     * 获取最新App版本信息
     *
     * @param callback
     * @return
     */
    public Callback.Cancelable getLastVersion(ResponseMessageCallback<AppVersion> callback) {
        return getResponseMessage(Constant.APP_NEW_VERSION_URL, null, callback);
    }
}
