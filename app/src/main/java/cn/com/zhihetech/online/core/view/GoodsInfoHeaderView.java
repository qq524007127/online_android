package cn.com.zhihetech.online.core.view;

import android.content.Context;
import android.content.Intent;
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
import java.util.ArrayList;
import java.util.List;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.Goods;
import cn.com.zhihetech.online.bean.GoodsBanner;
import cn.com.zhihetech.online.bean.ImgInfo;
import cn.com.zhihetech.online.bean.ShowImageInfo;
import cn.com.zhihetech.online.core.http.ArrayCallback;
import cn.com.zhihetech.online.core.util.ImageLoader;
import cn.com.zhihetech.online.core.util.StringUtils;
import cn.com.zhihetech.online.model.GoodsBannerModel;
import cn.com.zhihetech.online.ui.activity.GoodsCommentActivity;
import cn.com.zhihetech.online.ui.activity.ShowBigImageActivity;

/**
 * Created by ShenYunjie on 2016/1/20.
 */
public class GoodsInfoHeaderView extends FrameLayout {
    @ViewInject(R.id.goods_info_header_banner)
    protected ZhiheBanner goodsBanner;
    @ViewInject(R.id.goods_info_name_tv)
    protected TextView goodsNameTv;
    @ViewInject(R.id.goods_info_price_tv)
    protected TextView goodsPriceTv;
    @ViewInject(R.id.goods_info_stock_tv)
    protected TextView goodsStock;
    @ViewInject(R.id.goods_info_carriage_tv)
    protected TextView carriageTv;
    @ViewInject(R.id.goods_info_volume_tv)
    protected TextView volumeTv;
    @ViewInject(R.id.goods_info_desc_tv)
    protected TextView goodsDescTv;
    @ViewInject(R.id.goods_info_evaluate_view)
    private View goodsCommentView;

    protected String goodsId;

    protected ArrayCallback<GoodsBanner> bannerCallback = new ArrayCallback<GoodsBanner>() {
        @Override
        public void onArray(final List<GoodsBanner> datas) {
            goodsBanner.setPages(new CBViewHolderCreator<GoodsBannerHolder>() {
                @Override
                public GoodsBannerHolder createHolder() {
                    GoodsBannerHolder bannerHolder = new GoodsBannerHolder(datas);
                    bannerHolder.setGoodsBannerClickListener(new OnGoodsBannerClickListener() {
                        @Override
                        public void onBannerClick(List<GoodsBanner> goodsBanners, int position) {
                            Intent intent = new Intent(getContext(), ShowBigImageActivity.class);
                            intent.putExtra(ShowBigImageActivity.CURRENT_POSITION, position);
                            ArrayList<ShowImageInfo> imgList = new ArrayList<>();
                            for (GoodsBanner banner : datas) {
                                ShowImageInfo showImageInfo = new ShowImageInfo(banner.getImgInfo().getUrl(), null);
                                showImageInfo.setShowDesc(false);
                                imgList.add(showImageInfo);
                            }
                            intent.putExtra(ShowBigImageActivity.IMAGE_LIST_KEY, imgList);
                            getContext().startActivity(intent);
                        }
                    });
                    return bannerHolder;
                }
            }, datas);
        }
    };

    public GoodsInfoHeaderView(Context context, @NonNull String goodsId) {
        super(context);
        this.goodsId = goodsId;
        init();
    }

    protected void init() {
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.content_goods_info_header, null);
        addView(rootView);
        x.view().inject(this, rootView);
        initBanners();
    }

    private void initBanners() {
        new GoodsBannerModel().getGoodsBannersByGoodsId(bannerCallback, goodsId);
    }

    public void bindGoodsData(@NonNull final Goods goods) {
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
            text = MessageFormat.format(getContext().getString(R.string.goods_desc), goods.getGoodsDesc().trim());
        }
        goodsDescTv.setText(text);

        goodsCommentView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), GoodsCommentActivity.class);
                intent.putExtra(GoodsCommentActivity.GOODS_ID_KEY, goods.getGoodsId());
                getContext().startActivity(intent);
            }
        });
    }

    public class GoodsBannerHolder implements Holder<GoodsBanner> {

        private List<GoodsBanner> goodsBanners;
        private ImageView bannerImg;
        private OnGoodsBannerClickListener goodsBannerClickListener;

        public GoodsBannerHolder(List<GoodsBanner> goodsBanners) {
            this.goodsBanners = goodsBanners;
        }

        @Override
        public View createView(Context context) {
            bannerImg = new ImageView(context);
            bannerImg.setScaleType(ImageView.ScaleType.FIT_XY);
            return bannerImg;
        }

        @Override
        public void UpdateUI(Context context, final int position, GoodsBanner data) {
            ImageLoader.disPlayImage(bannerImg, data.getImgInfo());
            if (goodsBannerClickListener != null) {
                bannerImg.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goodsBannerClickListener.onBannerClick(goodsBanners, position);
                    }
                });
            }
        }

        public void setGoodsBannerClickListener(OnGoodsBannerClickListener goodsBannerClickListener) {
            this.goodsBannerClickListener = goodsBannerClickListener;
        }
    }

    public interface OnGoodsBannerClickListener {
        void onBannerClick(List<GoodsBanner> goodsBanners, int position);
    }
}
