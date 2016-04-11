package cn.com.zhihetech.online.core.util;

import org.xutils.DbManager;
import org.xutils.common.util.FileUtil;
import org.xutils.db.table.TableEntity;
import org.xutils.x;

import cn.com.zhihetech.online.core.ZhiheApplication;

/**
 * Created by ShenYunjie on 2016/4/11.
 */
public class XUtilHelper {

    private final int DB_VERVION = 1;
    private final String DB_NAME = "zhihe_db";
    private final String DB_DIR = "database";

    private static XUtilHelper instance;
    protected DbManager dbManager;
    private boolean initialized = false; //是否已经初始化

    private XUtilHelper() {
    }

    public static XUtilHelper getInstance() {
        if (instance == null) {
            instance = new XUtilHelper();
        }
        return instance;
    }

    public XUtilHelper init(ZhiheApplication application, boolean isDebug) {
        if (!initialized) {
            x.Ext.init(application);
            x.Ext.setDebug(isDebug);
            initDataBase();
            initialized = true;
        }
        return this;
    }

    /**
     * 初始化本地数据库
     */
    private XUtilHelper initDataBase() {
        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
                .setDbName(DB_NAME) //设置数据库名
                .setDbVersion(DB_VERVION) //设置数据库版本,每次启动应用时将会检查该版本号,
                        //发现数据库版本低于这里设置的值将进行数据库升级并触发DbUpgradeListener
                .setAllowTransaction(true)//设置是否开启事务,默认为false关闭事务
                .setTableCreateListener(new DbManager.TableCreateListener() {
                    @Override
                    public void onTableCreated(DbManager db, TableEntity<?> table) {
                        //db.getDatabase().enableWriteAheadLogging(); // 开启WAL, 对写入加速提升巨大
                    }
                })//设置数据库创建时的Listener
                .setDbDir(FileUtil.getCacheDir(DB_DIR))    //设置数据库.db文件存放的目录,默认为包名下databases目录下
                .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                    @Override
                    public void onUpgrade(DbManager db, int oldVersion, int newVersion) {

                    }
                });//设置数据库升级时的Listener,这里可以执行相关数据库表的相关修改,比如alter语句增加字段等
        this.dbManager = x.getDb(daoConfig);
        return this;
    }

    public DbManager getDbManager() {
        return this.dbManager;
    }
}
