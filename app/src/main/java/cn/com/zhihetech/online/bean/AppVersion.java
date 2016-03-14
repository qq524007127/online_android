package cn.com.zhihetech.online.bean;

/**
 * Created by ShenYunjie on 2016/3/14.
 */
public class AppVersion extends BaseBean {
    private int versionCode;    //版本序号
    private String versionName; //名称
    private String versionUrl;  //现在地址
    private String versionDisc; //版本描述

    public AppVersion() {
    }

    public AppVersion(int versionCode, String versionName, String versionUrl, String versionDisc) {
        this.versionCode = versionCode;
        this.versionName = versionName;
        this.versionUrl = versionUrl;
        this.versionDisc = versionDisc;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getVersionUrl() {
        return versionUrl;
    }

    public void setVersionUrl(String versionUrl) {
        this.versionUrl = versionUrl;
    }

    public String getVersionDisc() {
        return versionDisc;
    }

    public void setVersionDisc(String versionDisc) {
        this.versionDisc = versionDisc;
    }
}
