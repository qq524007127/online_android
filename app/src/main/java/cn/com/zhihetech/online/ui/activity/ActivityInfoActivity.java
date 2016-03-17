package cn.com.zhihetech.online.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.easemob.easeui.EaseConstant;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.text.MessageFormat;

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

    public final static int USER_ALREADY_IN_ACTIVITY_FANS = 820;    //用户已经加入活动

    public final static String ACTIVITY_ID_KEY = "ACTIVITY_ID";
    public final static String ACTIVITY_NAME_KEY = "ACTIVITY_NAME";

    private final static String ACTIVITY_INFO_URL = Constant.DOMAIN + "web/activity/{0}";


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
    private boolean isJoined = false;

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
            if (activity != null && activity.getCurrentState() == Constant.ACTIVITY_STATE_STARTED) {
                joinActivity(activity);
            } else {
                progressBar.setVisibility(View.GONE);
            }
        }
    };

    /**
     * 加入活动回调
     */
    private ObjectCallback<ResponseMessage> joinActivityCallback = new ObjectCallback<ResponseMessage>() {
        @Override
        public void onObject(ResponseMessage data) {
            int code = data.getCode();
            progressBar.setVisibility(View.GONE);
            if (code == ResponseStateCode.SUCCESS || code == USER_ALREADY_IN_ACTIVITY_FANS) {
                chatRoomBtn.setVisibility(View.VISIBLE);
            } else {
                showMsg(chatRoomBtn, data.getMsg());
            }

        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            super.onError(ex, isOnCallback);
            showMsg(chatRoomBtn, "数据初始化失败，请下拉刷新重试！");
            progressBar.setVisibility(View.GONE);
        }
    };

    /**
     * 加入活动
     *
     * @param act
     */
    private void joinActivity(Activity act) {
        //如果已经加入了活动则不用再次加入活动
        if (isJoined) {
            progressBar.setVisibility(View.GONE);
            chatRoomBtn.setVisibility(View.VISIBLE);
            return;
        }
        new ActivityModel().joinActivity(joinActivityCallback, getUserId(), act.getActivitId());
    }

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
        chatRoomBtn.setVisibility(View.INVISIBLE);
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
        String url = MessageFormat.format(ACTIVITY_INFO_URL, actId);
        webView.loadUrl(url);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                setActivityName(title);
            }
        });
        webView.setWebViewEventListener(new ZhiheWebView.WebViewEventListener() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                containerRefreshLayout.setRefreshing(true);
            }

            @Override
            public void onPageError(WebView view, WebResourceRequest request, WebResourceError error) {
                new AlertDialog.Builder(getSelf())
                        .setTitle(R.string.tip)
                        .setMessage("页面加载失败是否重新加载？")
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                webView.reload();
                                loadActivityInfo(actId);
                            }
                        })
                        .setPositiveButton(R.string.ok, null).show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                containerRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Event({R.id.enter_chat_room_btn})
    private void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.enter_chat_room_btn:
                Intent intent = new Intent(getSelf(), ActivityChatRoomActivity.class);
                intent.putExtra(EaseConstant.EXTRA_USER_ID, this.activity.getChatRoomId());
                intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_CHATROOM);
                intent.putExtra(ActivityChatRoomActivity.CHAT_ROOM_NAME, activity.getActivitName());
                intent.putExtra(ActivityChatRoomActivity.ACTIVITY_ID, activity.getActivitId());
                startActivity(intent);
        }
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
