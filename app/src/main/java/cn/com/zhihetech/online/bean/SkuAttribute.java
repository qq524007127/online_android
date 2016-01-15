package cn.com.zhihetech.online.bean;

import java.util.Set;

/**
 * Created by YangDaiChun on 2015/12/2.
 * <p/>
 * sku属性,常见的sku属性如尺寸：XL/XXL/S/M ;颜色：红色，蓝色
 */
public class SkuAttribute extends BaseBean {

    private String skuAttId;
    private String skuAttCode; //sku属性编号
    private String skuAttName; //sku属性名:如 红色
    private String skuAttDesc;
    private SkuAttribute parentySkuAttribute;
    private Set<SkuAttribute> childSkuAttributes;
    private Set<GoodsAttributeSet> goodsAttributeSets;

    public String getSkuAttId() {
        return skuAttId;
    }

    public void setSkuAttId(String skuAttId) {
        this.skuAttId = skuAttId;
    }

    public String getSkuAttCode() {
        return skuAttCode;
    }

    public void setSkuAttCode(String skuAttCode) {
        this.skuAttCode = skuAttCode;
    }

    public String getSkuAttName() {
        return skuAttName;
    }

    public void setSkuAttName(String skuAttName) {
        this.skuAttName = skuAttName;
    }

    public String getSkuAttDesc() {
        return skuAttDesc;
    }

    public void setSkuAttDesc(String skuAttDesc) {
        this.skuAttDesc = skuAttDesc;
    }

    public SkuAttribute getParentySkuAttribute() {
        return parentySkuAttribute;
    }

    public void setParentySkuAttribute(SkuAttribute parentySkuAttribute) {
        this.parentySkuAttribute = parentySkuAttribute;
    }

    public Set<SkuAttribute> getChildSkuAttributes() {
        return childSkuAttributes;
    }

    public void setChildSkuAttributes(Set<SkuAttribute> childSkuAttributes) {
        this.childSkuAttributes = childSkuAttributes;
    }

    public Set<GoodsAttributeSet> getGoodsAttributeSets() {
        return goodsAttributeSets;
    }

    public void setGoodsAttributeSets(Set<GoodsAttributeSet> goodsAttributeSets) {
        this.goodsAttributeSets = goodsAttributeSets;
    }

}
