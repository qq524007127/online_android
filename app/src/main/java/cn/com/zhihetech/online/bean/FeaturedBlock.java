package cn.com.zhihetech.online.bean;

/**
 * 特色街区
 * Created by ShenYunjie on 2016/3/10.
 */
public class FeaturedBlock extends BaseBean {
    private String fbId;
    private String fbName;
    private int fbOrder;
    private boolean permit;
    private Area area;
    private ImgInfo coverImg;

    public String getFbId() {
        return fbId;
    }

    public void setFbId(String fbId) {
        this.fbId = fbId;
    }

    public String getFbName() {
        return fbName;
    }

    public void setFbName(String fbName) {
        this.fbName = fbName;
    }

    public int getFbOrder() {
        return fbOrder;
    }

    public void setFbOrder(int fbOrder) {
        this.fbOrder = fbOrder;
    }

    public boolean isPermit() {
        return permit;
    }

    public void setPermit(boolean permit) {
        this.permit = permit;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public ImgInfo getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(ImgInfo coverImg) {
        this.coverImg = coverImg;
    }
}
