package cn.com.zhihetech.online.core.common;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

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
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.EMUserInfo;
import cn.com.zhihetech.online.bean.User;
import cn.com.zhihetech.online.core.db.DBUtils;
import cn.com.zhihetech.online.ui.activity.ChatActivity;

/**
 * Created by ShenYunjie on 2016/1/15.
 */
public class ZhiheApplication extends Application implements Thread.UncaughtExceptionHandler {

    private final int DB_VERVION = 1;
    private final String DB_NAME = "zhihe_db";
    private final String DB_DIR = "database";

    protected static ZhiheApplication instance;
    protected String userId;
    protected User user;

    protected DbManager dbManager;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        x.Ext.init(this);
        x.Ext.setDebug(Constant.DEBUG);
        initDB();
        initEMChat();
    }

    public static ZhiheApplication getInstance() {
        return instance;
    }

    public String getUserId() {
        return userId;
    }

    public ZhiheApplication setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public User getUser() {
        return user;
    }

    public ZhiheApplication setUser(User user) {
        this.user = user;
        return this;
    }

    /**
     * 获取环信用户名
     *
     * @return
     */
    public String getEMChatUserName() {
        return this.userId.replaceAll("-", "");
    }

    /**
     * 获取环信登录密码
     *
     * @return
     */
    public String getEMChatPassword() {
        return this.userId;
    }

    public DbManager getDbManager() {
        return dbManager;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {

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
                //可以根据message的类型提示不同文字，这里为一个简单的示例
                return "你的好基友" + message.getFrom() + "发来了一条消息哦";
            }

            @Override
            public String onLatestMessageNotify(EMMessage message, int fromUsersNum, int messageNum) {
                return fromUsersNum + "个基友，发来了" + messageNum + "条消息";
            }

            @Override
            public String onSetNotificationTitle(EMMessage emMessage) {
                return getString(R.string.you_have_new_message);
            }

            @Override
            public int onSetSmallIcon(EMMessage emMessage) {
                return R.drawable.no_title_logo;
            }
        });

        /**
         * 设置点击新消息通知跳转界面
         */
        options.setOnNotificationClickListener(new OnNotificationClickListener() {

            @Override
            public Intent onNotificationClick(EMMessage message) {
                Intent intent = new Intent(getInstance(), ChatActivity.class);
                EMMessage.ChatType chatType = message.getChatType();
                if (chatType == EMMessage.ChatType.Chat) { //单聊信息
                    intent.putExtra(ChatActivity.USER_NAME_KEY, message.getFrom());
                    //intent.putExtra("chatType", ChatActivity.CHATTYPE_SINGLE);
                } else { //群聊信息
                    //message.getTo()为群聊id
                    intent.putExtra(ChatActivity.USER_NAME_KEY, message.getTo());
                    //intent.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
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
