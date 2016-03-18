package cn.com.zhihetech.online.ui.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.widget.LinearLayout;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.core.util.StringUtils;
import cn.com.zhihetech.online.core.view.ZhiheSwipeRefreshLayout;
import cn.com.zhihetech.online.core.view.ZhiheWebView;

/**
 * Created by ShenYunjie on 2016/3/17.
 */
@ContentView(R.layout.content_webview_fragment)
public class WebViewFragment extends BaseFragment {

    public final static String LOAD_URL = "_LOAD_URL";
    public final static String ENABLE_REFRESH = "_ENABLE_REFRESH";

    @ViewInject(R.id.webview_load_error_layout_view)
    private LinearLayout errorLayout;
    @ViewInject(R.id.webview_reload_view)
    private View reloadView;
    @ViewInject(R.id.webview_refresh_layout)
    private ZhiheSwipeRefreshLayout refreshLayout;
    @ViewInject(R.id.webview_container_wv)
    private ZhiheWebView webView;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle args = getArguments();
        if (args == null) {
            return;
        }
        String url = args.getString(LOAD_URL);
        refreshLayout.setEnabled(args.getBoolean(ENABLE_REFRESH, true));
        if (!StringUtils.isEmpty(url)) {
            initAndLoadPageByUrl(url);
        } else {
            refreshLayout.setEnabled(false);
        }
    }

    private void initAndLoadPageByUrl(String url) {
        initViews();
        webView.loadUrl(url);
    }

    private void initViews() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                webView.reload();
            }
        });
        reloadView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.reload();
            }
        });
        webView.setWebViewEventListener(new ZhiheWebView.WebViewEventListener() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (!refreshLayout.isRefreshing()) {
                    refreshLayout.setRefreshing(true);
                }
            }

            @Override
            public void onPageError(WebView view, WebResourceRequest request, WebResourceError error) {
                errorLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                refreshLayout.setRefreshing(false);
            }
        });
    }
}
