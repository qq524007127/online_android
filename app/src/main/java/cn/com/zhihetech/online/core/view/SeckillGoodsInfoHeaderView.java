package cn.com.zhihetech.online.core.view;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.widget.TextView;

import org.xutils.view.annotation.ViewInject;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.ActivityGoods;
import cn.com.zhihetech.online.bean.Goods;

/**
 * Created by ShenYunjie on 2016/3/8.
 */
public class SeckillGoodsInfoHeaderView extends GoodsInfoHeaderView {

    @ViewInject(R.id.seckill_goods_info_old_price_tv)
    protected TextView seckillOldPriceTv;

    public SeckillGoodsInfoHeaderView(Context context, @NonNull String goodsId) {
        super(context, goodsId);
    }

    @Override
    protected void init() {
        super.init();
        seckillOldPriceTv.setVisibility(VISIBLE);
        seckillOldPriceTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
    }

    public void bindActivityGoods(@NonNull ActivityGoods activityGoods) {
        Goods goods = activityGoods.getGoods();
        bindGoodsData(goods);
        this.goodsPriceTv.setText("现价:" + activityGoods.getActivityPrice());
        this.seckillOldPriceTv.setText("原价:" + goods.getPrice());
        this.goodsStock.setText("还剩:(" + activityGoods.getCount() + ")");
        this.volumeTv.setText("");
    }
}
