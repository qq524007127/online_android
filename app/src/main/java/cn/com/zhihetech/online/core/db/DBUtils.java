package cn.com.zhihetech.online.core.db;

import org.xutils.DbManager;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import cn.com.zhihetech.online.bean.EMUserInfo;
import cn.com.zhihetech.online.core.util.XUtilHelper;

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
        return getDbManager().selector(EMUserInfo.class).where("user_name", "=", userName).findFirst();
    }

    /**
     * 保存或更新环信用户信息
     *
     * @param userInfo
     * @return
     * @throws DbException
     */
    public EMUserInfo saveUserInfo(EMUserInfo userInfo) throws DbException {
        getDbManager().delete(EMUserInfo.class, WhereBuilder.b("user_name", "=", userInfo.getUserName()));
        getDbManager().saveBindingId(userInfo);
        return userInfo;
    }

    protected DbManager getDbManager() {
        return XUtilHelper.getInstance().getDbManager();
    }
}
