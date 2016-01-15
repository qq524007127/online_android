package cn.com.zhihetech.online.bean;

/**
 * Created by ShenYunjie on 2016/1/13.
 */

public class HobbyTag extends BaseBean {
    private String tagId;
    private String tagName;
    private String tagDesc; //标签描述
    private HobbyTag parentTag; //父标签

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getTagDesc() {
        return tagDesc;
    }

    public void setTagDesc(String tagDesc) {
        this.tagDesc = tagDesc;
    }

    public HobbyTag getParentTag() {
        return parentTag;
    }

    public void setParentTag(HobbyTag parentTag) {
        this.parentTag = parentTag;
    }
}
