package cn.com.zhihetech.online.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ListView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.Merchant;
import cn.com.zhihetech.online.core.adapter.MyFriendAdapter;
import cn.com.zhihetech.online.core.common.PageData;
import cn.com.zhihetech.online.core.common.Pager;
import cn.com.zhihetech.online.core.http.PageDataCallback;
import cn.com.zhihetech.online.core.view.ZhiheSwipeRefreshLayout;
import cn.com.zhihetech.online.model.MerchantModel;

/**
 * Created by ShenYunjie on 2016/1/28.
 */
@ContentView(R.layout.content_my_friends)
public class MyFriendsFrgment extends BaseFragment {

    @ViewInject(R.id.my_friends_refresh_layout)
    private ZhiheSwipeRefreshLayout refreshLayout;
    @ViewInject(R.id.my_friends_lv)
    private ListView friendsLv;

    private MyFriendAdapter adapter;

    private PageDataCallback<Merchant> friendCallback = new PageDataCallback<Merchant>() {
        @Override
        public void onPageData(PageData<Merchant> result, List<Merchant> rows) {
            adapter.refreshData(rows);
        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            super.onError(ex, isOnCallback);
            showMsg(friendsLv, "获取好友列表失败,请刷新重试");
        }

        @Override
        public void onFinished() {
            refreshLayout.setRefreshing(false);
        }
    };

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewAndData();
    }

    private void initViewAndData() {
        initViews();
        loadData();
    }

    private void initViews() {
        adapter = new MyFriendAdapter(getContext());
        friendsLv.setAdapter(adapter);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
    }

    private void loadData() {
        if (!refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(true);
        }
        new MerchantModel().getFocusMerchantsByUserId(friendCallback, new Pager(Integer.MAX_VALUE), getUseId());
    }
}
