package cn.com.zhihetech.online.core.view;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Message;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

import java.text.MessageFormat;

import cn.com.zhihetech.online.core.ZhiheApplication;
import cn.com.zhihetech.online.core.util.SharedPreferenceUtils;
import cn.com.zhihetech.online.ui.activity.MerchantHomeActivity;
import cn.com.zhihetech.online.ui.activity.SeckillGoodsInfoActivity;

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
        setWebChromeClient(new MyWebChromeClient());
        addJavascriptInterface(new JSInterface(getContext(), this), "JSInterface");
    }

    private void initSettings() {
        WebSettings settings = this.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(true);  //设置可以访问文件
        settings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        settings.setAppCacheEnabled(true);
    }

    public void setListener(OnWebViewEventListener listener) {
        this.listener = listener;
    }

    public interface OnWebViewEventListener {
        void onPageStarted(WebView view, String url, Bitmap favicon);

        void onPageFinished(WebView view, String url);
    }

    /**
     * Android与WebView中js交互的接口
     */
    public class JSInterface {

        private Context mContext;
        private WebView target;

        public JSInterface(Context mContext, WebView target) {
            this.mContext = mContext;
            this.target = target;
        }

        /**
         * 跳转到指定的秒杀商品
         *
         * @param goodsId
         * @param goodsName
         */
        @JavascriptInterface
        public void navigationGoods(final String goodsId, final String goodsName) {
            target.post(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(mContext, SeckillGoodsInfoActivity.class);
                    intent.putExtra(SeckillGoodsInfoActivity.SECKILL_GOODS_ID_KEY, goodsId);
                    intent.putExtra(SeckillGoodsInfoActivity.CUSTOM_TITLE_KEY, goodsName);
                    mContext.startActivity(intent);
                }
            });
        }

        /**
         * 跳转到指定的商家
         *
         * @param merchantId
         * @param merchantName
         */
        @JavascriptInterface
        public void navigationMerchant(final String merchantId, final String merchantName) {
            target.post(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(mContext, MerchantHomeActivity.class);
                    intent.putExtra(MerchantHomeActivity.MERCHANT_ID_KEY, merchantId);
                    intent.putExtra(MerchantHomeActivity.CUSTOM_TITLE_KEY, merchantName);
                    mContext.startActivity(intent);
                }
            });
        }

        /**
         * 获取当前登录用户的token
         */
        @JavascriptInterface
        public void getUserToken() {
            SharedPreferenceUtils sharedPreferenceUtils = SharedPreferenceUtils.getInstance(mContext);
            final String token = sharedPreferenceUtils.getUserToken();
            final String onBackUserTokenTpl = "javascript:onBackUserToken(\"{0}\")";
            target.post(new Runnable() {
                @Override
                public void run() {
                    target.loadUrl(MessageFormat.format(onBackUserTokenTpl,token));
                }
            });
        }

        /**
         * 获取当前登录用户的ID
         */
        @JavascriptInterface
        public void getUserId() {
            final String userId = ZhiheApplication.getInstance().getUserId();
            final String onBackUserIdTpl = "javascript:onBackUserId(\"{0}\")";
            target.post(new Runnable() {
                @Override
                public void run() {
                    target.loadUrl(MessageFormat.format(onBackUserIdTpl,userId));
                }
            });
        }
    }

    public class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onCloseWindow(WebView window) {
            super.onCloseWindow(window);
        }

        @Override
        public boolean onCreateWindow(WebView view, boolean dialog,
                                      boolean userGesture, Message resultMsg) {
            return super.onCreateWindow(view, dialog, userGesture, resultMsg);
        }

        /**
         * 覆盖默认的window.alert展示界面，避免title里显示为“：来自file:////”
         */
        public boolean onJsAlert(WebView view, String url, String message,
                                 JsResult result) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

            builder.setTitle("对话框")
                    .setMessage(message)
                    .setPositiveButton("确定", null);

            // 不需要绑定按键事件
            // 屏蔽keycode等于84之类的按键
            builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    return true;
                }
            });
            // 禁止响应按back键的事件
            builder.setCancelable(false);
            AlertDialog dialog = builder.create();
            dialog.show();
            result.confirm();// 因为没有绑定事件，需要强行confirm,否则页面会变黑显示不了内容。
            return true;
            // return super.onJsAlert(view, url, message, result);
        }

        public boolean onJsBeforeUnload(WebView view, String url,
                                        String message, JsResult result) {
            return super.onJsBeforeUnload(view, url, message, result);
        }

        /**
         * 覆盖默认的window.confirm展示界面，避免title里显示为“：来自file:////”
         */
        public boolean onJsConfirm(WebView view, String url, String message,
                                   final JsResult result) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle("对话框")
                    .setMessage(message)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            result.confirm();
                        }
                    })
                    .setNeutralButton("取消", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            result.cancel();
                        }
                    });
            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    result.cancel();
                }
            });

            // 屏蔽keycode等于84之类的按键，避免按键后导致对话框消息而页面无法再弹出对话框的问题
            builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    return true;
                }
            });
            // 禁止响应按back键的事件
            // builder.setCancelable(false);
            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
            // return super.onJsConfirm(view, url, message, result);
        }

        /**
         * 覆盖默认的window.prompt展示界面，避免title里显示为“：来自file:////”
         * window.prompt('请输入您的域名地址', '618119.com');
         */
        public boolean onJsPrompt(WebView view, String url, String message,
                                  String defaultValue, final JsPromptResult result) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

            builder.setTitle("对话框").setMessage(message);

            final EditText et = new EditText(view.getContext());
            et.setSingleLine();
            et.setText(defaultValue);
            builder.setView(et)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            result.confirm(et.getText().toString());
                        }
                    })
                    .setNeutralButton("取消", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            result.cancel();
                        }
                    });

            // 屏蔽keycode等于84之类的按键，避免按键后导致对话框消息而页面无法再弹出对话框的问题
            builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    return true;
                }
            });

            // 禁止响应按back键的事件
            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public void onReceivedIcon(WebView view, Bitmap icon) {
            super.onReceivedIcon(view, icon);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }

        @Override
        public void onRequestFocus(WebView view) {
            super.onRequestFocus(view);
        }
    }
}
