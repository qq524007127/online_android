package cn.com.zhihetech.online.model;

import org.xutils.common.Callback;

import cn.com.zhihetech.online.bean.ImgInfo;
import cn.com.zhihetech.online.bean.SystemConfig;
import cn.com.zhihetech.online.core.common.Constant;
import cn.com.zhihetech.online.core.http.ResponseMessageCallback;

/**
 * Created by ShenYunjie on 2016/3/24.
 */
public class SystemConfigModel extends BaseModel<SystemConfig> {

    /**
     * 获取App启动图片
     *
     * @param callback
     * @return
     */
    public Callback.Cancelable getStartImg(ResponseMessageCallback<ImgInfo> callback) {
        return new SimpleModel(ImgInfo.class).getResponseMessage(Constant.APP_START_IMG_URL, null, callback);
    }
}
