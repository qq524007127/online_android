package cn.com.zhihetech.online.model;

import android.support.annotation.Nullable;

import com.alibaba.fastjson.JSONObject;

import org.xutils.common.Callback;

import cn.com.zhihetech.online.bean.OrderEvaluate;
import cn.com.zhihetech.online.core.common.Constant;
import cn.com.zhihetech.online.core.common.ResponseMessage;
import cn.com.zhihetech.online.core.http.ObjectCallback;

/**
 * Created by ShenYunjie on 2016/2/19.
 */
public class OrderEvaluateModel extends BaseModel<OrderEvaluate> {
    public Callback.Cancelable orderEvaluate(ObjectCallback<ResponseMessage> callback, @Nullable OrderEvaluate evaluate) {
        String evaluateStr = JSONObject.toJSONString(evaluate);
        ModelParams params = new ModelParams().addParam("orderEvaluateStr", evaluateStr);
        return new SimpleModel(ResponseMessage.class).postObject(Constant.ORDER_EVALUATE_URL, params, callback);
    }
}
