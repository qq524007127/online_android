package cn.com.zhihetech.online.ui.activity;

import android.os.Bundle;
import android.view.KeyEvent;

import org.xutils.view.annotation.ContentView;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.core.util.StringUtils;
import cn.com.zhihetech.online.ui.fragment.WebViewFragment;

/**
 * Created by ShenYunjie on 2016/3/31.
 */
@ContentView(R.layout.activity_webpage)
public class WebPageActivity extends BaseActivity {

    private final String FRAGMENT_KEY = "_current_fragment";
    public final static String PAGE_URL = WebViewFragment.LOAD_URL;
    public final static String ENABLE_REFRESH = WebViewFragment.ENABLE_REFRESH;

    WebViewFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String url = getIntent().getStringExtra(PAGE_URL);
        if (StringUtils.isEmpty(url)) {
            return;
        }
        boolean enableRefresh = getIntent().getBooleanExtra(ENABLE_REFRESH, true);
        fragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_KEY) == null ? new WebViewFragment()
                : (WebViewFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_KEY);
        Bundle args = new Bundle();
        args.putString(WebViewFragment.LOAD_URL, url);
        args.putBoolean(WebViewFragment.ENABLE_REFRESH, enableRefresh);
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.web_page_content, fragment, FRAGMENT_KEY).commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (fragment != null && fragment.onKeyDown(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
