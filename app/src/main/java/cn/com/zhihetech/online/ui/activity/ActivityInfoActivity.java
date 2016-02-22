package cn.com.zhihetech.online.ui.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.core.util.StringUtils;
import cn.com.zhihetech.online.core.view.ZhiheProgressDialog;
import cn.com.zhihetech.online.core.view.ZhiheWebView;

/**
 * Created by ShenYunjie on 2016/2/22.
 */
@ContentView(R.layout.activity_activity_info)
public class ActivityInfoActivity extends BaseActivity {

    public final static String ACTIVITY_ID_KEY = "ACTIVITY_ID";
    public final static String ACTIVITY_NAME_KEY = "ACTIVITY_NAME";

    @ViewInject(R.id.activity_container_wv)
    private ZhiheWebView webView;

    private ZhiheProgressDialog progressDialog;

    private String actId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actId = getIntent().getStringExtra(ACTIVITY_ID_KEY);
        if (StringUtils.isEmpty(actId)) {
            showMsg("出错了！");
            finish();
            return;
        }
        initViews();
    }

    private void initViews() {
        progressDialog = new ZhiheProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.data_loading));
        webView.loadUrl("http://www.baidu.com");
        webView.setListener(new ZhiheWebView.OnWebViewEventListener() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                progressDialog.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                progressDialog.dismiss();
            }
        });
    }

    @Override
    protected CharSequence getToolbarTile() {
        if (!StringUtils.isEmpty(getIntent().getStringExtra(ACTIVITY_NAME_KEY))) {
            return getIntent().getStringExtra(ACTIVITY_NAME_KEY);
        }
        return super.getToolbarTile();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
