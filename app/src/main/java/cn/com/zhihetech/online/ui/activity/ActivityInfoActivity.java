package cn.com.zhihetech.online.ui.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.Activity;
import cn.com.zhihetech.online.core.common.Constant;
import cn.com.zhihetech.online.core.common.ResponseMessage;
import cn.com.zhihetech.online.core.common.ResponseStateCode;
import cn.com.zhihetech.online.core.http.ObjectCallback;
import cn.com.zhihetech.online.core.http.ResponseMessageCallback;
import cn.com.zhihetech.online.core.util.StringUtils;
import cn.com.zhihetech.online.core.view.ZhiheSwipeRefreshLayout;
import cn.com.zhihetech.online.core.view.ZhiheWebView;
import cn.com.zhihetech.online.model.ActivityModel;

/**
 * Created by ShenYunjie on 2016/2/22.
 */
@ContentView(R.layout.activity_activity_info)
public class ActivityInfoActivity extends BaseActivity {

    public final static String ACTIVITY_ID_KEY = "ACTIVITY_ID";
    public final static String ACTIVITY_NAME_KEY = "ACTIVITY_NAME";


    @ViewInject(R.id.activity_name_tv)
    private TextView activitNameTv;
    @ViewInject(R.id.activity_progress_pb)
    private ProgressBar progressBar;
    @ViewInject(R.id.enter_chat_room_btn)
    private Button chatRoomBtn;
    @ViewInject(R.id.activity_container_zsrl)
    private ZhiheSwipeRefreshLayout containerRefreshLayout;
    @ViewInject(R.id.activity_container_wv)
    private ZhiheWebView webView;

    private String actId;
    private Activity activity;

    /**
     * 加载活动详情回调
     */
    private ResponseMessageCallback<Activity> activityCallback = new ResponseMessageCallback<Activity>() {

        @Override
        public void onResponseMessage(ResponseMessage<Activity> responseMessage) {
            if (responseMessage.getCode() == ResponseStateCode.SUCCESS) {
                activity = responseMessage.getData();
            }
        }

        @Override
        public void onFinished() {
            super.onFinished();
            progressBar.setVisibility(View.GONE);
            if (activity != null && activity.getCurrentState() == Constant.ACTIVITY_STATE_STARTED) {
                chatRoomBtn.setVisibility(View.VISIBLE);
            } else {
                chatRoomBtn.setVisibility(View.INVISIBLE);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actId = getIntent().getStringExtra(ACTIVITY_ID_KEY);
        if (StringUtils.isEmpty(actId)) {
            showMsg("出错了！");
            finish();
            return;
        }
        initViews();
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        loadActivityInfo(actId);
    }

    private void loadActivityInfo(@NonNull String actId) {
        progressBar.setVisibility(View.VISIBLE);
        chatRoomBtn.setVisibility(View.GONE);
        new ActivityModel().getActivityById(activityCallback, actId);
    }

    /**
     * 初始化控件
     */
    private void initViews() {
        containerRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                webView.reload();
                loadActivityInfo(actId);
            }
        });
        initWebView();
    }

    private void initWebView() {
        webView.loadUrl(Constant.DOMAIN + "web/activity/" + actId);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                setActivityName(title);
            }
        });
        webView.setListener(new ZhiheWebView.OnWebViewEventListener() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                containerRefreshLayout.setRefreshing(true);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                containerRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Event({R.id.enter_chat_room_btn})
    private void onViewClick(View view) {

    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
            return;
        }
        super.onBackPressed();
    }

    /**
     * 设置活动的名称
     *
     * @param name
     */
    protected void setActivityName(String name) {
        this.activitNameTv.setText(name);
    }
}
