package cn.com.zhihetech.online.model;

import android.support.annotation.NonNull;

import org.xutils.common.Callback;

import java.text.MessageFormat;

import cn.com.zhihetech.online.bean.GoodsScore;
import cn.com.zhihetech.online.core.common.Constant;
import cn.com.zhihetech.online.core.common.Pager;
import cn.com.zhihetech.online.core.http.PageDataCallback;

/**
 * Created by ShenYunjie on 2016/3/16.
 */
public class GoodsScoreModel extends BaseModel<GoodsScore> {

    /**
     * 根据商品ID获取商品评论
     *
     * @param callback
     * @param pager
     * @param goodsId
     * @return
     */
    public Callback.Cancelable getGoodsScoresByGoodsId(PageDataCallback<GoodsScore> callback, Pager pager, @NonNull String goodsId) {
        String url = MessageFormat.format(Constant.GOODS_COMMNETS_URL, goodsId);
        return getPageData(url, new ModelParams().addPager(pager), callback);
    }
}
