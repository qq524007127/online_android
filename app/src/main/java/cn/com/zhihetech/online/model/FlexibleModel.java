package cn.com.zhihetech.online.model;

/**
 * 可灵活自定义类型的Model
 * Created by ShenYunjie on 2016/1/19.
 */
public class FlexibleModel<T> extends BaseModel<T> {

    protected Class<T> clazz;

    public FlexibleModel(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Class<T> getParamTypeClass() {
        return this.clazz;
    }
}
