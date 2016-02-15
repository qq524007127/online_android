package cn.com.zhihetech.online.core.db;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import cn.com.zhihetech.online.bean.EMUserInfo;
import cn.com.zhihetech.online.core.common.ZhiheApplication;

/**
 * Created by ShenYunjie on 2016/2/15.
 */
public class DBUtils {

    /**
     * 根据环信用户名查询环信用户基本信息
     *
     * @param userName
     * @return
     * @throws DbException
     */
    public EMUserInfo getUserInfoByUserName(String userName) throws DbException {
        return getDbManager().selector(EMUserInfo.class).where("userName", "=", userName).findFirst();
    }

    /**
     * 保存或更新环信用户信息
     *
     * @param userInfo
     * @return
     * @throws DbException
     */
    public EMUserInfo saveOrUpdateUserInfo(EMUserInfo userInfo) throws DbException {
        EMUserInfo tmp = getUserInfoByUserName(userInfo.getUserName());
        if (tmp == null) {
            getDbManager().saveBindingId(userInfo);
            return userInfo;
        }
        tmp.setAvatarUrl(userInfo.getAvatarUrl());
        tmp.setUserNick(userInfo.getUserNick());
        tmp.setUserType(userInfo.getUserType());
        getDbManager().saveOrUpdate(tmp);
        return tmp;
    }

    protected DbManager getDbManager() {
        return ZhiheApplication.getInstance().getDbManager();
    }
}
