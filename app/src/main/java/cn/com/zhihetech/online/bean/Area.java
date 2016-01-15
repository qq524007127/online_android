package cn.com.zhihetech.online.bean;


public class Area extends BaseBean {

    private String areaId;
    private String areaName;
    private boolean isRoot;
    private Area parentArea;
    private String fullName;

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public boolean getIsRoot() {
        return isRoot;
    }

    public void setIsRoot(boolean isRoot) {
        this.isRoot = isRoot;
    }

    public Area getParentArea() {
        return parentArea;
    }

    public void setParentArea(Area parentArea) {
        this.parentArea = parentArea;
    }

    public String getFullName() {
        this.fullName = this.getAreaName();
        if(this.getParentArea()!=null){
            this.fullName = getParentFullName(this.getParentArea()) + this.fullName;
        }
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    private String getParentFullName(Area pArea) {

        String tmp = pArea.getAreaName();
        if (pArea.getParentArea() != null && !pArea.isRoot){
            tmp = getParentFullName(pArea.getParentArea()) + tmp;
        }
        return tmp + ",";
    }
}
