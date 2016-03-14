package cn.com.zhihetech.online.model;

import android.support.annotation.NonNull;

import org.xutils.common.Callback;

import java.text.MessageFormat;

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
     * 找回登录密码
     *
     * @param callback
     * @param mobileNum 手机号码
     * @param vercode   短信验证码
     * @return
     */
    public Callback.Cancelable findPassword(ObjectCallback<ResponseMessage> callback, @NonNull String mobileNum, @NonNull String vercode) {
        ModelParams params = new ModelParams().addParam("phoneNumber", mobileNum).addParam("securityCode", vercode);
        return new SimpleModel<ResponseMessage>(ResponseMessage.class).postObject(Constant.USER_RESET_PWD_URL, params, callback);
    }

    /**
     * 用户提现
     *
     * @param callback
     * @param userId     用户ID
     * @param vercode    验证码
     * @param alipayCode 提现支付宝账号
     * @param amount     提现金额
     * @return
     */
    public Callback.Cancelable takeWalletMoney(ObjectCallback<ResponseMessage> callback, @NonNull String userId, @NonNull String vercode,
                                               @NonNull String alipayCode, @NonNull float amount) {
        ModelParams params = new ModelParams().addParam("userId", userId).addParam("securityCode", vercode)
                .addParam("aliCode", alipayCode).addParam("money", String.valueOf(amount));
        return new SimpleModel(ResponseMessage.class).postObject(Constant.USER_APPLY_TAKE_WALLET_MONEY_URL, params, callback);
    }

    /**
     * 获取用户钱包金额
     *
     * @param callback
     * @param userId
     * @return
     */
    public Callback.Cancelable getWalletTotalMoney(ResponseMessageCallback<Float> callback, @NonNull String userId) {
        String url = MessageFormat.format(Constant.USER_WALLET_TOTAL_MONEY_URL, userId);
        return new SimpleModel(Float.class).getResponseMessage(url, null, callback);
    }

    /**
     * 用户修改基本信息
     *
     * @param callback
     * @param userId   用户ID
     * @param userNick 用户昵称
     * @param userSex  用户性别
     * @param birthday 用户生日
     * @param occName  职业
     * @param areaId   区域ID
     * @param income   收入
     * @return
     */
    public Callback.Cancelable modifyBaseInfo(ResponseMessageCallback<User> callback, @NonNull String userId, @NonNull String userNick, @NonNull boolean userSex,
                                              @NonNull String birthday, @NonNull String occName, @NonNull String areaId, @NonNull String income) {
        ModelParams params = new ModelParams().addParam("userId", userId).addParam("userName", userNick).addParam("sex", String.valueOf(userSex))
                .addParam("userBirthday", birthday).addParam("occupation", occName).addParam("area.areaId", areaId).addParam("income", income);
        return postResponseMessage(Constant.USER_MODIFY_INFO_URL, params, callback);
    }

    /**
     * 修改头像
     *
     * @param callback
     * @param userId
     * @param imgInfoId
     * @return
     */
    public Callback.Cancelable changeHeader(ObjectCallback<ResponseMessage> callback, @NonNull String userId, @NonNull String imgInfoId) {
        ModelParams params = new ModelParams().addParam("userId", userId).addParam("imgInfoId", imgInfoId);
        return new SimpleModel(ResponseMessage.class).postObject(Constant.USER_CHANGE_HEADER_URL, params, callback);
    }

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
     * 修改登录密码
     *
     * @param callback
     * @param userId
     * @param oldPwd   原登录密码
     * @param newPwd
     * @return
     */
    public Callback.Cancelable changePwd(ObjectCallback<ResponseMessage> callback, @NonNull String userId,
                                         @NonNull String oldPwd, @NonNull String newPwd) {
        ModelParams params = new ModelParams().addParam("userId", userId).addParam("oldPwd", oldPwd).addParam("newPwd", newPwd);
        return new SimpleModel(ResponseMessage.class).postObject(Constant.USER_CHANGE_PWD_URL, params, callback);
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
