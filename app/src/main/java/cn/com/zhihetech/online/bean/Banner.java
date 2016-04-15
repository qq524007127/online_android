package cn.com.zhihetech.online.bean;

import java.util.Date;

import cn.com.zhihetech.online.core.common.Constant;

public class Banner extends BaseBean {

    private String bannerId;
    private ImgInfo imgInfo;   //轮播图图片
    private int viewType;      //跳转的页面类型
    private String viewTargert;  //跳转目标
    private String viewTargetTitle; //跳转目标的标题
    private int bannerType = Constant.BANNER_MAIN;   //轮播图所处的位置
    private int bannerOrder;      //轮播图顺序
    private Date createDate;


    public String getBannerId() {
        return bannerId;
    }

    public void setBannerId(String bannerId) {
        this.bannerId = bannerId;
    }


    public ImgInfo getImgInfo() {
        return imgInfo;
    }

    public void setImgInfo(ImgInfo imgInfo) {
        this.imgInfo = imgInfo;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public String getViewTargert() {
        return viewTargert;
    }

    public void setViewTargert(String viewTargert) {
        this.viewTargert = viewTargert;
    }

    public String getViewTargetTitle() {
        return viewTargetTitle;
    }

    public void setViewTargetTitle(String viewTargetTitle) {
        this.viewTargetTitle = viewTargetTitle;
    }

    public int getBannerType() {
        return bannerType;
    }

    public void setBannerType(int bannerType) {
        this.bannerType = bannerType;
    }

    public int getBannerOrder() {
        return bannerOrder;
    }


    public void setBannerOrder(int bannerOrder) {
        this.bannerOrder = bannerOrder;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
