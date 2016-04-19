package cn.com.zhihetech.online.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

/**
 * Created by ShenYunjie on 2016/4/19.
 */
public class GoodsBrowse extends BaseBean {

    private String goodsBrowseId;
    private Goods goods;
    private Date browseDate;
    private User user;

    public String getGoodsBrowseId() {
        return goodsBrowseId;
    }

    public void setGoodsBrowseId(String goodsBrowseId) {
        this.goodsBrowseId = goodsBrowseId;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    public Date getBrowseDate() {
        return browseDate;
    }

    public void setBrowseDate(Date browseDate) {
        this.browseDate = browseDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
