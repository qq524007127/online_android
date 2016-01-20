package cn.com.zhihetech.online.bean;

import java.util.Date;

/**
 * Created by YangDaiChun on 2015/11/12.
 */

public class GoodsBanner extends BaseBean{

    private String goodsBannerId;
    private ImgInfo imgInfo;
    private Goods goods;
    private int bannerOrder;
    private Date createDate;

    public String getGoodsBannerId() {
        return goodsBannerId;
    }

    public void setGoodsBannerId(String goodsBannerId) {
        this.goodsBannerId = goodsBannerId;
    }

    public ImgInfo getImgInfo() {
        return imgInfo;
    }

    public void setImgInfo(ImgInfo imgInfo) {
        this.imgInfo = imgInfo;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
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
