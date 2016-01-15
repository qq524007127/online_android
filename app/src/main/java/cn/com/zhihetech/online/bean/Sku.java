package cn.com.zhihetech.online.bean;

/**
 * Created by YangDaiChun on 2015/12/2.
 *
 * 商品的SKU，例如一件上架的衣服，它的其中的一种组合 XL + 红色 就会形成这件衣服的一个SKU
 */

public class Sku extends BaseBean {

    private String skuId;
    /**
     * 作为一个SKU的唯一标识，sku = 商品编码+各个sku组合
     */
    private String skuCode;
    /**
     * 商品单品价格
     */
    private float price;
    /**
     * 商品单品库存量
     */
    private long stock;

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public long getStock() {
        return stock;
    }

    public void setStock(long stock) {
        this.stock = stock;
    }
}
