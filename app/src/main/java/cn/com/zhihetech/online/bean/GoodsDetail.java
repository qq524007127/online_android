package cn.com.zhihetech.online.bean;

/**
 * Created by YangDaiChun on 2015/11/13.
 */
public class GoodsDetail extends BaseBean {

    private String goodsDetailId;
    private Goods goods;
    private ImgInfo imgInfo;
    private int viewType;
    private String viewTarget;
    private int detailOrder;

    public String getGoodsDetailId() {
        return goodsDetailId;
    }

    public void setGoodsDetailId(String goodsDetailId) {
        this.goodsDetailId = goodsDetailId;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
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

    public String getViewTarget() {
        return viewTarget;
    }

    public void setViewTarget(String viewTarget) {
        this.viewTarget = viewTarget;
    }

    public int getDetailOrder() {
        return detailOrder;
    }

    public void setDetailOrder(int detailOrder) {
        this.detailOrder = detailOrder;
    }
}
