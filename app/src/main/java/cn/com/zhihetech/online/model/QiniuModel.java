package cn.com.zhihetech.online.model;

import org.xutils.common.Callback;

import cn.com.zhihetech.online.core.common.Constant;
import cn.com.zhihetech.online.core.http.SimpleCallback;

/**
 * Created by ShenYunjie on 2016/3/11.
 */
public class QiniuModel extends BaseModel<Object> {

    /**
     * 获取七牛云存储文件上传token
     *
     * @param callback
     * @return
     */
    public Callback.Cancelable getUploadToken(SimpleCallback callback) {
        return getText(Constant.QINIU_UPLOAD_TOKEN_URL, null, callback);
    }
}
