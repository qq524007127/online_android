package cn.com.zhihetech.online.bean;

/**
 * 实淘会客厅
 *
 * Created by ShenYunjie on 2016/4/5.
 */
public class ReceptionRoom extends BaseBean {
    private String roomId;
    private String roomName;
    private ImgInfo coverImg;   //封面图
    private String templatePath;
    private boolean deleted = false;

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public ImgInfo getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(ImgInfo coverImg) {
        this.coverImg = coverImg;
    }

    public String getTemplatePath() {
        return templatePath;
    }

    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
