package cn.com.zhihetech.online.core.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by ShenYunjie on 2016/2/22.
 */
public class ZhiheWebView extends WebView {

    private OnWebViewEventListener listener;

    public ZhiheWebView(Context context) {
        this(context, null);
    }

    public ZhiheWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZhiheWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ZhiheWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public ZhiheWebView(Context context, AttributeSet attrs, int defStyleAttr, boolean privateBrowsing) {
        super(context, attrs, defStyleAttr, privateBrowsing);
        init();
    }

    private void init() {
        initSettings();
        setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (listener != null) {
                    listener.onPageStarted(view, url, favicon);
                }
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (listener != null) {
                    listener.onPageFinished(view, url);
                }
                super.onPageFinished(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    private void initSettings() {
        WebSettings settings = getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(true);  //设置可以访问文件
        settings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        settings.setAppCacheEnabled(true);
    }

    public void setListener(OnWebViewEventListener listener) {
        this.listener = listener;
    }

    public void jsCallBack(String target) {
        addJavascriptInterface(null, target);
    }

    public interface OnWebViewEventListener {
        void onPageStarted(WebView view, String url, Bitmap favicon);

        void onPageFinished(WebView view, String url);
    }
}
