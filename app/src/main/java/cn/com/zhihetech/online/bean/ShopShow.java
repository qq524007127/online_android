package cn.com.zhihetech.online.bean;

/**
 * 店铺图片展示
 * Created by ShenYunjie on 2016/1/11.
 */
public class ShopShow extends BaseBean {
    private String showId;
    private Merchant merchant;
    private ImgInfo imgInfo;
    private String showDesc; //图片描述

    public ShopShow() {
    }

    public String getShowId() {
        return showId;
    }

    public void setShowId(String showId) {
        this.showId = showId;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public ImgInfo getImgInfo() {
        return imgInfo;
    }

    public void setImgInfo(ImgInfo imgInfo) {
        this.imgInfo = imgInfo;
    }

    public String getShowDesc() {
        return showDesc;
    }

    public void setShowDesc(String showDesc) {
        this.showDesc = showDesc;
    }
}
