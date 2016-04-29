package cn.com.zhihetech.online.ui.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.KeyEvent;
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
import cn.com.zhihetech.online.core.view.WebViewUtils;

/**
 * Created by ShenYunjie on 2016/3/17.
 */
@ContentView(R.layout.content_webview_fragment)
public class WebViewFragment extends BaseFragment {

    public final static String LOAD_URL = "_LOAD_URL";
    public final static String ENABLE_REFRESH = "_ENABLE_REFRESH";

    @ViewInject(R.id.web_view_load_error_layout_view)
    private LinearLayout errorLayout;
    @ViewInject(R.id.web_view_reload_view)
    private View reloadView;
    @ViewInject(R.id.web_view_container_ll)
    private LinearLayout containerLayout;
    @ViewInject(R.id.web_view_refresh_layout)
    private ZhiheSwipeRefreshLayout refreshLayout;
    @ViewInject(R.id.web_view_container_wv)
    private WebView webView;

    private WebViewEventHandle webViewEventHandle;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof WebViewEventHandle) {
            webViewEventHandle = (WebViewEventHandle) context;
        }
    }

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
        webView.requestFocus();
    }

    private void initViews() {
        WebViewUtils utils = new WebViewUtils(getContext(), webView);
        utils.setWebViewEventListener(new WebViewUtils.WebViewEventListener() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (!refreshLayout.isRefreshing()) {
                    refreshLayout.setRefreshing(true);
                }
                if (webViewEventHandle != null) {
                    webViewEventHandle.onPageStarted(view, url, favicon);
                }
            }

            @Override
            public void onPageError(WebView view, WebResourceRequest request, WebResourceError error) {
                //errorLayout.setVisibility(View.VISIBLE);
                containerLayout.setVisibility(View.GONE);
                if (webViewEventHandle != null) {
                    webViewEventHandle.onPageError(view);
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                refreshLayout.setRefreshing(false);
                if (webViewEventHandle != null) {
                    webViewEventHandle.onPageFinished(view, url);
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                if (webViewEventHandle != null) {
                    webViewEventHandle.onReceivedTitle(view, title);
                }
            }
        });
        utils.setUpSettigs();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                containerLayout.setVisibility(View.VISIBLE);
                webView.reload();
            }
        });
        reloadView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.reload();
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.goBack();//返回上一页面
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public interface WebViewEventHandle {
        void onPageStarted(WebView view, String url, Bitmap favicon);

        void onPageError(WebView view);

        void onPageFinished(WebView view, String url);

        void onReceivedTitle(WebView webView, String title);
    }
}
