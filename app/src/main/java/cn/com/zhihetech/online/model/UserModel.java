package cn.com.zhihetech.online.model;

import android.support.annotation.NonNull;

import org.xutils.common.Callback;

import java.util.Map;

import cn.com.zhihetech.online.bean.Token;
import cn.com.zhihetech.online.bean.User;
import cn.com.zhihetech.online.core.common.Constant;
import cn.com.zhihetech.online.core.common.ResponseMessage;
import cn.com.zhihetech.online.core.http.ObjectCallback;
import cn.com.zhihetech.online.core.http.ResponseMessageCallback;
import cn.com.zhihetech.online.core.util.DateUtils;
import cn.com.zhihetech.online.core.util.StringUtils;

/**
 * Created by ShenYunjie on 2016/1/29.
 */
public class UserModel extends BaseModel<User> {
    /**
     * 用户登录
     *
     * @param callback 回调
     * @param userNum  账号
     * @param password 密码
     * @return
     */
    public Callback.Cancelable login(ResponseMessageCallback<Token> callback, @NonNull String userNum, @NonNull String password) {
        ModelParams params = new ModelParams().addParam("userPhone", userNum).addParam("pwd", password);
        return new SimpleModel(Token.class).postResponseMessage(Constant.USER_LOGIN_URL, params, callback);
    }

    /**
     * 用户注册
     *
     * @param callback
     * @param user
     * @return
     */
    public Callback.Cancelable register(ObjectCallback<ResponseMessage> callback, @NonNull User user) {
        return new SimpleModel(ResponseMessage.class).postObject(Constant.USER_REGISTER_URL, createParams(user), callback);
    }

    /**
     * 根据User创建查询参数
     *
     * @param user
     * @return
     */
    private ModelParams createParams(User user) {
        ModelParams params = new ModelParams();
        params.addParam("userPhone", user.getUserPhone());
        params.addParam("pwd", user.getPwd());
        params.addParam("income", user.getIncome());
        params.addParam("userName", user.getUserName());
        params.addParam("occupation", user.getOccupation());
        params.addParam("sex", StringUtils.object2String(Boolean.valueOf(user.isSex())));
        params.addParam("userBirthday", DateUtils.formatDate(user.getBirthday()));
        if (user.getArea() != null)
            params.addParam("area.areaId", user.getArea().getAreaId());
        if (StringUtils.isEmpty(user.getInvitCode())) {
            params.addParam("invitCode", user.getInvitCode());
        }
        return params;
    }
}
