package cn.com.zhihetech.online.bean;

/**
 * Created by ShenYunjie on 2016/3/10.
 */
public class ShoppingCenter extends BaseBean {

    private String scId;  //id
    private String scName;  //名称
    private int scOrder;   //顺序
    private boolean permit;  //是否启用
    private ImgInfo coverImg;

    public String getScId() {
        return scId;
    }

    public void setScId(String scId) {
        this.scId = scId;
    }

    public String getScName() {
        return scName;
    }

    public void setScName(String scName) {
        this.scName = scName;
    }

    public int getScOrder() {
        return scOrder;
    }

    public void setScOrder(int scOrder) {
        this.scOrder = scOrder;
    }

    public boolean isPermit() {
        return permit;
    }

    public void setPermit(boolean permit) {
        this.permit = permit;
    }

    public ImgInfo getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(ImgInfo coverImg) {
        this.coverImg = coverImg;
    }
}
