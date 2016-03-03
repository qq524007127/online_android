package cn.com.zhihetech.online.core.common;

import java.io.Serializable;

/**
 * Created by ShenYunjie on 2016/1/20.
 */
public class ResponseStateCode implements Serializable, Cloneable {
    public final static int SUCCESS = 200;  //操作成功
    public final static int UN_FOCUS_GOODS = 715;   //未关注
    public final static int FOCUSED_GOODS = 710;    //已关注
    public static int NOT_FOUND = 404;  //未找到目标
    /**
     * 红包已经被抢光
     */
    public final static int RED_ENVELOP_FINISHED = 850;
    public final static int RED_ENVELOP_ALREADY_RECEIVED = 855; //红包已领取
}
