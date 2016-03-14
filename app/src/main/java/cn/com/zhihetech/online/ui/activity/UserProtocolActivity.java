package cn.com.zhihetech.online.ui.activity;

import android.os.Bundle;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.core.common.Constant;
import cn.com.zhihetech.online.core.view.ZhiheWebView;

/**
 * Created by ShenYunjie on 2016/3/14.
 */
@ContentView(R.layout.activity_user_protocol)
public class UserProtocolActivity extends BaseActivity {
    @ViewInject(R.id.protocol_container_webview)
    private ZhiheWebView containerWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        containerWebView.loadUrl(Constant.USER_PROTOCOL_URL);
    }
}
