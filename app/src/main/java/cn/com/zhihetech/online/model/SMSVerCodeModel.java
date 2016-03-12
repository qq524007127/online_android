package cn.com.zhihetech.online.model;

import android.support.annotation.NonNull;

import org.xutils.common.Callback;

import cn.com.zhihetech.online.core.common.Constant;
import cn.com.zhihetech.online.core.common.ResponseMessage;
import cn.com.zhihetech.online.core.http.ObjectCallback;
import cn.com.zhihetech.online.core.http.ResponseMessageCallback;

/**
 * Created by ShenYunjie on 2016/1/29.
 */
public class SMSVerCodeModel {

    /**
     * 获取申请提现验证码
     *
     * @param callback
     * @param mobileNum
     * @return
     */
    public Callback.Cancelable getTakeWalletMoneyVerifyCode(ResponseMessageCallback<Integer> callback, @NonNull String mobileNum) {
        return getVerifyCode(callback, mobileNum, VerifyCodeType.TAKE_WALLET_MONEY_TYPE);
    }

    /**
     * 获取找回密码验证码
     *
     * @param callback
     * @param mobileNum
     * @return
     */
    public Callback.Cancelable getFindPwdVerifyCode(ResponseMessageCallback<Integer> callback, @NonNull String mobileNum) {
        return getVerifyCode(callback, mobileNum, VerifyCodeType.FORGET_PWD_TYPE);
    }

    /**
     * 获取用户注册验证码
     *
     * @param callback
     * @param mobileNum
     * @return
     */
    public Callback.Cancelable getRegistVerifyCode(ResponseMessageCallback<Integer> callback, @NonNull String mobileNum) {
        return getVerifyCode(callback, mobileNum, VerifyCodeType.REGIST_TYPE);
    }

    /**
     * 获取注册验证码
     *
     * @param callback       回调
     * @param mobileNum      手机号码
     * @param verifyCodeType 验证码类型
     * @return
     */
    public Callback.Cancelable getVerifyCode(ResponseMessageCallback<Integer> callback, @NonNull String mobileNum, @NonNull VerifyCodeType verifyCodeType) {
        ModelParams params = new ModelParams().addParam("phoneNumber", mobileNum);
        switch (verifyCodeType) {
            case REGIST_TYPE:
                params.addParam("securityState", "1");
                break;
            case FORGET_PWD_TYPE:
                params.addParam("securityState", "2");
                break;
            case TAKE_WALLET_MONEY_TYPE:
                params.addParam("securityState", "3");
                break;
        }
        return new SimpleModel(Integer.class).postResponseMessage(Constant.VER_CODE_URL, params, callback);
    }

    /**
     * 教验验证码
     *
     * @param callback
     * @param mobileNum
     * @param verCode
     * @return
     */
    public Callback.Cancelable verifyMobileNum(ObjectCallback<ResponseMessage> callback, @NonNull String mobileNum, @NonNull String verCode) {
        ModelParams params = new ModelParams().addParam("phoneNumber", mobileNum).addParam("securityCode", verCode);
        return new SimpleModel(ResponseMessage.class).postObject(Constant.VER_CODE_VERIY_URL, params, callback);
    }

    /**
     * 短信验证码类型
     */
    public enum VerifyCodeType {
        REGIST_TYPE, FORGET_PWD_TYPE, TAKE_WALLET_MONEY_TYPE
    }
}
