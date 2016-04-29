package cn.com.zhihetech.online.ui.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;

import org.xutils.view.annotation.ContentView;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.core.common.Constant;
import cn.com.zhihetech.online.core.util.StringUtils;
import cn.com.zhihetech.online.ui.fragment.WebViewFragment;

/**
 * Created by ShenYunjie on 2016/3/17.
 */
@ContentView(R.layout.activity_coore_center)
public class NewsReleaseMeetActivity extends BaseActivity implements WebViewFragment.WebViewEventHandle {
    private final String FRAGMENT_KEY = "_current_fragment";

    WebViewFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_KEY) == null ? new WebViewFragment()
                : (WebViewFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_KEY);
        Bundle bundle = new Bundle();
        bundle.putString(WebViewFragment.LOAD_URL, Constant.NEWS_RELEASE_MEET_PAGE_URL);
        bundle.putBoolean(WebViewFragment.ENABLE_REFRESH, true);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_fragment_layout, fragment, FRAGMENT_KEY).commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (fragment != null && fragment.onKeyDown(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {

    }

    @Override
    public void onPageError(WebView view) {

    }

    @Override
    public void onPageFinished(WebView view, String url) {

    }

    @Override
    public void onReceivedTitle(WebView webView, String title) {
        if (!StringUtils.isEmpty(title)) {
            this.toolbar.setTitle(title);
        }
    }
}
