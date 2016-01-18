package cn.com.zhihetech.online.model;

import android.support.annotation.NonNull;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cn.com.zhihetech.online.core.common.Constant;
import cn.com.zhihetech.online.core.common.PageData;
import cn.com.zhihetech.online.core.common.ResponseMessage;
import cn.com.zhihetech.online.core.http.ArrayCallback;
import cn.com.zhihetech.online.core.http.ObjectCallback;
import cn.com.zhihetech.online.core.http.PageDataCallback;
import cn.com.zhihetech.online.core.http.ReponseMessageCallback;
import cn.com.zhihetech.online.core.http.RequestCallback;
import cn.com.zhihetech.online.core.util.GenericTypeUtils;
import cn.com.zhihetech.online.core.util.StringUtils;

/**
 * Created by ShenYunjie on 2016/1/15.
 */
public abstract class BaseModel<T> {

    protected Callback.Cancelable getObject(@NonNull String url, ModelParams params, final ObjectCallback<T> callback) {
        RequestParams requestParams = createRequestParams(url, params);
        return x.http().get(requestParams, new BaseCallback(callback) {
            @Override
            public void onSuccess(String result) {
                if (callback != null) {
                    callback.onSuccess(result);
                    callback.onObject((T) JSONObject.parseArray(result, getParamTypeClass()));
                }
            }
        });
    }

    protected Callback.Cancelable getArray(@NonNull String url, ModelParams params, final ArrayCallback<T> callback) {
        RequestParams requestParams = createRequestParams(url, params);
        return x.http().get(requestParams, new BaseCallback(callback) {
            @Override
            public void onSuccess(String result) {
                if (Constant.DEBUG) {
                    Log.d("HomeHeaderView", result);
                }
                List<T> datas = (List<T>) JSONArray.parseArray(result, getParamTypeClass());
                if (callback != null) {
                    callback.onSuccess(result);
                    callback.onArray(datas);
                }
            }
        });
    }

    protected Callback.Cancelable getPageData(@NonNull String url, ModelParams params, final PageDataCallback<T> callback) {
        return x.http().get(createRequestParams(url, params), new BaseCallback(callback) {
            @Override
            public void onSuccess(String result) {
                if (callback == null) {
                    return;
                }
                callback.onSuccess(result);
                PageData<T> pageData = JSONObject.parseObject(result, PageData.class);
                List<T> rows = new ArrayList<T>();
                rows = (List<T>) JSONArray.parseArray(JSONObject.toJSONString(pageData.getRows()), getParamTypeClass());
                pageData.setRows(rows);
                Log.d("BaseModel.getPageData()", result);
                callback.onPageData(pageData, rows);
            }
        });
    }

    protected Callback.Cancelable getResponseMessage(@NonNull String url, ModelParams params, final ReponseMessageCallback<T> callback) {
        return x.http().get(createRequestParams(url, params), new BaseCallback(callback) {
            @Override
            public void onSuccess(String result) {
                if (callback == null) {
                    return;
                }
                callback.onSuccess(result);
                ResponseMessage<T> responseMessage = JSONObject.parseObject(result, ResponseMessage.class);
                T data = (T) JSONObject.parseObject(JSONObject.toJSONString(responseMessage.getData()), getParamTypeClass());
                responseMessage.setData(data);
                callback.onResponseMessage(responseMessage);
            }
        });
    }

    /**
     * 发起get请求
     *
     * @param requestParams
     * @param callback
     * @return
     */
    protected Callback.Cancelable sendGetRequest(RequestParams requestParams, RequestCallback callback) {
        return x.http().get(requestParams, new BaseCallback(callback) {
            @Override
            public void onSuccess(String result) {

            }
        });
    }

    private RequestParams createRequestParams(@NonNull String url) {
        return createRequestParams(url, null);
    }

    /**
     * 统一创建请求参数
     *
     * @param url
     * @param params
     * @return
     */
    private RequestParams createRequestParams(@NonNull String url, ModelParams params) {
        RequestParams requestParams = new RequestParams(url);
        //在此处初始化登录令牌（token)
        if (params == null || params.getParams().isEmpty()) {
            return requestParams;
        }
        Set<String> keies = params.getParams().keySet();
        for (String key : keies) {
            if (StringUtils.isEmpty(key)) {
                continue;
            }
            requestParams.addBodyParameter(key,params.getParams().get(key));
        }
        return requestParams;
    }

    public Class<?> getParamTypeClass() {
        return GenericTypeUtils.getGenerParamType(this.getClass());
    }

    public abstract class BaseCallback implements Callback.CommonCallback<String> {

        private RequestCallback callback;

        public BaseCallback(RequestCallback callback) {
            this.callback = callback;
        }

        @Override
        public abstract void onSuccess(String result);

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            if (callback != null) {
                callback.onError(ex, isOnCallback);
            }
        }

        @Override
        public void onCancelled(CancelledException cex) {
            if (callback != null) {
                callback.onCancelled(cex);
            }
        }

        @Override
        public void onFinished() {
            if (callback != null) {
                callback.onFinished();
            }
        }
    }
}
