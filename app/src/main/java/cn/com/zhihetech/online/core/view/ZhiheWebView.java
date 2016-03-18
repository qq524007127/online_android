package cn.com.zhihetech.online.core.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by ShenYunjie on 2016/2/22.
 */
public class ZhiheWebView extends WebView {

    private WebViewEventListener webViewEventListener;
    private JsInterface jsInterface;

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
        setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(final WebView view, final String url, final Bitmap favicon) {
                if (webViewEventListener != null) {
                    webViewEventListener.onPageStarted(view, url, favicon);
                }
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(final WebView view, final String url) {
                if (webViewEventListener != null) {
                    webViewEventListener.onPageFinished(view, url);
                }
                if (jsInterface != null) {
                    jsInterface.initJsRuntime();
                }
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(final WebView view, final WebResourceRequest request, final WebResourceError error) {
                super.onReceivedError(view, request, error);
                if (webViewEventListener != null) {
                    webViewEventListener.onPageError(view, request, error);
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        setWebChromeClient(new ZhiheWbChromeClient());
        setJsInterface(new ZhiheJSInterface(getContext(), this));
        initSettings();
    }

    private void initSettings() {
        WebSettings settings = this.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(true);  //设置可以访问文件
        settings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        settings.setAppCacheEnabled(true);
    }

    public void setWebViewEventListener(WebViewEventListener webViewEventListener) {
        this.webViewEventListener = webViewEventListener;
    }

    public WebViewEventListener getWebViewEventListener() {
        return webViewEventListener;
    }

    public JsInterface getJsInterface() {
        return jsInterface;
    }

    public void setJsInterface(JsInterface jsInterface) {
        this.jsInterface = jsInterface;
        addJavascriptInterface(this.jsInterface, "JSInterface");
    }

    public interface WebViewEventListener {
        /**
         * 页面开始加载
         *
         * @param view
         * @param url
         * @param favicon
         */
        void onPageStarted(WebView view, String url, Bitmap favicon);

        /**
         * 页面加载失败
         *
         * @param view
         * @param request
         * @param error
         */
        void onPageError(WebView view, WebResourceRequest request, WebResourceError error);

        /**
         * 页面加载完成
         *
         * @param view
         * @param url
         */
        void onPageFinished(WebView view, String url);
    }

    public interface JsInterface {
        /**
         * 跳转到指定的秒杀商品
         *
         * @param goodsId
         * @param goodsName
         */
        @JavascriptInterface
        void navigationActivityGoods(final String goodsId, final String goodsName);

        /**
         * 跳转到指定的普通商品
         *
         * @param goodsId
         * @param goodsName
         */
        @JavascriptInterface
        void navigationGoods(final String goodsId, final String goodsName);

        /**
         * 跳转到指定的商家
         *
         * @param merchantId
         * @param merchantName
         */
        @JavascriptInterface
        void navigationMerchant(final String merchantId, final String merchantName);

        /**
         * 导航的优惠券详情页
         *
         * @param couponItemId
         */
        @JavascriptInterface
        void navigationCouponItemInfo(final String couponItemId);

        /**
         * 获取当前登录用户的token
         */
        @JavascriptInterface
        void getUserToken();

        /**
         * 获取当前登录用户的ID
         */
        @JavascriptInterface
        void getUserId();

        /**
         * 初始化当前JS运行环境
         */
        @JavascriptInterface
        void initJsRuntime();

        /**
         * 拨打电话
         *
         * @param tellNumber
         */
        @JavascriptInterface
        void callPhone(@NonNull String tellNumber);

        /**
         * 开启或关闭加载框
         *
         * @param toggleState true:开启；false:关闭
         * @param msg         显示的内容
         */
        @JavascriptInterface
        void toggleProgressDialog(boolean toggleState, String msg);
    }
}
