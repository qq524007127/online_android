package cn.com.zhihetech.online.ui.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.TextView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.core.util.StringUtils;
import cn.com.zhihetech.online.core.view.ZhiheSwipeRefreshLayout;
import cn.com.zhihetech.online.core.view.ZhiheWebView;

/**
 * Created by ShenYunjie on 2016/2/22.
 */
@ContentView(R.layout.activity_activity_info)
public class ActivityInfoActivity extends BaseActivity {

    public final static String ACTIVITY_ID_KEY = "ACTIVITY_ID";
    public final static String ACTIVITY_NAME_KEY = "ACTIVITY_NAME";

    @ViewInject(R.id.activity_name_tv)
    private TextView activitNameTv;
    @ViewInject(R.id.activity_container_zsrl)
    private ZhiheSwipeRefreshLayout containerRefreshLayout;
    @ViewInject(R.id.activity_container_wv)
    private ZhiheWebView webView;

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
        containerRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                webView.reload();
            }
        });
        initWebView();
    }

    private void initWebView() {
        webView.loadUrl("http://www.baidu.com");
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                setActivityName(title);
            }
        });
        webView.setListener(new ZhiheWebView.OnWebViewEventListener() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                containerRefreshLayout.setRefreshing(true);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                containerRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
            return;
        }
        super.onBackPressed();
    }

    /**
     * 设置活动的名称
     *
     * @param name
     */
    protected void setActivityName(String name) {
        this.activitNameTv.setText(name);
    }
}
