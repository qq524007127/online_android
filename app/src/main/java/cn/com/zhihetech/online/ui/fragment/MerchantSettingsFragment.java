package cn.com.zhihetech.online.ui.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;

import com.easemob.chat.EMChatManager;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.core.common.ActivityStack;
import cn.com.zhihetech.online.core.util.SharedPreferenceUtils;
import cn.com.zhihetech.online.ui.activity.ChangePasswordActivity;
import cn.com.zhihetech.online.ui.activity.LoginActivity;

/**
 * Created by ShenYunjie on 2016/3/1.
 */
@ContentView(R.layout.content_merchant_settings)
public class MerchantSettingsFragment extends BaseFragment {

    @Event({R.id.merchant_change_password_btn, R.id.merchant_exit_app_btn})
    private void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.merchant_exit_app_btn:
                new AlertDialog.Builder(getContext())
                        .setTitle(R.string.tip)
                        .setMessage("确定要退出当前账号吗？")
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                logoutAccount();
                            }
                        })
                        .setNegativeButton(R.string.cancel, null)
                        .show();
                break;
            case R.id.merchant_change_password_btn:
                Intent intent = new Intent(getContext(), ChangePasswordActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * 退出当前登录账号
     */
    private void logoutAccount() {
        SharedPreferenceUtils.getInstance(getContext()).clear();
        ActivityStack.getInstance().removeWithout(getActivity());
        EMChatManager.getInstance().logout(null);
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}
