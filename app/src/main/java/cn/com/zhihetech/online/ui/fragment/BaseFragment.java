package cn.com.zhihetech.online.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.xutils.x;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.User;
import cn.com.zhihetech.online.core.common.Constant;
import cn.com.zhihetech.online.core.ZhiheApplication;

/**
 * Created by ShenYunjie on 2016/1/15.
 */
public abstract class BaseFragment extends Fragment {
    private boolean injected = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        injected = true;
        return x.view().inject(this, inflater, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!injected) {
            x.view().inject(this, getView());
        }
    }

    /**
     * 获取当前登录用户的ID
     *
     * @return
     */
    protected String getLoginUserId() {
        return getUser().getUserId();
    }

    /**
     * 获取当前登录的User
     *
     * @return
     */
    protected User getUser() {
        return ZhiheApplication.getInstance().getLogedUser();
    }

    protected String getLogTag() {
        return this.getClass().getName();
    }

    protected void log(String msg) {
        if (Constant.DEBUG) {
            log(getLogTag(), msg);
        }
    }

    protected void log(String tag, String msg) {
        if (Constant.DEBUG) {
            Log.d(tag, msg);
        }
    }

    protected void onHttpError(Throwable ex, boolean isOnCallback) {
        if (Constant.DEBUG) {
            if (ex != null && !isOnCallback) {
                ex.printStackTrace();
            }
        }
    }

    protected void showMsg(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
    }

    protected void showMsg(int resId) {
        Toast.makeText(getContext(), resId, Toast.LENGTH_LONG).show();
    }

    protected void showMsg(View view, String msg) {
        /*Snackbar snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.normal_bg));
        snackbar.show();*/
        showMsg(msg);
    }

    protected void showMsg(View view, int resId) {
        /*Snackbar snackbar = Snackbar.make(view, resId, Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.normal_bg));
        snackbar.show();*/
        showMsg(resId);
    }

    /**
     * 相应onKeyDown事件，与Activity共享
     *
     * @param keyCode
     * @param event
     * @return boolean true:消耗了此事件;false:未消耗此事件
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }
}
