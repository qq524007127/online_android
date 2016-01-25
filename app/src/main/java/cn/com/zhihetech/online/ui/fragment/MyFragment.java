package cn.com.zhihetech.online.ui.fragment;

import android.content.Intent;
import android.view.View;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.ui.widget.ReceiptAddressActivity;

/**
 * Created by ShenYunjie on 2016/1/22.
 */
@ContentView(R.layout.content_my_fragment)
public class MyFragment extends BaseFragment {

    @Event({R.id.my_receiver_address_view})
    private void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.my_receiver_address_view:
                Intent intent = new Intent(getContext(), ReceiptAddressActivity.class);
                startActivity(intent);
                break;
        }
    }
}
