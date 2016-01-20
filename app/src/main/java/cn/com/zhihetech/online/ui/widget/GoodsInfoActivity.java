package cn.com.zhihetech.online.ui.widget;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.Goods;
import cn.com.zhihetech.online.bean.GoodsDetail;
import cn.com.zhihetech.online.core.common.ResponseMessage;
import cn.com.zhihetech.online.core.common.ResponseStateCode;
import cn.com.zhihetech.online.core.http.ArrayCallback;
import cn.com.zhihetech.online.core.http.ObjectCallback;
import cn.com.zhihetech.online.core.http.ResponseMessageCallback;
import cn.com.zhihetech.online.core.view.GoodsInfoHeaderView;
import cn.com.zhihetech.online.core.view.ZhiheApplication;
import cn.com.zhihetech.online.core.view.adapter.GoodsDetailAdapter;
import cn.com.zhihetech.online.model.FocusGoodsModel;
import cn.com.zhihetech.online.model.GoodsDetailModel;
import cn.com.zhihetech.online.model.GoodsModel;

/**
 * Created by ShenYunjie on 2016/1/19.
 */
@ContentView(R.layout.activity_goods_info)
public class GoodsInfoActivity extends BaseActivity {
    public final static String GOODS_ID_KEY = "_goods_id";

    @ViewInject(R.id.goods_info_lv)
    private ListView goodsInfoFrv;
    @ViewInject(R.id.goods_info_service_view)
    private View serviceView;
    @ViewInject(R.id.goods_info_shop_view)
    private View entenrShopView;
    @ViewInject(R.id.goods_info_collection_view)
    private View collectionView;
    @ViewInject(R.id.goods_info_focus_msg_tv)
    private TextView focusMsgTv;
    @ViewInject(R.id.goods_info_shopping_cart_view)
    private View shoppingCartView;
    @ViewInject(R.id.goods_info_buy_view)
    private View buyView;

    private String goodsId;
    private Goods goods;
    private GoodsInfoHeaderView headerView;

    /**
     * 获取商家详情图回调
     */
    private ArrayCallback<GoodsDetail> detailCallback = new ArrayCallback<GoodsDetail>() {
        @Override
        public void onArray(List<GoodsDetail> datas) {
            goodsInfoFrv.setAdapter(new GoodsDetailAdapter(datas));
        }
    };

    /**
     * 获取商品回调
     */
    private ResponseMessageCallback<Goods> goodsCallback = new ResponseMessageCallback<Goods>() {
        @Override
        public void onResponseMessage(ResponseMessage<Goods> responseMessage) {
            if (responseMessage.getCode() == 200) {
                goods = responseMessage.getData();
                bindHeaderViewGoods();
            }
        }
    };

    /**
     * 检查收藏状态回调
     */
    private ObjectCallback<ResponseMessage> checkFocusGoodsCallback = new ObjectCallback<ResponseMessage>() {
        @Override
        public void onObject(ResponseMessage data) {
            if(data.getCode() == ResponseStateCode.FOCUSED_GOODS){
                settingFocusState(false);
            }
            if(data.getCode() == ResponseStateCode.UN_FOCUS_GOODS){
                settingFocusState(true);
            }
        }
    };

    /**
     * 收藏商品回调
     */
    private ObjectCallback<ResponseMessage> focusCallback = new ObjectCallback<ResponseMessage>() {
        @Override
        public void onObject(ResponseMessage data) {
            if (data.getCode() == ResponseStateCode.SUCCESS) {
                showMsg(collectionView, "收藏成功");
                settingFocusState(false);
            }
        }

        @Override
        public void onFinished() {
            collectionView.setClickable(true);
        }
    };

    /**
     * 取消收藏商品回调
     */
    private ObjectCallback<ResponseMessage> unFocusCallback = new ObjectCallback<ResponseMessage>() {
        @Override
        public void onObject(ResponseMessage data) {
            if (data.getCode() == ResponseStateCode.SUCCESS) {
                settingFocusState(true);
            }
        }

        @Override
        public void onFinished() {
            collectionView.setClickable(true);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        goodsId = getIntent().getStringExtra(GOODS_ID_KEY);
        initViews();
        initData();
    }

    private void initData() {
        new GoodsModel().getGoodsByGoodsId(goodsCallback, goodsId);
        new GoodsDetailModel().getGoodsDetailByGoodsId(detailCallback, goodsId);
    }

    private void initViews() {
        headerView = new GoodsInfoHeaderView(this, goodsId);
        goodsInfoFrv.addHeaderView(headerView);
        initCollectionView();
    }

    /**
     * 初始化收藏按钮
     */
    private void initCollectionView() {
        new FocusGoodsModel().checkFocusSate(checkFocusGoodsCallback, goodsId, ZhiheApplication.getInstandce().getUserId());
    }

    /**
     * 设置收藏控件状态
     *
     * @param isFocus true:点击为收藏商品；false:点击为取消收藏状态
     */
    private void settingFocusState(boolean isFocus) {
        if (!isFocus) {
            focusMsgTv.setText(R.string.un_collection);
            collectionView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setClickable(false);
                    unFocusGoods();
                }
            });
            return;
        }
        focusMsgTv.setText(R.string.collection);
        collectionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setClickable(false);
                focusGoods();
            }
        });
    }

    /**
     * 绑定商品信息头部的商品基础信息
     */
    private void bindHeaderViewGoods() {
        headerView.bindGoodsData(goods);
    }

    /**
     * 收藏商品
     */
    private void focusGoods() {
        new FocusGoodsModel().focusGoods(focusCallback, goodsId, ZhiheApplication.getInstandce().getUserId());
    }

    /**
     * 取消收藏商品
     */
    private void unFocusGoods() {
        new FocusGoodsModel().unFocusGoods(unFocusCallback, goodsId, ZhiheApplication.getInstandce().getUserId());
    }

    @Event({R.id.goods_info_shop_view, R.id.goods_info_shopping_cart_view})
    private void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.goods_info_shop_view:
                if (goods != null) {
                    Intent intent = new Intent(this, MerchantHomeActivity.class);
                    intent.putExtra(MerchantHomeActivity.MERCHANT_ID_KEY, goods.getMerchant().getMerchantId());
                    startActivity(intent);
                }
                break;
            case R.id.goods_info_shopping_cart_view:

                break;
        }
    }
}
