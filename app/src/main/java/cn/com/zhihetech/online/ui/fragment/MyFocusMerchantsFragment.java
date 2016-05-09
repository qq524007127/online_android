package cn.com.zhihetech.online.ui.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.LocalSocketAddress;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.easemob.easeui.EaseConstant;

import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.EMUserInfo;
import cn.com.zhihetech.online.bean.Merchant;
import cn.com.zhihetech.online.core.adapter.MyFriendAdapter;
import cn.com.zhihetech.online.core.common.Constant;
import cn.com.zhihetech.online.core.common.PageData;
import cn.com.zhihetech.online.core.common.Pager;
import cn.com.zhihetech.online.core.common.ResponseMessage;
import cn.com.zhihetech.online.core.common.ResponseStateCode;
import cn.com.zhihetech.online.core.db.DBUtils;
import cn.com.zhihetech.online.core.http.ObjectCallback;
import cn.com.zhihetech.online.core.http.PageDataCallback;
import cn.com.zhihetech.online.core.view.ZhiheSwipeRefreshLayout;
import cn.com.zhihetech.online.model.MerchantModel;
import cn.com.zhihetech.online.ui.activity.SingleChatActivity;

/**
 * Created by ShenYunjie on 2016/1/28.
 */
@ContentView(R.layout.content_my_friends)
public class MyFocusMerchantsFragment extends BaseFragment {

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
        friendsLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Merchant data = adapter.getItem(position);
                Intent intent = new Intent(getContext(), SingleChatActivity.class);
                intent.putExtra(EaseConstant.EXTRA_USER_ID, data.getEMUserId());
                try {
                    saveMerchantInfo(data);
                } catch (DbException e) {
                    e.printStackTrace();
                    showMsg("出错了！");
                    return;
                }
                startActivity(intent);
            }
        });
        friendsLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(getContext())
                        .setTitle(R.string.tip)
                        .setMessage("确定要取消与此商家的好友关系吗？")
                        .setPositiveButton(R.string.cancel, null)
                        .setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                cancelFocusMerchant(adapter.getItem(position));
                            }
                        }).show();
                return true;
            }
        });
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
        new MerchantModel().getFocusMerchantsByUserId(friendCallback, new Pager(Integer.MAX_VALUE), getLoginUserId());
    }

    /**
     * 取消商家关注
     *
     * @param merchant
     */
    private void cancelFocusMerchant(Merchant merchant) {
        new MerchantModel().cancelFocusMerchant(new ObjectCallback<ResponseMessage>() {
            @Override
            public void onObject(ResponseMessage data) {
                if (data.getCode() == ResponseStateCode.SUCCESS) {
                    loadData();
                    showMsg("取消关注成功！");
                } else {
                    showMsg(data.getMsg());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                showMsg("取消商家关注失败，请重试！");
            }
        }, getLoginUserId(), merchant.getMerchantId());
    }

    /**
     * 将商家的环信用户基本信息保存到本地数据库
     *
     * @param merchant
     * @throws DbException
     */
    private void saveMerchantInfo(Merchant merchant) throws DbException {
        EMUserInfo userInfo = new EMUserInfo(merchant.getEMUserId(), merchant.getMerchName(),
                merchant.getCoverImg().getUrl(), merchant.getMerchantId(), Constant.EXTEND_MERCHANT_USER);
        new DBUtils().saveUserInfo(userInfo);
    }
}
