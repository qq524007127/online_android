package cn.com.zhihetech.online.ui.fragment;

import com.easemob.easeui.ui.EaseConversationListFragment;

/**
 * Created by ShenYunjie on 2016/2/2.
 */
public class ConversationListFragment extends EaseConversationListFragment {

    @Override
    protected void initView() {
        super.initView();
        hideTitleBar();
    }
}
