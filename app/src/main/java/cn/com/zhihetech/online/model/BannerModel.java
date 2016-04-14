package cn.com.zhihetech.online.model;

import org.xutils.common.Callback;

import cn.com.zhihetech.online.bean.Banner;
import cn.com.zhihetech.online.core.common.Constant;
import cn.com.zhihetech.online.core.http.ArrayCallback;

/**
 * Created by ShenYunjie on 2016/1/15.
 */
public class BannerModel extends BaseModel<Banner> {
    /**
     * 获取轮播图
     *
     * @param callback
     * @return
     */
    public Callback.Cancelable getBanners(ArrayCallback<Banner> callback) {
        return getArray(Constant.BANNERS_URL, null, callback);
    }

    /**
     * @param callback
     * @param bannerType
     * @return
     */
    public Callback.Cancelable getBannersByType(ArrayCallback<Banner> callback, int bannerType) {
        return getArray(Constant.BANNERS_URL, new ModelParams().addParam("bannerType", String.valueOf(bannerType)), callback);
    }
}
