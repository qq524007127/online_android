package cn.com.zhihetech.online.model;

import cn.com.zhihetech.online.bean.Navigation;
import cn.com.zhihetech.online.core.common.Constant;
import cn.com.zhihetech.online.core.http.ArrayCallback;

/**
 * Created by ShenYunjie on 2016/1/18.
 */
public class NavigationModel extends BaseModel<Navigation> {
    public void getNavigations(ArrayCallback<Navigation> callback){
        getArray(Constant.NAVIGATIONS_URL,null,callback);
    }
}
