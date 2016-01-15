package cn.com.zhihetech.online.core.view;

import java.util.List;

/**
 * Created by ShenYunjie on 2016/1/15.
 */
public interface OnAdapterChangeHandle<T> {
    void refreshData(List<T> datas);

    void addDatas(List<T> datas);

    void clearData();

    void insertData(int position, T data);

    void addData(T data);
}
