package cn.com.zhihetech.online.ui.activity;


import android.os.Bundle;

import org.xutils.view.annotation.ContentView;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.core.common.Constant;
import cn.com.zhihetech.online.ui.fragment.WebViewFragment;

/**
 * Created by ShenYunjie on 2016/3/17.
 */
@ContentView(R.layout.activity_complaint)
public class ComplaintActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WebViewFragment fragment = new WebViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(WebViewFragment.LOAD_URL, Constant.COMPLAINT_AND_ROGHTS_PAGE_URL);
        bundle.putBoolean(WebViewFragment.ENABLE_REFRESH, false);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.complaint_content_view, fragment).commit();
    }
}
