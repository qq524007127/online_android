package cn.com.zhihetech.online.ui.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.Area;
import cn.com.zhihetech.online.bean.User;
import cn.com.zhihetech.online.core.common.ResponseMessage;
import cn.com.zhihetech.online.core.common.ResponseStateCode;
import cn.com.zhihetech.online.core.http.ArrayCallback;
import cn.com.zhihetech.online.core.http.ObjectCallback;
import cn.com.zhihetech.online.core.util.DateUtils;
import cn.com.zhihetech.online.core.util.StringUtils;
import cn.com.zhihetech.online.model.AreaModel;
import cn.com.zhihetech.online.model.UserModel;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@ContentView(R.layout.activity_regist_userinfo)
public class RegisterUserInfoActivity extends BaseActivity {

    @ViewInject(R.id.user_mobile_num_et)
    private EditText userPhoneNum;
    @ViewInject(R.id.accept_checkbox)
    private CheckBox acceptCb;
    @ViewInject(R.id.user_pwd)
    private EditText userPwd;
    @ViewInject(R.id.user_repwd)
    private EditText reUserPwd;
    @ViewInject(R.id.user_nickname)
    private EditText nickName;
    @ViewInject(R.id.invit_code)
    private EditText invitCode;
    @ViewInject(R.id.user_sex_btn)
    private Button sexButton;
    @ViewInject(R.id.birthday_btn)
    private Button birthdayButton;
    @ViewInject(R.id.area_btn)
    private Button areaButton;
    @ViewInject(R.id.income_btn)
    private Button incomeButton;
    @ViewInject(R.id.occupation_btn)
    private Button occupationButton;
    @ViewInject(R.id.user_protocol)
    private TextView userProtocol;
    @ViewInject(R.id.submit_btn)
    private Button submitButton;

    private DatePickerDialog birthDayPickerDialog;
    private AlertDialog sexPicker;
    private AlertDialog areaPicker;
    private AlertDialog occupationPicker;
    private AlertDialog incomePicker;

    private ProgressDialog progressDialog;

    private String[] incomeArray;
    private String[] occupationArray;
    private List<Area> cityAreas;   //市级区域

    private User user = null;

    private ArrayCallback<Area> loadAreaCallback = new ArrayCallback<Area>() {
        @Override
        public void onArray(List<Area> datas) {
            cityAreas = datas;
            initAreaPicker();
        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            super.onError(ex, isOnCallback);
            showMsg(areaButton, "初始化数据失败，请稍后重试！");
            finish();
        }

        @Override
        public void onFinished() {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViewAndData();
    }

    private void initViewAndData() {
        loadArea();
        this.occupationArray = getResources().getStringArray(R.array.occupation_list);
        this.incomeArray = getResources().getStringArray(R.array.income_list);
        initUserPhone();
        initBirthdayPicker();
        initSexPicker();
        initIncomePicker();
        initOccupationPicker();
        this.acceptCb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton paramAnonymousCompoundButton, boolean paramAnonymousBoolean) {
                submitButton.setClickable(paramAnonymousBoolean);
            }
        });
    }

    private void loadArea() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("数据加载中");
        progressDialog.setCancelable(false);
        progressDialog.show();
        new AreaModel().getCityAreas(loadAreaCallback);
    }

    private void initUserPhone() {
        String mobileNum = getIntent().getStringExtra(RegisterVerCodeActivity.USER_MOBILE_NUM);
        this.userPhoneNum.setText(mobileNum);
    }

    /**
     * 创建出生日期选择器
     */
    private void initBirthdayPicker() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        birthDayPickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Date birthDay = DateUtils.parseDate(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth, "yyyy-MM-dd");
                birthdayButton.setText(DateUtils.formatDate(birthDay));
                birthdayButton.setTag(birthDay);
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
                        incomeButton.setText(incomeArray[which]);
                        incomeButton.setTag(incomeArray[which]);
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
                        occupationButton.setText(occupationArray[which]);
                        occupationButton.setTag(occupationArray[which]);
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
                            sexButton.setTag(false);
                            sexButton.setText("女");
                            return;
                        }
                        sexButton.setTag(true);
                        sexButton.setText("男");
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
                        areaButton.setText(cityAreas.get(which).getAreaName());
                        areaButton.setTag(cityAreas.get(which).getAreaId());
                    }
                })
                .create();
    }

    @Event({R.id.user_sex_btn, R.id.birthday_btn, R.id.area_btn, R.id.income_btn, R.id.occupation_btn,
            R.id.user_protocol, R.id.submit_btn})
    private void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.user_sex_btn:
                sexPicker.show();
                break;
            case R.id.birthday_btn:
                birthDayPickerDialog.show();
                break;
            case R.id.area_btn:
                areaPicker.show();
                break;
            case R.id.income_btn:
                incomePicker.show();
                break;
            case R.id.occupation_btn:
                occupationPicker.show();
                break;
            case R.id.user_protocol:
                Intent protocolIntent = new Intent(this, UserProtocolActivity.class);
                startActivity(protocolIntent);
                break;
            case R.id.submit_btn:
                userRegister();
                break;
        }
    }

    /**
     * 开始执行用户注册
     */
    private void userRegister() {
        if (!checkUserInfo()) {
            return;
        }
        final ProgressDialog progressDialog = ProgressDialog.show(this, "", "正在注册，请稍等...");
        new UserModel().register(new ObjectCallback<ResponseMessage>() {
            @Override
            public void onObject(ResponseMessage data) {
                if (data.getCode() != ResponseStateCode.SUCCESS) {
                    showMsg(submitButton, data.getMsg());
                    return;
                }
                new android.support.v7.app.AlertDialog.Builder(getSelf())
                        .setTitle(R.string.tip)
                        .setMessage("注册成功")
                        .setCancelable(false)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface paramAnonymous2DialogInterface, int paramAnonymous2Int) {
                                finish();
                            }
                        }).show();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                showMsg(submitButton, ex.getMessage());
            }

            @Override
            public void onFinished() {
                progressDialog.dismiss();
            }
        }, user);
    }

    private boolean checkUserInfo() {
        this.user = initUser();
        if (!StringUtils.isMobileNum(this.user.getUserPhone())) {
            showMsg(this.userPhoneNum, "手机号码不正确");
            return false;
        }
        if (StringUtils.isEmpty(this.user.getPwd()) || this.user.getPwd().length() < 6) {
            this.userPwd.setError("密码长度不能小于6");
            this.userPwd.requestFocus();
            return false;
        }
        if (!this.user.getPwd().equals(StringUtils.object2String(this.reUserPwd.getText()))) {
            this.reUserPwd.setError("两次输入的密码不同，请重新输入");
            this.reUserPwd.requestFocus();
            return false;
        }
        if (StringUtils.isEmpty(this.user.getUserName())) {
            this.nickName.setError("昵称不能为空");
            this.nickName.requestFocus();
            return false;
        }
        if (this.sexButton.getTag() == null) {
            showMsg(sexButton, "请选择性别");
            return false;
        }
        if (this.birthdayButton.getTag() == null) {
            showMsg(birthdayButton, "请选择出生日期");
            return false;
        }
        if (StringUtils.isEmpty(this.user.getOccupation())) {
            showMsg(occupationButton, "请选择职业");
            return false;
        }
        if (this.user.getArea() == null) {
            showMsg(areaButton, "请选择所属区域");
            return false;
        }
        if (StringUtils.isEmpty(this.user.getIncome())) {
            showMsg(incomeButton, "请选择收入");
            return false;
        }
        return true;
    }

    private User initUser() {
        User user = new User();
        user.setUserPhone(StringUtils.object2String(this.userPhoneNum.getText()));
        user.setPwd(StringUtils.object2String(this.userPwd.getText()));
        user.setUserName(StringUtils.object2String(this.nickName.getText()));
        if (sexButton.getTag() != null) {
            user.setSex((Boolean) this.sexButton.getTag());
        }
        if (this.birthdayButton.getTag() != null) {
            user.setBirthday((Date) birthdayButton.getTag());
        }
        user.setOccupation(StringUtils.object2String(this.occupationButton.getTag()));
        String area = StringUtils.object2String(this.areaButton.getTag());
        if (!StringUtils.isEmpty(area))
            user.setArea(new Area(area));
        if (!StringUtils.isEmpty(StringUtils.object2String(this.invitCode.getText())))
            user.setInvitCode(StringUtils.object2String(this.invitCode.getText()));
        user.setIncome(StringUtils.object2String(this.incomeButton.getTag()));
        return user;
    }
}