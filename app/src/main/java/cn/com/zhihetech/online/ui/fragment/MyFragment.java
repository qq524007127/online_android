package cn.com.zhihetech.online.ui.fragment;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.easemob.chat.EMChatManager;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.User;
import cn.com.zhihetech.online.core.ZhiheApplication;
import cn.com.zhihetech.online.core.common.ActivityStack;
import cn.com.zhihetech.online.core.common.Constant;
import cn.com.zhihetech.online.core.util.ImageLoader;
import cn.com.zhihetech.online.core.util.SharedPreferenceUtils;
import cn.com.zhihetech.online.ui.activity.AboutUsActivity;
import cn.com.zhihetech.online.ui.activity.BaseActivity;
import cn.com.zhihetech.online.ui.activity.ChangePasswordActivity;
import cn.com.zhihetech.online.ui.activity.LoginActivity;
import cn.com.zhihetech.online.ui.activity.MyFavoritesActivity;
import cn.com.zhihetech.online.ui.activity.MyRedEnvelopItemListActivity;
import cn.com.zhihetech.online.ui.activity.MyCouponItemActivity;
import cn.com.zhihetech.online.ui.activity.MyWalletActivity;
import cn.com.zhihetech.online.ui.activity.OrderActivity;
import cn.com.zhihetech.online.ui.activity.ReceiptAddressActivity;
import cn.com.zhihetech.online.ui.activity.UserHeaderModifyActivity;
import cn.com.zhihetech.online.ui.activity.UserInfoChangeActivity;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ShenYunjie on 2016/1/22.
 */
@ContentView(R.layout.content_my_fragment)
public class MyFragment extends BaseFragment {

    private final int REFUND_AND_SERVICE = 101; //退款和售后

    @ViewInject(R.id.my_header_img)
    private CircleImageView headerImg;
    @ViewInject(R.id.my_nick_name_tv)
    private TextView nickNameTv;

    private BroadcastReceiver userHeaderChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            User user = ZhiheApplication.getInstance().getUser();
            switch (intent.getAction()) {
                case UserHeaderModifyActivity.MODIFY_USER_HEADER_SUCCESS_ACTION:
                    ImageLoader.disPlayImage(headerImg, user.getPortrait());
                    break;
                case UserInfoChangeActivity.USER_INFO_MODIFIED_ACTION:
                    nickNameTv.setText(user.getUserName());
                    break;
            }
        }
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        nickNameTv.setText(getUser().getUserName());
        User user = ZhiheApplication.getInstance().getUser();
        ImageLoader.disPlayImage(headerImg, user.getPortrait());
        IntentFilter filter = new IntentFilter(UserHeaderModifyActivity.MODIFY_USER_HEADER_SUCCESS_ACTION);
        filter.addAction(UserInfoChangeActivity.USER_INFO_MODIFIED_ACTION);
        getActivity().registerReceiver(userHeaderChangeReceiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(userHeaderChangeReceiver);
    }

    @Event({R.id.my_waiting_pay_view, R.id.my_no_dispatch, R.id.my_already_dispatch_view,
            R.id.my_waiting_evalute_view, R.id.about_us_view, R.id.my_header_img,
            R.id.my_refund_and_service_view, R.id.my_coupon_view, R.id.my_red_envelop_view,
            R.id.my_friends_view, R.id.my_favorites_view, R.id.my_info_change_view, R.id.my_pwd_change_view,
            R.id.my_receiver_address_view, R.id.exit_app_btn, R.id.my_all_order_view, R.id.my_wallet})
    private void onViewClick(View view) {
        Intent orderIntent = new Intent(getContext(), OrderActivity.class);
        switch (view.getId()) {
            case R.id.my_all_order_view:
                startActivity(orderIntent);
                break;
            case R.id.my_waiting_pay_view:
                orderIntent.putExtra(OrderActivity.ORDER_STATE_KEY, Constant.ORDER_STATE_NO_PAYMENT);
                orderIntent.putExtra(BaseActivity.CUSTOM_TITLE_KEY, "待付款");
                startActivity(orderIntent);
                break;
            case R.id.my_no_dispatch:
                orderIntent.putExtra(OrderActivity.ORDER_STATE_KEY, Constant.ORDER_STATE_NO_DISPATCHER);
                orderIntent.putExtra(BaseActivity.CUSTOM_TITLE_KEY, "代发货");
                startActivity(orderIntent);
                break;
            case R.id.my_already_dispatch_view:
                orderIntent.putExtra(OrderActivity.ORDER_STATE_KEY, Constant.ORDER_STATE_ALREADY_DISPATCHER);
                orderIntent.putExtra(BaseActivity.CUSTOM_TITLE_KEY, "待收货");
                startActivity(orderIntent);
                break;
            case R.id.my_waiting_evalute_view:
                orderIntent.putExtra(OrderActivity.ORDER_STATE_KEY, Constant.ORDER_STATE_ALREADY_DELIVER);
                orderIntent.putExtra(BaseActivity.CUSTOM_TITLE_KEY, "待评价");
                startActivity(orderIntent);
                break;
            case R.id.my_refund_and_service_view:
                orderIntent.putExtra(OrderActivity.ORDER_STATE_KEY, REFUND_AND_SERVICE);
                orderIntent.putExtra(BaseActivity.CUSTOM_TITLE_KEY, "退款/售后");
                startActivity(orderIntent);
                break;
            case R.id.my_coupon_view:
                Intent couponIntent = new Intent(getContext(), MyCouponItemActivity.class);
                startActivity(couponIntent);
                break;
            case R.id.my_red_envelop_view:
                Intent redEnvelopItemIntent = new Intent(getContext(), MyRedEnvelopItemListActivity.class);
                startActivity(redEnvelopItemIntent);
                break;
            case R.id.my_friends_view:

                break;
            case R.id.my_favorites_view:
                Intent myFavsIntent = new Intent(getContext(), MyFavoritesActivity.class);
                startActivity(myFavsIntent);
                break;
            case R.id.my_info_change_view:
                Intent infoChangeIntent = new Intent(getContext(), UserInfoChangeActivity.class);
                startActivity(infoChangeIntent);
                break;
            case R.id.my_pwd_change_view:
                Intent changePwdIntent = new Intent(getContext(), ChangePasswordActivity.class);
                startActivity(changePwdIntent);
                break;
            case R.id.my_receiver_address_view:
                Intent intent = new Intent(getContext(), ReceiptAddressActivity.class);
                startActivity(intent);
                break;
            case R.id.my_wallet:
                Intent walletIntent = new Intent(getContext(), MyWalletActivity.class);
                startActivity(walletIntent);
                break;
            case R.id.about_us_view:
                Intent aboutUsIntent = new Intent(getContext(), AboutUsActivity.class);
                startActivity(aboutUsIntent);
                break;
            case R.id.exit_app_btn:
                new AlertDialog.Builder(getContext())
                        .setTitle(R.string.tip)
                        .setMessage("确定要退出当前账号吗？")
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                logoutAccount();
                            }
                        })
                        .setNegativeButton(R.string.cancel, null)
                        .show();
                break;
            case R.id.my_header_img:
                Intent headerModifyIntent = new Intent(getContext(), UserHeaderModifyActivity.class);
                startActivity(headerModifyIntent);
                break;
        }
    }

    /**
     * 退出当前登录账号
     */
    private void logoutAccount() {
        SharedPreferenceUtils.getInstance(getContext()).clear();
        ActivityStack.getInstance().removeWithout(getActivity());
        EMChatManager.getInstance().logout(null);
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}
