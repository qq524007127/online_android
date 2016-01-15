package cn.com.zhihetech.online.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.core.view.LoadMoreListView;
import cn.com.zhihetech.online.core.view.ZhiheSwipeRefreshLayout;

/**
 * Created by ShenYunjie on 2016/1/15.
 */
@ContentView(R.layout.content_home_fragment)
public class HomeFragment extends BaseFragment {
    @ViewInject(R.id.home_search_btn)
    private Button searchBtn;
    @ViewInject(R.id.home_content_srl)
    private ZhiheSwipeRefreshLayout swipeRefreshLayout;
    @ViewInject(R.id.home_activity_lv)
    private LoadMoreListView activityLV;

    private BaseAdapter adapter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
