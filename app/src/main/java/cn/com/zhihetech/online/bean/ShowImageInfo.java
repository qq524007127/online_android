package cn.com.zhihetech.online.bean;

/**
 * 用于展示大图的实体类
 * Created by ShenYunjie on 2016/3/31.
 */
public class ShowImageInfo extends BaseBean {
    private String url; //图片地址
    private String desc;    //图片描述
    private boolean showDesc = false;   //是否要显示图片描述

    public ShowImageInfo() {
    }

    public ShowImageInfo(String url, String desc) {
        this.url = url;
        this.desc = desc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isShowDesc() {
        return showDesc;
    }

    public void setShowDesc(boolean showDesc) {
        this.showDesc = showDesc;
    }
}
