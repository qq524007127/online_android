package cn.com.zhihetech.online.bean;


/**
 * Created by YangDaiChun on 2015/11/12.
 */

public class Navigation extends BaseBean {
    private String navigationId;
    private String navigationName;  //导航名
    private String viewTargert;   //跳转到的模块
    private int order;   //导航顺序
    private boolean permit;   //是否启用
    private String desc;    //描述
    private ImgInfo img;   //导航图标
    private String viewUrl; //导航跳转的url地址，viewTarget为10时才有效

    public String getNavigationId() {
        return navigationId;
    }

    public void setNavigationId(String navigationId) {
        this.navigationId = navigationId;
    }

    public String getNavigationName() {
        return navigationName;
    }

    public void setNavigationName(String navigationName) {
        this.navigationName = navigationName;
    }


    public ImgInfo getImg() {
        return this.img;
    }

    public void setImg(ImgInfo img) {
        this.img = img;
    }

    public String getViewTargert() {
        return viewTargert;
    }

    public void setViewTargert(String viewTargert) {
        this.viewTargert = viewTargert;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public boolean isPermit() {
        return permit;
    }

    public void setPermit(boolean permit) {
        this.permit = permit;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getViewUrl() {
        return viewUrl;
    }

    public void setViewUrl(String viewUrl) {
        this.viewUrl = viewUrl;
    }
}
