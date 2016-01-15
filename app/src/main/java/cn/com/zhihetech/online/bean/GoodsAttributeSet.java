package cn.com.zhihetech.online.bean;

import java.util.Date;
import java.util.Set;

/**
 * Created by YangDaiChun on 2015/12/2.
 * <p>
 * 商品或商家的属性集：例如常见的属性集有：服装、PC、家具、图书等
 */
public class GoodsAttributeSet extends BaseBean {

    private String goodsAttSetId;
    private String goodsAttSetName;  //属性名：如服装
    private String goodsAttSetDesc;
    private Date creatDate;
    private ImgInfo coverImg;
    private boolean permit;
    private Set<SkuAttribute> skuAttributes;  //此处为多对多关系 一类商品有多种sku属性，如服饰这类商品有颜色和尺寸的sku属性



    public String getGoodsAttSetId() {
        return goodsAttSetId;
    }

    public void setGoodsAttSetId(String goodsAttSetId) {
        this.goodsAttSetId = goodsAttSetId;
    }

    public String getGoodsAttSetName() {
        return goodsAttSetName;
    }

    public void setGoodsAttSetName(String goodsAttSetName) {
        this.goodsAttSetName = goodsAttSetName;
    }

    public String getGoodsAttSetDesc() {
        return goodsAttSetDesc;
    }

    public void setGoodsAttSetDesc(String goodsAttSetDesc) {
        this.goodsAttSetDesc = goodsAttSetDesc;
    }


    public Set<SkuAttribute> getSkuAttributes() {
        return skuAttributes;
    }

    public void setSkuAttributes(Set<SkuAttribute> skuAttributes) {
        this.skuAttributes = skuAttributes;
    }

    public Date getCreatDate() {
        return creatDate;
    }

    public void setCreatDate(Date creatDate) {
        this.creatDate = creatDate;
    }

    public ImgInfo getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(ImgInfo coverImg) {
        this.coverImg = coverImg;
    }

    public boolean isPermit() {
        return permit;
    }

    public void setPermit(boolean permit) {
        this.permit = permit;
    }
}
