package cn.com.zhihetech.online.ui.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.easemob.chat.EMChatManager;

import org.w3c.dom.Text;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.core.common.ActivityStack;
import cn.com.zhihetech.online.core.common.Constant;
import cn.com.zhihetech.online.core.util.SharedPreferenceUtils;
import cn.com.zhihetech.online.ui.activity.LoginActivity;
import cn.com.zhihetech.online.ui.activity.OrderActivity;
import cn.com.zhihetech.online.ui.activity.ReceiptAddressActivity;

/**
 * Created by ShenYunjie on 2016/1/22.
 */
@ContentView(R.layout.content_my_fragment)
public class MyFragment extends BaseFragment {

    @ViewInject(R.id.my_nick_name_tv)
    private TextView nickNameTv;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        nickNameTv.setText(getUser().getUserName());
    }

    @Event({R.id.my_waiting_pay_view, R.id.my_no_dispatch, R.id.my_already_dispatch_view,
            R.id.my_waiting_evalute_view,
            R.id.my_refunding_view, R.id.my_volum_view, R.id.my_red_money_view,
            R.id.my_friends_view, R.id.my_baby_view, R.id.my_info_change_view, R.id.my_pwd_change_view,
            R.id.my_receiver_address_view, R.id.exit_app_btn, R.id.my_all_order_view})
    private void onViewClick(View view) {
        Intent orderIntent = new Intent(getContext(), OrderActivity.class);
        switch (view.getId()) {
            case R.id.my_all_order_view:
                startActivity(orderIntent);
                break;
            case R.id.my_waiting_pay_view:
                orderIntent.putExtra(OrderActivity.ORDER_STATE_KEY, Constant.ORDER_STATE_NO_PAYMENT);
                startActivity(orderIntent);
                break;
            case R.id.my_no_dispatch:
                orderIntent.putExtra(OrderActivity.ORDER_STATE_KEY, Constant.ORDER_STATE_NO_DISPATCHER);
                startActivity(orderIntent);
                break;
            case R.id.my_already_dispatch_view:
                orderIntent.putExtra(OrderActivity.ORDER_STATE_KEY, Constant.ORDER_STATE_ALREADY_DISPATCHER);
                startActivity(orderIntent);
                break;
            case R.id.my_waiting_evalute_view:
                orderIntent.putExtra(OrderActivity.ORDER_STATE_KEY, Constant.ORDER_STATE_ALREADY_DELIVER);
                startActivity(orderIntent);
                break;
            case R.id.my_refunding_view:
                orderIntent.putExtra(OrderActivity.ORDER_STATE_KEY, Constant.ORDER_STATE_WAIT_REFUND);
                startActivity(orderIntent);
                break;
            case R.id.my_volum_view:

                break;
            case R.id.my_red_money_view:

                break;
            case R.id.my_friends_view:

                break;
            case R.id.my_baby_view:

                break;
            case R.id.my_info_change_view:

                break;
            case R.id.my_pwd_change_view:

                break;
            case R.id.my_receiver_address_view:
                Intent intent = new Intent(getContext(), ReceiptAddressActivity.class);
                startActivity(intent);
                break;
            case R.id.exit_app_btn:
                new AlertDialog.Builder(getContext())
                        .setTitle(R.string.tip)
                        .setMessage("确定要退出当前账号吗？")
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                logoutApp();
                            }
                        })
                        .setNegativeButton(R.string.cancel, null)
                        .show();
                break;
        }
    }

    /**
     * 退出当前登录账号
     */
    private void logoutApp() {
        SharedPreferenceUtils.getInstance(getContext()).clear();
        ActivityStack.getInstance().removeWithout(getActivity());
        EMChatManager.getInstance().logout(null);
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}
