package cn.com.zhihetech.online.ui.fragment;

import android.content.Intent;
import android.view.View;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.ui.widget.OrderActivity;
import cn.com.zhihetech.online.ui.widget.ReceiptAddressActivity;

/**
 * Created by ShenYunjie on 2016/1/22.
 */
@ContentView(R.layout.content_my_fragment)
public class MyFragment extends BaseFragment {

    @Event({R.id.my_waiting_pay_view, R.id.my_paied_view, R.id.my_waiting_evalute_view,
            R.id.my_refunding_view, R.id.refunded_view, R.id.my_volum_view, R.id.my_red_money_view,
            R.id.my_friends_view, R.id.my_baby_view, R.id.my_info_change_view, R.id.my_pwd_change_view,
            R.id.my_receiver_address_view, R.id.exit_app_btn, R.id.my_all_order_view})
    private void onViewClick(View view) {
        Intent orderIntent = new Intent(getContext(), OrderActivity.class);
        switch (view.getId()) {
            case R.id.my_all_order_view:
                startActivity(orderIntent);
                break;
            case R.id.my_waiting_pay_view:

                startActivity(orderIntent);
                break;
            case R.id.my_paied_view:

                startActivity(orderIntent);
                break;
            case R.id.my_waiting_evalute_view:

                startActivity(orderIntent);
                break;
            case R.id.my_refunding_view:

                startActivity(orderIntent);
                break;
            case R.id.refunded_view:

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

                break;
        }
    }
}
