package cn.com.zhihetech.online.ui.view;

import cn.com.zhihetech.online.bean.ImgInfo;

/**
 * Created by ShenYunjie on 2016/1/15.
 */
public interface StartView extends BaseView {
    void onDispalyStartImage(ImgInfo startImg);

    void onLoginSuccess();

    void onLogFail();
}
