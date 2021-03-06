package cn.com.zhihetech.online.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.easemob.easeui.widget.EaseImageView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.Merchant;
import cn.com.zhihetech.online.bean.ShopShow;
import cn.com.zhihetech.online.bean.ShowImageInfo;
import cn.com.zhihetech.online.core.adapter.ShopShowAdapter;
import cn.com.zhihetech.online.core.common.ResponseMessage;
import cn.com.zhihetech.online.core.common.ResponseStateCode;
import cn.com.zhihetech.online.core.http.ArrayCallback;
import cn.com.zhihetech.online.core.http.ResponseMessageCallback;
import cn.com.zhihetech.online.core.util.ImageLoader;
import cn.com.zhihetech.online.model.MerchantModel;
import cn.com.zhihetech.online.model.ShopShowModel;
import cn.com.zhihetech.online.ui.activity.ShowBigImageActivity;

/**
 * Created by ShenYunjie on 2016/1/20.
 */
@ContentView(R.layout.content_merchant_info_fragment)
public class MerchantInfoFragment extends BaseFragment {
    @ViewInject(R.id.merchant_info_header_civ)
    private EaseImageView merchantHeaderImg;
    @ViewInject(R.id.merchant_info_name_tv)
    private TextView merchantNameTv;
    @ViewInject(R.id.merchant_info_score_rb)
    private RatingBar merchantScoreRb;
    @ViewInject(R.id.merchant_info_address_tv)
    private TextView addressTv;
    @ViewInject(R.id.merchant_info_tell_tv)
    private TextView tellTv;
    @ViewInject(R.id.merchant_info_show_rv)
    private RecyclerView shopShowRv;
    @ViewInject(R.id.merchant_info_buss_photo_iv)
    private ImageView bussPhotoIv;
    @ViewInject(R.id.merchant_info_desc_tv)
    private TextView merchantDescTv;

    private String merchantId;
    private Merchant merchant;

    ResponseMessageCallback<Merchant> merchantCallbak = new ResponseMessageCallback<Merchant>() {
        @Override
        public void onResponseMessage(ResponseMessage<Merchant> responseMessage) {
            if (responseMessage.getCode() != ResponseStateCode.SUCCESS) {
                showMsg(responseMessage.getMsg());
                return;
            }
            merchant = responseMessage.getData();
            bindViews();
        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            showMsg("获取商家信息失败，请重试！");
        }
    };

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
        initData();
    }

    public BaseFragment getInstance(@NonNull String merchantId) {
        this.merchantId = merchantId;
        return this;
    }

    private void initViews() {
        shopShowRv.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        shopShowRv.setLayoutManager(layoutManager);
        loadShopShow(merchantId);
    }

    private void initData() {
        new MerchantModel().getMerchantById(merchantCallbak, merchantId);
    }

    public void bindViews() {
        ImageLoader.disPlayImage(merchantHeaderImg, merchant.getCoverImg());
        merchantNameTv.setText(merchant.getMerchName());
        merchantScoreRb.setRating(merchant.getScore());
        String text = MessageFormat.format(getString(R.string.shop_address), merchant.getAddress());
        addressTv.setText(text);
        text = MessageFormat.format(getString(R.string.contact_num), merchant.getContactMobileNO());
        tellTv.setText(text);
        merchantDescTv.setText(merchant.getMerchantDetails());

        if (merchant.getBusLicePhoto() != null) {
            ImageLoader.disPlayImage(bussPhotoIv, merchant.getBusLicePhoto());
            bussPhotoIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ArrayList<ShowImageInfo> imageInfos = new ArrayList<>();
                    ShowImageInfo imageInfo = new ShowImageInfo(merchant.getBusLicePhoto().getUrl(), "");
                    imageInfo.setShowDesc(false);
                    imageInfos.add(imageInfo);

                    Intent intent = new Intent(v.getContext(), ShowBigImageActivity.class);
                    intent.putExtra(ShowBigImageActivity.IMAGE_LIST_KEY, imageInfos);
                    v.getContext().startActivity(intent);
                }
            });
        }

    }

    private void loadShopShow(String merchantId) {
        new ShopShowModel().getMerchantShopShows(new ArrayCallback<ShopShow>() {
            @Override
            public void onArray(List<ShopShow> datas) {
                initShopShows(datas);
            }
        }, merchantId);
    }

    private void initShopShows(List<ShopShow> datas) {
        shopShowRv.setAdapter(new ShopShowAdapter(datas));
    }
}
