package cn.com.zhihetech.online.core.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

/**
 * Created by ShenYunjie on 2016/2/22.
 */
public class WebViewUtils {

    private WebViewEventListener webViewEventListener;
    private JsInterface jsInterface;

    private Context mContext;
    private WebView webView;

    private final int ERROR_CODE = -1;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ERROR_CODE:
                    if (webViewEventListener != null) {
                        webViewEventListener.onPageError(webView, null, null);
                    }
                    Toast.makeText(mContext, "加载页面出错！onReceivedError()", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    public WebViewUtils(Context mContext, WebView webView) {
        this.mContext = mContext;
        this.webView = webView;
    }

    public void setUpSettigs() {
        webView.setWebViewClient(new WebViewClient() {
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
                if (webViewEventListener != null) {
                    webViewEventListener.onPageError(webView, null, null);
                }
                super.onReceivedError(view, request, error);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        this.webView.setWebChromeClient(new ZHWebChromeClient());
        setJsInterface(new ZHJSInterface(mContext, webView));
        initSettings();
    }

    private void initSettings() {
        WebSettings settings = this.webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(true);  //设置可以访问文件
        settings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        settings.setAppCacheEnabled(true);
        settings.setUseWideViewPort(true);
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

    protected void setJsInterface(JsInterface jsInterface) {
        this.jsInterface = jsInterface;
        this.webView.addJavascriptInterface(this.jsInterface, "JSInterface");
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
         * 获取当前登录用户的账号名称
         */
        @JavascriptInterface
        void getUserId();

        /**
         * 获取当前登录用户的账号名称
         */
        @JavascriptInterface
        void getUserCode();

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
