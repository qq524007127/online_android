package cn.com.zhihetech.online.bean;

import java.util.Date;

/**
 * Created by ShenYunjie on 2015/11/12.
 */

public class ImgInfo extends BaseBean {

    private String imgInfoId;
    private int width;
    private int height;
    private String key;
    private Date createDate;
    private String bucket;

    private String domain = "http://7xofn0.com1.z0.glb.clouddn.com/";

    private String url;

    public String getImgInfoId() {
        return imgInfoId;
    }

    public void setImgInfoId(String imgInfoId) {
        this.imgInfoId = imgInfoId;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getUrl() {
        setUrl(this.domain + this.key);
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
