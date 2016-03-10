package cn.com.zhihetech.online.model;

import org.xutils.common.Callback;

import cn.com.zhihetech.online.bean.FeaturedBlock;
import cn.com.zhihetech.online.core.common.Constant;
import cn.com.zhihetech.online.core.common.Pager;
import cn.com.zhihetech.online.core.http.PageDataCallback;

/**
 * Created by ShenYunjie on 2016/3/10.
 */
public class FeaturedBlockModel extends BaseModel<FeaturedBlock> {

    /**
     * 分页获取特色街区
     *
     * @param callback
     * @param pager
     * @return
     */
    public Callback.Cancelable getFeaturedBlocks(PageDataCallback<FeaturedBlock> callback, Pager pager) {
        return getPageData(Constant.FEATURED_BLOCKS_URL, new ModelParams().addPager(pager), callback);
    }
}
