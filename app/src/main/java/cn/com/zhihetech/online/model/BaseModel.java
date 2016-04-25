package cn.com.zhihetech.online.model;

import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.WindowManager;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.core.ZhiheApplication;
import cn.com.zhihetech.online.core.common.ActivityStack;
import cn.com.zhihetech.online.core.common.Constant;
import cn.com.zhihetech.online.core.common.PageData;
import cn.com.zhihetech.online.core.common.ResponseMessage;
import cn.com.zhihetech.online.core.common.ResponseStateCode;
import cn.com.zhihetech.online.core.http.ArrayCallback;
import cn.com.zhihetech.online.core.http.ObjectCallback;
import cn.com.zhihetech.online.core.http.PageDataCallback;
import cn.com.zhihetech.online.core.http.ResponseMessageCallback;
import cn.com.zhihetech.online.core.http.RequestCallback;
import cn.com.zhihetech.online.core.http.SimpleCallback;
import cn.com.zhihetech.online.core.util.AppUtils;
import cn.com.zhihetech.online.core.util.GenericTypeUtils;
import cn.com.zhihetech.online.core.util.SharedPreferenceUtils;
import cn.com.zhihetech.online.core.util.StringUtils;
import cn.com.zhihetech.online.ui.activity.LoginActivity;

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
                    callback.onObject(JSONObject.parseObject(result, getParamTypeClass()));
                }
            }
        });
    }

    protected Callback.Cancelable getArray(@NonNull String url, ModelParams params, final ArrayCallback<T> callback) {
        RequestParams requestParams = createRequestParams(url, params);
        return x.http().get(requestParams, new BaseCallback(callback) {
            @Override
            public void onSuccess(String result) {
                List<T> datas = JSONArray.parseArray(result, getParamTypeClass());
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
                rows = JSONArray.parseArray(JSONObject.toJSONString(pageData.getRows()), getParamTypeClass());
                pageData.setRows(rows);
                Log.d("BaseModel.getPageData()", result);
                callback.onPageData(pageData, rows);
            }
        });
    }

    protected Callback.Cancelable getResponseMessage(@NonNull String url, ModelParams params, final ResponseMessageCallback<T> callback) {
        return x.http().get(createRequestParams(url, params), new BaseCallback(callback) {
            @Override
            public void onSuccess(String result) {
                if (callback == null) {
                    return;
                }
                callback.onSuccess(result);
                ResponseMessage<T> responseMessage = JSONObject.parseObject(result, ResponseMessage.class);
                T data = JSONObject.parseObject(JSONObject.toJSONString(responseMessage.getData()), getParamTypeClass());
                responseMessage.setData(data);
                callback.onResponseMessage(responseMessage);
            }
        });
    }

    public Callback.Cancelable getText(@NonNull String url, ModelParams params, final SimpleCallback callback) {
        return x.http().get(createRequestParams(url, params), new BaseCallback(callback) {
            @Override
            public void onSuccess(String result) {
                if (callback != null) {
                    callback.onSuccess(result);
                }
            }
        });
    }

    /*==================================================POST提交======================================================*/
    protected Callback.Cancelable postObject(@NonNull String url, ModelParams params, final ObjectCallback<T> callback) {
        RequestParams requestParams = createRequestParams(url, params);
        return x.http().post(requestParams, new BaseCallback(callback) {
            @Override
            public void onSuccess(String result) {
                if (callback != null) {
                    callback.onSuccess(result);
                    callback.onObject(JSONObject.parseObject(result, getParamTypeClass()));
                }
            }
        });
    }

    protected Callback.Cancelable postArray(@NonNull String url, ModelParams params, final ArrayCallback<T> callback) {
        RequestParams requestParams = createRequestParams(url, params);
        return x.http().post(requestParams, new BaseCallback(callback) {
            @Override
            public void onSuccess(String result) {
                if (Constant.DEBUG) {
                    Log.d("HomeHeaderView", result);
                }
                List<T> datas = JSONArray.parseArray(result, getParamTypeClass());
                if (callback != null) {
                    callback.onSuccess(result);
                    callback.onArray(datas);
                }
            }
        });
    }

    protected Callback.Cancelable postPageData(@NonNull String url, ModelParams params, final PageDataCallback<T> callback) {
        return x.http().post(createRequestParams(url, params), new BaseCallback(callback) {
            @Override
            public void onSuccess(String result) {
                if (callback == null) {
                    return;
                }
                callback.onSuccess(result);
                PageData<T> pageData = JSONObject.parseObject(result, PageData.class);
                List<T> rows = new ArrayList<T>();
                rows = JSONArray.parseArray(JSONObject.toJSONString(pageData.getRows()), getParamTypeClass());
                pageData.setRows(rows);
                Log.d("BaseModel.getPageData()", result);
                callback.onPageData(pageData, rows);
            }
        });
    }

    protected Callback.Cancelable postResponseMessage(@NonNull String url, ModelParams params, final ResponseMessageCallback<T> callback) {
        return x.http().post(createRequestParams(url, params), new BaseCallback(callback) {
            @Override
            public void onSuccess(String result) {
                if (callback == null) {
                    return;
                }
                callback.onSuccess(result);
                ResponseMessage<T> responseMessage = JSONObject.parseObject(result, ResponseMessage.class);
                T data = JSONObject.parseObject(JSONObject.toJSONString(responseMessage.getData()), getParamTypeClass());
                responseMessage.setData(data);
                callback.onResponseMessage(responseMessage);
            }
        });
    }

    public Callback.Cancelable postText(@NonNull String url, ModelParams params, final SimpleCallback callback) {
        return x.http().post(createRequestParams(url, params), new BaseCallback(callback) {
            @Override
            public void onSuccess(String result) {
                if (callback != null) {
                    callback.onSuccess(result);
                }
            }
        });
    }

    protected RequestParams createRequestParams(@NonNull String url) {
        return createRequestParams(url, null);
    }

    /**
     * 统一创建请求参数
     *
     * @param url
     * @param params
     * @return
     */
    protected RequestParams createRequestParams(@NonNull String url, ModelParams params) {
        RequestParams requestParams = new RequestParams(url);
        requestParams.setCharset(Constant.ENCODING);
        settingRequestHeader(requestParams);

        //在此处初始化登录令牌（token)
        if (params == null || params.getParams().isEmpty()) {
            return requestParams;
        }
        Set<String> keies = params.getParams().keySet();
        for (String key : keies) {
            if (StringUtils.isEmpty(key)) {
                continue;
            }
            requestParams.addBodyParameter(key, params.getParams().get(key));
        }
        return requestParams;
    }

    protected void settingRequestHeader(RequestParams params) {
        Application application = ZhiheApplication.getInstance();
        SharedPreferenceUtils sharedPreferenceUtils = SharedPreferenceUtils.getInstance(application);
        String token = sharedPreferenceUtils.getUserToken();
        params.addHeader("mobileName", Build.MODEL);    //手机型号
        params.addHeader("osName", "Android");  //手机操作系统名称
        params.addHeader("osVersion", Build.VERSION.RELEASE);    //操作系统版本
       if (!StringUtils.isEmpty(token)) {
            params.addHeader("token", token);    //用户token（只用登录成功之后才会有此项)
            params.addHeader("userCode", sharedPreferenceUtils.getUserCode()); //当前发送请求的用户ID(登录陈宫之后才有此项）
        }
        params.addHeader("appVersionCode", String.valueOf(AppUtils.getVersionCode(application)));   //软件版本
        params.addHeader("appVersionName", AppUtils.getVersionName(application));   //软件版本名称
    }

    protected Class<T> getParamTypeClass() {
        return (Class<T>) GenericTypeUtils.getGenerParamType(this.getClass());
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
            if (!isOnCallback) {
                Log.e("BaseModel", ex.getMessage());
            }
            if (ex instanceof HttpException) {
                HttpException exception = (HttpException) ex;
                if (exception.getCode() == ResponseStateCode.UNAUTHORIZED) {
                    onUnauthorized();
                    return;
                }
            }
            if (callback != null) {
                callback.onError(ex, isOnCallback);
            }
        }

        /**
         * 当未登录(未授权)时统一在此处理
         */
        private void onUnauthorized() {
            final String UNAUTHORIZED_DIALOG = "unauthorized_dialog";
            final ZhiheApplication application = ZhiheApplication.getInstance();
            if (ActivityStack.getInstance().getActivities().isEmpty()) {
                return;
            } else if (application.getExtAttribute(UNAUTHORIZED_DIALOG) != null) {
                return;
            }
            AlertDialog alertDialog = new AlertDialog.Builder(application)
                    .setTitle("警告")
                    .setMessage("你还未登录或登录已过期，是否现在登录？")
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            application.removeExtAttribute(UNAUTHORIZED_DIALOG);
                            ActivityStack.getInstance().clearActivity();
                            application.onExitAccount();
                            application.startActivity(new Intent(application, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            application.removeExtAttribute(UNAUTHORIZED_DIALOG);
                            application.onExitAccount();
                            ActivityStack.getInstance().clearActivity();
                        }
                    }).create();
            alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            alertDialog.show();
            application.addExtAttribute(UNAUTHORIZED_DIALOG, alertDialog);
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
