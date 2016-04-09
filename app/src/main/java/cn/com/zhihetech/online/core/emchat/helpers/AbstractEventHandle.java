package cn.com.zhihetech.online.core.emchat.helpers;

import com.easemob.chat.EMMessage;

/**
 * Created by ShenYunjie on 2016/4/8.
 */
public abstract class AbstractEventHandle implements EMEventHandle.OnEMEventListener {

    @Override
    public abstract int getLevel();

    @Override
    public boolean onNewMessage(EMMessage message) {
        return true;
    }

    @Override
    public boolean onNewCMDMessage(EMMessage message) {
        return true;
    }
}
