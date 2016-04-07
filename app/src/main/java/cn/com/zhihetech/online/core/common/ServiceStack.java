package cn.com.zhihetech.online.core.common;

import java.util.Stack;

import cn.com.zhihetech.online.core.service.BaseService;

/**
 * Created by ShenYunjie on 2016/4/7.
 */
public class ServiceStack {
    private Stack<BaseService> serviceStack;
    private static ServiceStack instance;

    public ServiceStack() {
        this.serviceStack = new Stack<>();
    }

    public static ServiceStack getInstance() {
        if (instance == null) {
            instance = new ServiceStack();
        }
        return instance;
    }

    public void clearService() {
        for (BaseService service : serviceStack) {
            service.stopSelf();
        }
        serviceStack.clear();
    }

    public void addService(BaseService service) {
        this.serviceStack.push(service);
    }

    public void removeService(BaseService service) {
        this.serviceStack.remove(service);
    }
}
