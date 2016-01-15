package cn.com.zhihetech.online.bean;


/**
 * Created by YangDaiChun on 2015/11/12.
 */

public class District extends BaseBean {

    private String districtId;
    private Area districtArea;  //商圈所属区域
    private String districtName;
    private String districtDesc; //描述

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }


    public Area getDistrictArea() {
        return districtArea;
    }

    public void setDistrictArea(Area districtArea) {
        this.districtArea = districtArea;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getDistrictDesc() {
        return districtDesc;
    }

    public void setDistrictDesc(String districtDesc) {
        this.districtDesc = districtDesc;
    }
}
