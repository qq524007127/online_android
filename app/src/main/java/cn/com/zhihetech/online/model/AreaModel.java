package cn.com.zhihetech.online.model;

import org.xutils.common.Callback;

import cn.com.zhihetech.online.bean.Area;
import cn.com.zhihetech.online.core.common.Constant;
import cn.com.zhihetech.online.core.http.ArrayCallback;

/**
 * Created by ShenYunjie on 2016/1/29.
 */
public class AreaModel extends BaseModel<Area> {

    /**
     * 获取市级区域
     *
     * @param callback
     * @return
     */
    public Callback.Cancelable getCityAreas(ArrayCallback<Area> callback) {
        return getArray(Constant.CITY_AREAS_URL, null, callback);
    }
}
