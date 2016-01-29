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

    private final String registerVerCodeType = "1"; //注册验证码类型

    /**
     * 获取注册验证码
     *
     * @param callback
     * @param mobileNum
     * @return
     */
    public Callback.Cancelable getRegisterVerCode(ResponseMessageCallback<Integer> callback, @NonNull String mobileNum) {
        ModelParams params = new ModelParams().addParam("phoneNumber", mobileNum).addParam("securityState", registerVerCodeType);
        return new SimpleModel<Integer>(Integer.class).postResponseMessage(Constant.VER_CODE_URL, params, callback);
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
}
