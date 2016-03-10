package cn.com.zhihetech.online.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.User;
import cn.com.zhihetech.online.core.ZhiheApplication;

/**
 * Created by ShenYunjie on 2016/3/10.
 */
@ContentView(R.layout.activity_user_info_change)
public class UserInfoChangeActivity extends BaseActivity {

    @ViewInject(R.id.user_mobile_num_et)
    private EditText userNumEt;
    @ViewInject(R.id.user_nickname)
    private EditText userNickEt;
    @ViewInject(R.id.user_sex_btn)
    private Button userSex;
    @ViewInject(R.id.user_birthday_btn)
    private Button userBirthday;
    @ViewInject(R.id.user_occupation_btn)
    private Button userOcc;
    @ViewInject(R.id.user_area_btn)
    private Button userArea;
    @ViewInject(R.id.user_income_btn)
    private Button userIncome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadViewAndData();
    }

    private void loadViewAndData() {
        bindViewData(ZhiheApplication.getInstance().getUser());
    }

    private void bindViewData(User user) {
        this.userNumEt.setText(user.getUserPhone());
        this.userNickEt.setText(user.getUserName());
        this.userSex.setText(user.isSex() ? "男" : "女");
        this.userSex.setTag(user.isSex());
    }

    @Event({R.id.submit_info_changed_btn})
    private void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.submit_info_changed_btn:

                break;
        }
    }
}
