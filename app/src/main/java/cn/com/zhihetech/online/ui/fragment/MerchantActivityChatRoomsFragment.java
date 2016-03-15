package cn.com.zhihetech.online.ui.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;

import com.easemob.easeui.EaseConstant;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.Activity;
import cn.com.zhihetech.online.core.ZhiheApplication;
import cn.com.zhihetech.online.core.adapter.ActivityChatRoomAdapter;
import cn.com.zhihetech.online.core.common.Constant;
import cn.com.zhihetech.online.core.common.PageData;
import cn.com.zhihetech.online.core.common.Pager;
import cn.com.zhihetech.online.core.common.ResponseMessage;
import cn.com.zhihetech.online.core.common.ResponseStateCode;
import cn.com.zhihetech.online.core.http.PageDataCallback;
import cn.com.zhihetech.online.core.http.ResponseMessageCallback;
import cn.com.zhihetech.online.core.view.LoadMoreListView;
import cn.com.zhihetech.online.core.view.OnLoadMoreListener;
import cn.com.zhihetech.online.core.view.ZhiheSwipeRefreshLayout;
import cn.com.zhihetech.online.model.ActivityModel;
import cn.com.zhihetech.online.ui.activity.ActivityChatRoomActivity;

/**
 * Created by ShenYunjie on 2016/3/1.
 */
@ContentView(R.layout.content_merchant_main)
public class MerchantActivityChatRoomsFragment extends BaseFragment {

    @ViewInject(R.id.merchant_activity_refresh_layout)
    private ZhiheSwipeRefreshLayout refreshLayout;
    @ViewInject(R.id.merchant_activity_list_view)
    private LoadMoreListView activityListView;
    private ProgressDialog progressDialog;

    private PageData<Activity> pageData;
    private ActivityChatRoomAdapter adapter;

    /**
     * 刷新（初始化数据）回调
     */
    private PageDataCallback<Activity> refreshCallback = new PageDataCallback<Activity>() {
        @Override
        public void onPageData(PageData<Activity> result, List<Activity> rows) {
            pageData = result;
            adapter.refreshData(rows);
        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            super.onError(ex, isOnCallback);
            showMsg("数据加载失败，请重试");
        }

        @Override
        public void onFinished() {
            super.onFinished();
            refreshLayout.setRefreshing(false);
        }
    };

    /**
     * 数据分页回调
     */
    private PageDataCallback<Activity> loadMoreCallback = new PageDataCallback<Activity>() {
        @Override
        public void onPageData(PageData<Activity> result, List<Activity> rows) {
            pageData = result;
            adapter.addDatas(rows);
        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            super.onError(ex, isOnCallback);
            showMsg("数据加载失败，请重试");
        }

        @Override
        public void onFinished() {
            super.onFinished();
            activityListView.loadComplete();
        }
    };

    /**
     * 获取单个活动信息回调
     */
    private ResponseMessageCallback<Activity> activityInfoCallback = new ResponseMessageCallback<Activity>() {
        @Override
        public void onResponseMessage(ResponseMessage<Activity> responseMessage) {
            if (responseMessage.getCode() != ResponseStateCode.SUCCESS) {
                showMsg("获取数据失败，请重试");
                return;
            }
            Activity activity = responseMessage.getData();
            if (activity.getCurrentState() == Constant.ACTIVITY_STATE_EXAMINED_OK ||
                    activity.getCurrentState() == Constant.ACTIVITY_STATE_STARTED) {
                navigationActivityChatRoom(activity);
                return;
            }
            showMsg("活动已结束");
            refreshData();
        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            showMsg("获取数据失败，请重试");
        }

        @Override
        public void onFinished() {
            progressDialog.dismiss();
        }
    };

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewAndData();
    }

    private void initViewAndData() {
        initViews();
        refreshData();
    }

    private void initViews() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });

        activityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                loadActivityInfo(adapter.getItem(position).getActivitId());
            }
        });

        activityListView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public boolean checkCanDoLoad() {
                return pageData != null && pageData.hasNextPage() && !refreshLayout.isRefreshing();
            }

            @Override
            public void onStartLoad() {
                loadMoreData();
            }
        });
        adapter = new ActivityChatRoomAdapter(getContext());
        activityListView.setAdapter(adapter);
    }

    /**
     * 获取指定ID的活动
     *
     * @param activityId
     */
    private void loadActivityInfo(String activityId) {
        progressDialog = ProgressDialog.show(getContext(), "", getString(R.string.data_loading));
        final Callback.Cancelable cancelable = new ActivityModel().getActivityById(activityInfoCallback, activityId);
        progressDialog.setCancelable(true);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                cancelable.cancel();
            }
        });
    }

    /**
     * 加载更多数据
     */
    private void loadMoreData() {
        new ActivityModel().getStartedActivitiesByMerchantId(loadMoreCallback, pageData.getNextPage(), getLoginUserId());
    }

    /**
     * 刷新或初始化数据
     */
    private void refreshData() {
        if (!refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(true);
        }
        new ActivityModel().getStartedActivitiesByMerchantId(refreshCallback, new Pager(), getLoginUserId());
    }

    /**
     * 跳转到活动聊天室
     *
     * @param activity
     */
    private void navigationActivityChatRoom(Activity activity) {
        Intent intent = new Intent(getContext(), ActivityChatRoomActivity.class);
        intent.putExtra(EaseConstant.EXTRA_USER_ID, activity.getChatRoomId());
        intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_CHATROOM);
        intent.putExtra(ChatFragment.ACTIVITY_ID_KEY, activity.getActivitId());
        intent.putExtra(ActivityChatRoomActivity.CHAT_ROOM_NAME, activity.getActivitName());
        intent.putExtra(ActivityChatRoomActivity.ACTIVITY_ID, activity.getActivitId());
        startActivity(intent);
    }

    @Override
    protected String getLoginUserId() {
        return ZhiheApplication.getInstance().getLogedMerchant().getMerchantId();
    }
}
