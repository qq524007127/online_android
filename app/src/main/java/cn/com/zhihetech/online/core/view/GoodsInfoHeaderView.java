package cn.com.zhihetech.online.core.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.MessageFormat;
import java.util.List;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.Goods;
import cn.com.zhihetech.online.bean.GoodsBanner;
import cn.com.zhihetech.online.core.http.ArrayCallback;
import cn.com.zhihetech.online.core.util.ImageLoader;
import cn.com.zhihetech.online.core.util.StringUtils;
import cn.com.zhihetech.online.model.GoodsBannerModel;

/**
 * Created by ShenYunjie on 2016/1/20.
 */
public class GoodsInfoHeaderView extends FrameLayout {
    @ViewInject(R.id.goods_info_header_banner)
    private ZhiheBanner goodsBanner;
    @ViewInject(R.id.goods_info_name_tv)
    private TextView goodsNameTv;
    @ViewInject(R.id.goods_info_price_tv)
    private TextView goodsPriceTv;
    @ViewInject(R.id.goods_info_stock_tv)
    private TextView goodsStock;
    @ViewInject(R.id.goods_info_carriage_tv)
    private TextView carriageTv;
    @ViewInject(R.id.goods_info_volume_tv)
    private TextView volumeTv;
    @ViewInject(R.id.goods_info_desc_tv)
    private TextView goodsDescTv;

    private String goodsId;

    ArrayCallback<GoodsBanner> bannerCallback = new ArrayCallback<GoodsBanner>() {
        @Override
        public void onArray(List<GoodsBanner> datas) {
            goodsBanner.setPages(new CBViewHolderCreator<GoodsBannerHolder>() {
                @Override
                public GoodsBannerHolder createHolder() {
                    return new GoodsBannerHolder();
                }
            }, datas);
        }
    };

    public GoodsInfoHeaderView(Context context, @NonNull String goodsId) {
        super(context);
        this.goodsId = goodsId;
        init();
    }

    private void init() {
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.content_goods_info_header, null);
        addView(rootView);
        x.view().inject(this, rootView);
        initBanners();
    }

    private void initBanners() {
        new GoodsBannerModel().getGoodsBannersByGoodsId(bannerCallback, goodsId);
    }

    public void bindGoodsData(@NonNull Goods goods) {
        goodsNameTv.setText(goods.getGoodsName());
        String text = MessageFormat.format(getContext().getString(R.string.goods_price), goods.getPrice());
        goodsPriceTv.setText(text);
        text = MessageFormat.format(getContext().getString(R.string.goods_stock), goods.getCurrentStock());
        goodsStock.setText(text);
        text = MessageFormat.format(getContext().getString(R.string.goods_carriage), "包邮");
        if (goods.getCarriage() > 0) {
            text = MessageFormat.format(getContext().getString(R.string.goods_carriage), goods.getCarriage());
        }
        carriageTv.setText(text);
        text = MessageFormat.format(getContext().getString(R.string.goods_volume), goods.getVolume());
        volumeTv.setText(text);
        text = MessageFormat.format(getContext().getString(R.string.goods_desc), "暂无简介");
        if (!StringUtils.isEmpty(goods.getGoodsDesc())) {
            text = MessageFormat.format(getContext().getString(R.string.goods_desc), goods.getGoodsDesc());
        }
        goodsDescTv.setText(text);
    }

    public class GoodsBannerHolder implements Holder<GoodsBanner> {

        private ImageView bannerImg;

        @Override
        public View createView(Context context) {
            bannerImg = new ImageView(context);
            bannerImg.setScaleType(ImageView.ScaleType.FIT_XY);
            return bannerImg;
        }

        @Override
        public void UpdateUI(Context context, int position, GoodsBanner data) {
            ImageLoader.disPlayImage(bannerImg, data.getImgInfo());
            bannerImg.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
}
