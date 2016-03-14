package cn.com.zhihetech.online.ui.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.text.MessageFormat;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.AppVersion;
import cn.com.zhihetech.online.core.common.ResponseMessage;
import cn.com.zhihetech.online.core.common.ResponseStateCode;
import cn.com.zhihetech.online.core.http.ResponseMessageCallback;
import cn.com.zhihetech.online.core.util.AppUtils;
import cn.com.zhihetech.online.model.AppVersionModel;

/**
 * Created by ShenYunjie on 2016/3/14.
 */
@ContentView(R.layout.activity_about_us)
public class AboutUsActivity extends BaseActivity {

    @ViewInject(R.id.add_version_name_tv)
    private TextView versionNameTv;
    @ViewInject(R.id.check_update_btn)
    private Button checkUpdateBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }

    private void initViews() {
        String txt = MessageFormat.format(getString(R.string.app_name_and_version_name), AppUtils.getVersionName(this));
        versionNameTv.setText(txt);
    }

    @Event({R.id.check_update_btn})
    private void onViewClick(View view) {
        checkUpdate();
    }

    private void checkUpdate() {
        final ProgressDialog progressDialog = ProgressDialog.show(this, "", "正在检查更新...");
        progressDialog.setCancelable(true);
        final Callback.Cancelable cancelable = new AppVersionModel().getLastVersion(new ResponseMessageCallback<AppVersion>() {
            @Override
            public void onResponseMessage(ResponseMessage<AppVersion> responseMessage) {
                if (responseMessage.getCode() != ResponseStateCode.SUCCESS) {
                    showMsg("检查更新失败！");
                    return;
                }
                int versionCode = AppUtils.getVersionCode(getSelf());
                if (versionCode >= responseMessage.getData().getVersionCode()) {
                    showMsg("已经是最新版本！");
                    return;
                }
                findNewVersion(responseMessage.getData());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                showMsg("检查更新失败！");
            }

            @Override
            public void onFinished() {
                progressDialog.dismiss();
            }
        });
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (!cancelable.isCancelled()) {
                    cancelable.cancel();
                }
            }
        });
    }

    private void findNewVersion(final AppVersion appVersion) {
        new AlertDialog.Builder(this)
                .setTitle("有新版本了")
                .setMessage(appVersion.getVersionDisc())
                .setNegativeButton("忽略", null)
                .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        Uri content_url = Uri.parse(appVersion.getVersionUrl());
                        intent.setData(content_url);
                        startActivity(intent);
                    }
                }).show();
    }
}
