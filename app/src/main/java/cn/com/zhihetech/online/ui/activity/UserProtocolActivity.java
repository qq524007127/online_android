package cn.com.zhihetech.online.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.core.common.Constant;
import cn.com.zhihetech.online.core.view.ZhiheWebView;
import cn.com.zhihetech.online.ui.fragment.WebViewFragment;

/**
 * Created by ShenYunjie on 2016/3/14.
 */
@ContentView(R.layout.activity_user_protocol)
public class UserProtocolActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fragment fragment = new WebViewFragment();
        Bundle args = new Bundle();
        args.putBoolean(WebViewFragment.ENABLE_REFRESH, false);
        args.putString(WebViewFragment.LOAD_URL, Constant.USER_PROTOCOL_PAGE_URL);
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.protocol_container, fragment).commit();
    }
}
