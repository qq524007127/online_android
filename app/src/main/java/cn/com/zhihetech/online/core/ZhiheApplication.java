package cn.com.zhihetech.online.core;

import android.app.ActivityManager;
import android.app.Application;
import android.app.Notification;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatOptions;
import com.easemob.chat.EMMessage;
import com.easemob.chat.OnMessageNotifyListener;
import com.easemob.chat.OnNotificationClickListener;
import com.easemob.easeui.controller.EaseUI;

import org.xutils.DbManager;
import org.xutils.common.util.FileUtil;
import org.xutils.db.table.TableEntity;
import org.xutils.x;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.EMUserInfo;
import cn.com.zhihetech.online.bean.Merchant;
import cn.com.zhihetech.online.bean.User;
import cn.com.zhihetech.online.core.common.Constant;
import cn.com.zhihetech.online.core.common.CrashHandler;
import cn.com.zhihetech.online.core.util.StringUtils;
import cn.com.zhihetech.online.ui.activity.SingleChatActivity;
import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by ShenYunjie on 2016/1/15.
 */
public class ZhiheApplication extends Application {

    private final static String LOGIN_MERCHANT_EXT_KEY = "__current_loged_merchant";

    public final static int COMMON_USER_TYPE = Constant.COMMON_USER;    //普通用户
    public final static int MERCHANT_USER_TYPE = Constant.MERCHANT_USER;    //商家用户

    private final int DB_VERVION = 1;
    private final String DB_NAME = "zhihe_db";
    private final String DB_DIR = "database";

    protected static ZhiheApplication instance;
    protected String userId;
    protected User user;
    protected int userType = COMMON_USER_TYPE;
    protected String emChatUserName;

    protected DbManager dbManager;
    private Map<String, Object> extInfo = new HashMap<>();

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        x.Ext.init(this);
        x.Ext.setDebug(Constant.DEBUG);
        initDB();
        initEMChat();
        initJPush();
        initCrashHandler();
    }

    /**
     * 程序崩溃处理
     */
    private void initCrashHandler() {
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
    }

    /**
     * 初始化极光推送
     */
    private void initJPush() {
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        JPushInterface.requestPermission(this);
    }

    public static ZhiheApplication getInstance() {
        return instance;
    }

    public String getUserId() {
        switch (userType) {
            case MERCHANT_USER_TYPE:
                return getLogedMerchant().getMerchantId();
            case COMMON_USER_TYPE:
                return this.getUser().getUserId();
        }
        return null;
    }

    public User getUser() {
        return user;
    }

    public ZhiheApplication setUser(User user) {
        this.user = user;
        return this;
    }

    /**
     * 设置环信账号
     *
     * @param emChatUserName
     */
    public void setEmChatUserName(String emChatUserName) {
        this.emChatUserName = emChatUserName;
    }

    /**
     * 获取环信用户名
     *
     * @return
     */
    public String getEmChatUserName() {
        if (!StringUtils.isEmpty(this.emChatUserName)) {
            return this.emChatUserName;
        }
        return this.getUserId().replaceAll("-", "");
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    /**
     * 获取环信登录密码
     *
     * @return
     */
    public String getEMChatPassword() {
        switch (userType) {
            case COMMON_USER_TYPE:
                return getUser().getUserId();
            case MERCHANT_USER_TYPE:
                return getLogedMerchant().getMerchantId();
        }
        return this.userId;
    }

    public DbManager getDbManager() {
        return dbManager;
    }

    /**
     * 添加扩展信息（如果key已经存在则覆盖之前的数据）
     *
     * @param key
     * @param val
     */
    public void addExtInfo(String key, Object val) {
        this.extInfo.put(key, val);
    }

    /**
     * 获取扩展信息
     *
     * @param key
     * @return
     */
    public Object getExtInfo(String key) {
        return this.extInfo.get(key);
    }

    /**
     * 将当前登录商家加入扩展信息中
     *
     * @param merchant
     */
    public void setMerchant(Merchant merchant) {
        addExtInfo(LOGIN_MERCHANT_EXT_KEY, merchant);
    }

    public Merchant getLogedMerchant() {
        return (Merchant) getExtInfo(LOGIN_MERCHANT_EXT_KEY);
    }

    /**
     * 初始化本地数据库
     */
    private void initDB() {
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
    }

    /**
     * 如果app启用了远程的service，此application:onCreate会被调用2次
     * 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
     * 默认的app会在以包名为默认的process name下运行，如果查到的process name不是app的process name就立即返回
     */
    private void initEMChat() {
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        if (processAppName == null || !processAppName.equalsIgnoreCase("cn.com.zhihetech.online")) {
            Log.e("ZhiheApplication", "enter the service process!");
            return;
        }
        EMChat.getInstance().init(this);
        EMChat.getInstance().setDebugMode(Constant.DEBUG);
        EMChat.getInstance().setAutoLogin(false);
        settingEMChatOptions();
        EaseUI.getInstance().init(this);
    }

    /**
     * 设置环信配置选项
     */
    private void settingEMChatOptions() {
        //获取到配置options对象
        EMChatOptions options = EMChatManager.getInstance().getChatOptions();

        /**
         * 设置后台收到新消息后通知
         */
        options.setNotifyText(new OnMessageNotifyListener() {

            //设置自定义的文字提示
            @Override
            public String onNewMessageNotify(EMMessage message) {
                String msg = "{0}发来一条新消息！";
                EMUserInfo userInfo = EMUserInfo.createEMUserInfo(message);
                //可以根据message的类型提示不同文字，这里为一个简单的示例
                return MessageFormat.format(msg, userInfo.getUserNick());
            }

            @Override
            public String onLatestMessageNotify(EMMessage message, int fromUsersNum, int messageNum) {
                EMUserInfo userInfo = EMUserInfo.createEMUserInfo(message);
                return fromUsersNum + "个好友，发来了" + messageNum + "条消息！";
            }

            @Override
            public String onSetNotificationTitle(EMMessage emMessage) {
                return getString(R.string.you_have_new_message);
            }

            @Override
            public int onSetSmallIcon(EMMessage emMessage) {
                return R.mipmap.ic_launcher;
            }
        });

        /**
         * 设置点击新消息通知跳转界面
         */
        options.setOnNotificationClickListener(new OnNotificationClickListener() {

            @Override
            public Intent onNotificationClick(EMMessage message) {
                Intent intent = new Intent(getInstance(), SingleChatActivity.class);
                EMMessage.ChatType chatType = message.getChatType();
                if (chatType == EMMessage.ChatType.Chat) { //单聊信息
                    intent.putExtra(SingleChatActivity.USER_NAME_KEY, message.getFrom());
                } else { //群聊信息
                    intent.putExtra(SingleChatActivity.USER_NAME_KEY, message.getTo());
                }
                return intent;
            }
        });
    }

    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(info.processName, PackageManager.GET_META_DATA));
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }
}
