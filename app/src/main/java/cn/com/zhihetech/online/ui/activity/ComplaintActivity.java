package cn.com.zhihetech.online.ui.activity;


import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.core.common.Constant;
import cn.com.zhihetech.online.ui.fragment.WebViewFragment;

/**
 * Created by ShenYunjie on 2016/3/17.
 */
@ContentView(R.layout.activity_complaint)
public class ComplaintActivity extends BaseActivity {

    private final String FRAGMENT_KEY = "_current_fragment";

    WebViewFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_KEY) == null ? new WebViewFragment()
                : (WebViewFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_KEY);
        Bundle bundle = new Bundle();
        bundle.putString(WebViewFragment.LOAD_URL, Constant.COMPLAINT_AND_RIGHTS_PAGE_URL);
        //bundle.putString(WebViewFragment.LOAD_URL, Constant.COMPLAINT_AND_RIGHTS_PAGE_URL);
        bundle.putBoolean(WebViewFragment.ENABLE_REFRESH, false);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.complaint_content_view, fragment, FRAGMENT_KEY).commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (fragment != null && fragment.onKeyDown(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
