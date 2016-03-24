package cn.com.zhihetech.online.bean;

/**
 * Created by ShenYunjie on 2016/3/24.
 */
public class SystemConfig extends BaseBean {
    private String configId;
    private String configName;
    private String configValue;
    private int configType;
    private String configDesc;

    public String getConfigId() {
        return configId;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public int getConfigType() {
        return configType;
    }

    public void setConfigType(int configType) {
        this.configType = configType;
    }

    public String getConfigDesc() {
        return configDesc;
    }

    public void setConfigDesc(String configDesc) {
        this.configDesc = configDesc;
    }
}
