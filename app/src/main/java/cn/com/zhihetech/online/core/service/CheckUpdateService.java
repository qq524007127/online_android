package cn.com.zhihetech.online.core.service;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.WindowManager;

import cn.com.zhihetech.online.bean.AppVersion;
import cn.com.zhihetech.online.core.common.ResponseMessage;
import cn.com.zhihetech.online.core.common.ResponseStateCode;
import cn.com.zhihetech.online.core.http.ResponseMessageCallback;
import cn.com.zhihetech.online.core.util.AppUtils;
import cn.com.zhihetech.online.model.AppVersionModel;

/**
 * Created by ShenYunjie on 2016/3/14.
 */
public class CheckUpdateService extends BaseService {

    private ResponseMessageCallback<AppVersion> callback = new ResponseMessageCallback<AppVersion>() {
        boolean isError = false;

        @Override
        public void onResponseMessage(ResponseMessage<AppVersion> responseMessage) {
            if (responseMessage.getCode() == ResponseStateCode.SUCCESS) {
                AppVersion version = responseMessage.getData();
                int versionCode = AppUtils.getVersionCode(CheckUpdateService.this);
                if (versionCode < version.getVersionCode()) {
                    showAlert(version);
                }
            }
        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            isError = true;
        }

        @Override
        public void onFinished() {
            super.onFinished();
            if (isError) {
                stopSelf();
            }
        }
    };

    private void showAlert(final AppVersion version) {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("有新版本了")
                .setMessage(version.getVersionDisc())
                .setNegativeButton("忽略", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        stopSelf();
                    }
                })
                .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent()
                                .setAction("android.intent.action.VIEW")
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        Uri content_url = Uri.parse(version.getVersionUrl());
                        intent.setData(content_url);
                        startActivity(intent);
                        stopSelf();
                    }
                }).create();
        alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        alertDialog.show();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        checkUpdate();
        return super.onStartCommand(intent, flags, startId);
    }

    protected void checkUpdate() {
        new AppVersionModel().getLastVersion(callback);
    }
}
