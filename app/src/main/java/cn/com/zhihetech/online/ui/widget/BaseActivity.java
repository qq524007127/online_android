package cn.com.zhihetech.online.ui.widget;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.core.common.ActivityStack;
import cn.com.zhihetech.online.core.common.Constant;
import cn.com.zhihetech.online.core.view.ZhiheApplication;

/**
 * Created by ShenYunjie on 2016/1/15.
 */
public class BaseActivity extends AppCompatActivity {

    @ViewInject(R.id.toolbar)
    protected Toolbar toolbar;

    private boolean toolbarHomeAsUp = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityStack.getInstance().addActivity(this);
        x.view().inject(this);
        initToolbar();
    }

    private void initToolbar() {
        if (toolbar == null) {
            return;
        }
        toolbar.setTitle(getToolbarTile());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(this.toolbarHomeAsUp);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityStack.getInstance().removeActivity(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 如果要在代码中自定义title则需重写此方法
     *
     * @return
     */
    protected CharSequence getToolbarTile() {
        return getTitle();
    }

    /**
     * 是否显示返回按钮
     *
     * @param toolbarHomeAsUp
     */
    protected void setToolbarHomeAsUp(boolean toolbarHomeAsUp) {
        this.toolbarHomeAsUp = toolbarHomeAsUp;
    }

    /**
     * 获取当前登录用户的ID
     *
     * @return
     */
    protected String getUserId() {
        return ZhiheApplication.getInstandce().getUserId();
    }

    protected String getTag() {
        return this.getClass().getName();
    }

    protected void log(String msg) {
        if (Constant.DEBUG) {
            log(getTag(), msg);
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

    protected void showMsg(View view, String msg) {
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show();
    }

    protected void showMsg(View view, int resId) {
        Snackbar.make(view, resId, Snackbar.LENGTH_LONG).show();
    }

    /**
     * 设置背景透明度
     *
     * @param bgAlpha
     */
    protected void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
    }
}
