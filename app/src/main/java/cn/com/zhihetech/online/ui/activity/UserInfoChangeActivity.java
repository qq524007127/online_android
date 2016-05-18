package cn.com.zhihetech.online.ui.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.easemob.easeui.widget.EaseImageView;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.Area;
import cn.com.zhihetech.online.bean.User;
import cn.com.zhihetech.online.core.ZhiheApplication;
import cn.com.zhihetech.online.core.common.ResponseMessage;
import cn.com.zhihetech.online.core.common.ResponseStateCode;
import cn.com.zhihetech.online.core.http.ArrayCallback;
import cn.com.zhihetech.online.core.http.ResponseMessageCallback;
import cn.com.zhihetech.online.core.util.DateUtils;
import cn.com.zhihetech.online.core.util.ImageLoader;
import cn.com.zhihetech.online.core.util.StringUtils;
import cn.com.zhihetech.online.model.AreaModel;
import cn.com.zhihetech.online.model.UserModel;

/**
 * Created by ShenYunjie on 2016/3/10.
 */
@ContentView(R.layout.activity_user_info_change)
public class UserInfoChangeActivity extends BaseActivity {

    public final static String USER_INFO_MODIFIED_ACTION = "USER_INFO_MODIFIED_ACTION";

    @ViewInject(R.id.user_header_img)
    private EaseImageView userHeaderImg;
    @ViewInject(R.id.user_mobile_num_et)
    private EditText userNumEt;
    @ViewInject(R.id.user_nickname)
    private EditText userNickEt;
    @ViewInject(R.id.user_sex_btn)
    private Button userSexBtn;
    @ViewInject(R.id.user_birthday_btn)
    private Button userBirthdayBtn;
    @ViewInject(R.id.user_occupation_btn)
    private Button userOccBtn;
    @ViewInject(R.id.user_area_btn)
    private Button userAreaBtn;
    @ViewInject(R.id.user_income_btn)
    private Button userIncomeBtn;

    private DatePickerDialog birthDayPickerDialog;
    private AlertDialog sexPicker;
    private AlertDialog areaPicker;
    private AlertDialog occupationPicker;
    private AlertDialog incomePicker;

    private ProgressDialog progressDialog;

    private String[] incomeArray;
    private String[] occupationArray;
    private List<Area> cityAreas;   //市级区域

    /**
     * 获取区域列表回调
     */
    private ArrayCallback<Area> loadAreaCallback = new ArrayCallback<Area>() {
        @Override
        public void onArray(List<Area> datas) {
            cityAreas = datas;
            initAreaPicker();
        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            super.onError(ex, isOnCallback);
            showMsg(userAreaBtn, "初始化数据失败，请稍后重试！");
            finish();
        }

        @Override
        public void onFinished() {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    };

    /**
     * 用户头像更改广播接收器
     */
    private BroadcastReceiver userHeaderChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(UserHeaderModifyActivity.MODIFY_USER_HEADER_SUCCESS_ACTION)) {
                User user = ZhiheApplication.getInstance().getLogedUser();
                ImageLoader.disPlayImage(userHeaderImg, user.getPortrait());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter filter = new IntentFilter(UserHeaderModifyActivity.MODIFY_USER_HEADER_SUCCESS_ACTION);
        registerReceiver(userHeaderChangeReceiver, filter);
        loadViewAndData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(userHeaderChangeReceiver);
    }

    private void loadViewAndData() {
        bindViewData(ZhiheApplication.getInstance().getLogedUser());
        loadAreas();
        this.occupationArray = getResources().getStringArray(R.array.occupation_list);
        this.incomeArray = getResources().getStringArray(R.array.income_list);
        initBirthdayPicker();
        initSexPicker();
        initIncomePicker();
        initOccupationPicker();
    }

    /**
     * 远程加载区域数据
     */
    private void loadAreas() {
        progressDialog = ProgressDialog.show(this, "", getString(R.string.data_loading));
        new AreaModel().getCityAreas(loadAreaCallback);
    }

    /**
     * 初始化当前用户数据到View控件
     *
     * @param user
     */
    private void bindViewData(User user) {
        ImageLoader.disPlayImage(this.userHeaderImg, user.getPortrait());
        this.userNumEt.setText(user.getUserPhone());
        this.userNickEt.setText(user.getUserName());
        this.userSexBtn.setText(user.isSex() ? "男" : "女");
        this.userSexBtn.setTag(user.isSex());
        this.userBirthdayBtn.setText(DateUtils.formatDate(user.getBirthday()));
        this.userBirthdayBtn.setTag(user.getBirthday());
        this.userOccBtn.setText(user.getOccupation());
        this.userIncomeBtn.setText(user.getIncome());
        this.userAreaBtn.setText(user.getArea().getAreaName());
        this.userAreaBtn.setTag(user.getArea().getAreaId());
    }

    @Event({R.id.submit_info_changed_btn, R.id.user_birthday_btn, R.id.user_occupation_btn,
            R.id.user_area_btn, R.id.user_income_btn, R.id.user_sex_btn, R.id.user_header_edit_view})
    private void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.user_header_edit_view:
                Intent intent = new Intent(this, UserHeaderModifyActivity.class);
                startActivity(intent);
                break;
            case R.id.user_birthday_btn:
                birthDayPickerDialog.show();
                break;
            case R.id.user_occupation_btn:
                occupationPicker.show();
                break;
            case R.id.user_area_btn:
                areaPicker.show();
                break;
            case R.id.user_income_btn:
                incomePicker.show();
                break;
            case R.id.user_sex_btn:
                sexPicker.show();
                break;
            case R.id.submit_info_changed_btn:
                upateUserInfo();
                break;
        }
    }

    private void upateUserInfo() {
        final String userNick = userNickEt.getText().toString();
        if (StringUtils.isEmpty(userNick)) {
            userNickEt.setError("昵称不能为空，请重新输入");
            userNickEt.requestFocus();
            return;
        }
        final boolean sex = (boolean) userSexBtn.getTag();
        String birthday = userBirthdayBtn.getText().toString();
        final String occ = userOccBtn.getText().toString();
        String areaId = userAreaBtn.getTag().toString();
        String income = userIncomeBtn.getText().toString();
        final ProgressDialog progress = ProgressDialog.show(this, "", getString(R.string.data_executing));
        progress.setCancelable(true);
        final User user = ZhiheApplication.getInstance().getLogedUser();
        final Callback.Cancelable cancelable = new UserModel().modifyBaseInfo(new ResponseMessageCallback<User>() {
            @Override
            public void onResponseMessage(ResponseMessage<User> responseMessage) {
                if (responseMessage.getCode() != ResponseStateCode.SUCCESS) {
                    showMsg(responseMessage.getMsg());
                    return;
                }
                User _user = responseMessage.getData();
                _user.getArea().setAreaName(userAreaBtn.getText().toString());
                ZhiheApplication.getInstance().replaceUser(_user);
                Intent intent = new Intent(USER_INFO_MODIFIED_ACTION);
                sendBroadcast(intent);
                showMsg("信息修改成功！");
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                showMsg("信息修改失败，请重试！");
            }

            @Override
            public void onFinished() {
                progress.dismiss();
            }
        }, user.getUserId(), userNick, sex, birthday, occ, areaId, income);
        progress.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (!cancelable.isCancelled()) {
                    cancelable.cancel();
                }
            }
        });
    }

    /**
     * 创建出生日期选择器
     */
    private void initBirthdayPicker() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime((Date) userBirthdayBtn.getTag());
        birthDayPickerDialog = new DatePickerDialog(this, R.style.AppDatePickerDialogStyle, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Date birthDay = DateUtils.parseDate(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth, "yyyy-MM-dd");
                userBirthdayBtn.setText(DateUtils.formatDate(birthDay));
                userBirthdayBtn.setTag(birthDay);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        birthDayPickerDialog.setTitle("请选择出生日期");
    }

    /**
     * 创建收入选择器
     */
    private void initIncomePicker() {
        incomePicker = new AlertDialog.Builder(this)
                .setTitle("选择职业")
                .setItems(incomeArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        userIncomeBtn.setText(incomeArray[which]);
                        userIncomeBtn.setTag(incomeArray[which]);
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create();
    }

    /**
     * 创建职业选择器
     */
    private void initOccupationPicker() {
        occupationPicker = new AlertDialog.Builder(this)
                .setTitle("选择职业")
                .setItems(occupationArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        userOccBtn.setText(occupationArray[which]);
                        userOccBtn.setTag(occupationArray[which]);
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create();
    }

    /**
     * 创建性别选择器
     */
    private void initSexPicker() {
        sexPicker = new AlertDialog.Builder(this)
                .setTitle("请选择性别")
                .setItems(new CharSequence[]{"女", "男"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            userSexBtn.setTag(false);
                            userSexBtn.setText("女");
                            return;
                        }
                        userSexBtn.setTag(true);
                        userSexBtn.setText("男");
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create();
    }

    /**
     * 初始化区域选择器（必须在获取区域数据成功回调后调用此方法）
     */
    private void initAreaPicker() {
        if (cityAreas == null) {
            return;
        }
        String[] cityNames = new String[cityAreas.size()];
        for (int i = 0; i < cityAreas.size(); i++) {
            cityNames[i] = cityAreas.get(i).getAreaName();
        }
        areaPicker = new AlertDialog.Builder(this)
                .setTitle("选择所属区域")
                .setItems(cityNames, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        userAreaBtn.setText(cityAreas.get(which).getAreaName());
                        userAreaBtn.setTag(cityAreas.get(which).getAreaId());
                    }
                })
                .create();
    }
}
