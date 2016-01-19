package cn.com.zhihetech.online.ui.fragment;

import cn.com.zhihetech.online.model.ModelParams;

/**
 * 延迟初始化数据的fragment
 * Created by ShenYunjie on 2016/1/19.
 */
public abstract class DelayInitFragment extends BaseFragment {
    private boolean inited = false;

    public final void init(ModelParams params) {
        if (!inited) {
            inited = true;
            initParams(params);
        }
    }

    protected void initParams(ModelParams params) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        inited = false;
    }
}
