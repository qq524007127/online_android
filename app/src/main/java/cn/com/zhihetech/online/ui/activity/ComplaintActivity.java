package cn.com.zhihetech.online.ui.activity;


import android.os.Bundle;

import org.xutils.view.annotation.ContentView;

import cn.com.zhihetech.online.R;
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
        bundle.putString(WebViewFragment.LOAD_URL, "http://www.baidu.co");
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.complaint_content_view, fragment).commit();
    }
}
