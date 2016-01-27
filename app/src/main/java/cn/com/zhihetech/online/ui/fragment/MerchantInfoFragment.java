package cn.com.zhihetech.online.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.text.MessageFormat;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.Merchant;
import cn.com.zhihetech.online.core.http.ObjectCallback;
import cn.com.zhihetech.online.core.util.ImageLoader;
import cn.com.zhihetech.online.model.MerchantModel;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ShenYunjie on 2016/1/20.
 */
@ContentView(R.layout.content_merchant_info_fragment)
public class MerchantInfoFragment extends BaseFragment {
    @ViewInject(R.id.merchant_info_header_civ)
    private CircleImageView merchantHeaderImg;
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

    ObjectCallback<Merchant> merchantCallbak = new ObjectCallback<Merchant>() {
        @Override
        public void onObject(Merchant data) {
            merchant = data;
            bindViews();
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
    }

    private void initData() {
        new MerchantModel().getMerchantById(merchantCallbak, merchantId);
    }

    public void bindViews() {
        ImageLoader.disPlayImage(merchantHeaderImg, merchant.getCoverImg());
        merchantNameTv.setText(merchant.getMerchName());
        String text = MessageFormat.format(getString(R.string.shop_address), merchant.getAddress());
        addressTv.setText(text);
        text = MessageFormat.format(getString(R.string.contact_num), merchant.getContactMobileNO());
        tellTv.setText(text);
        ImageLoader.disPlayImage(bussPhotoIv, merchant.getBusLicePhoto());
        merchantDescTv.setText(merchant.getMerchantDetails());
    }
}