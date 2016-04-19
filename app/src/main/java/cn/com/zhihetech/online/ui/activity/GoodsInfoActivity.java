package cn.com.zhihetech.online.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.easemob.easeui.EaseConstant;

import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.EMUserInfo;
import cn.com.zhihetech.online.bean.Goods;
import cn.com.zhihetech.online.bean.GoodsDetail;
import cn.com.zhihetech.online.bean.Merchant;
import cn.com.zhihetech.online.bean.OrderDetail;
import cn.com.zhihetech.online.core.common.Constant;
import cn.com.zhihetech.online.core.common.ResponseMessage;
import cn.com.zhihetech.online.core.common.ResponseStateCode;
import cn.com.zhihetech.online.core.db.DBUtils;
import cn.com.zhihetech.online.core.eventmsg.ShoppingCartMessageEvent;
import cn.com.zhihetech.online.core.http.ArrayCallback;
import cn.com.zhihetech.online.core.http.ObjectCallback;
import cn.com.zhihetech.online.core.http.ResponseMessageCallback;
import cn.com.zhihetech.online.core.util.StringUtils;
import cn.com.zhihetech.online.core.view.GoodsInfoHeaderView;
import cn.com.zhihetech.online.core.adapter.GoodsDetailAdapter;
import cn.com.zhihetech.online.core.view.GoodsCartOrBuySheetBottomView;
import cn.com.zhihetech.online.model.FocusGoodsModel;
import cn.com.zhihetech.online.model.GoodsBrowseModel;
import cn.com.zhihetech.online.model.GoodsDetailModel;
import cn.com.zhihetech.online.model.GoodsModel;
import cn.com.zhihetech.online.model.ShoppingCartModel;
import de.greenrobot.event.EventBus;

/**
 * Created by ShenYunjie on 2016/1/19.
 */
@ContentView(R.layout.activity_goods_info)
public class GoodsInfoActivity extends BaseActivity {

    public final static String GOODS_ID_KEY = "_goods_id";
    public final static String GOODS_NAME_KEY = "_goods_name";

    @ViewInject(R.id.goods_info_root_ll)
    private View rootContent;
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
    @ViewInject(R.id.goods_info_bottom_layout_view)
    private View bottomLayout;

    private GoodsCartOrBuySheetBottomView goodsSheetBottomView;
    private ProgressDialog progressDialog;

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
            } else {
                showMsg(responseMessage.getMsg());
                finish();
            }
        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            showMsg("出错了！");
            finish();
        }
    };

    /**
     * 检查收藏状态回调
     */
    private ObjectCallback<ResponseMessage> checkFocusGoodsCallback = new ObjectCallback<ResponseMessage>() {
        @Override
        public void onObject(ResponseMessage data) {
            if (data.getCode() == ResponseStateCode.FOCUSED_GOODS) {
                settingFocusState(false);
            }
            if (data.getCode() == ResponseStateCode.UN_FOCUS_GOODS) {
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

    /**
     * 添加商品到购物车回调
     */
    private ObjectCallback<ResponseMessage> addShopCartCallback = new ObjectCallback<ResponseMessage>() {
        @Override
        public void onObject(ResponseMessage data) {
            if (data.getCode() == ResponseStateCode.SUCCESS) {
                showMsg(shoppingCartView, "添加成功");
                EventBus.getDefault().post(new ShoppingCartMessageEvent());
            }
        }

        @Override
        public void onFinished() {
            progressDialog.dismiss();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        goodsId = getIntent().getStringExtra(GOODS_ID_KEY);
        if(StringUtils.isEmpty(goodsId)){
            showMsg("出错了！");
            finish();
            return;
        }
        initViews();
        initData();
    }

    @Override
    protected CharSequence getToolbarTile() {
        String title = getIntent().getStringExtra(GOODS_NAME_KEY);
        if (!StringUtils.isEmpty(title)) {
            return title;
        }
        return super.getToolbarTile();
    }

    private void initData() {
        new GoodsModel().getGoodsByGoodsId(goodsCallback, goodsId);
        new GoodsDetailModel().getGoodsDetailByGoodsId(detailCallback, goodsId);

        new GoodsBrowseModel().addGoodsBrowse(null, getUserId(), this.goodsId);   //添加商品浏览记录
    }

    private void initViews() {
        initProgressDialog();
        headerView = new GoodsInfoHeaderView(this, goodsId);
        goodsInfoFrv.addHeaderView(headerView);
        goodsSheetBottomView = new GoodsCartOrBuySheetBottomView(this);
        initCollectionView();
    }

    private void initProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.please_wait));
    }

    /**
     * 初始化收藏按钮
     */
    private void initCollectionView() {
        new FocusGoodsModel().checkFocusSate(checkFocusGoodsCallback, goodsId, getUserId());
    }

    /**
     * 设置收藏控件状态
     *
     * @param isFocus true:点击为收藏商品；false:点击为取消收藏状态
     */
    private void settingFocusState(boolean isFocus) {
        if (!isFocus) {
            focusMsgTv.setText(R.string.collectioned);
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
        new FocusGoodsModel().focusGoods(focusCallback, goodsId, getUserId());
    }

    /**
     * 取消收藏商品
     */
    private void unFocusGoods() {
        new FocusGoodsModel().unFocusGoods(unFocusCallback, goodsId, getUserId());
    }

    @Event({R.id.goods_info_shop_view, R.id.goods_info_shopping_cart_view, R.id.goods_info_buy_view, R.id.goods_info_service_view})
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
                if (goods == null) {
                    //showMsg(view, "商品数据还未加载完成，请稍后再试");
                    return;
                }
                goodsSheetBottomView.setOkText("加入购物车");
                goodsSheetBottomView.setOnOkListener(new GoodsCartOrBuySheetBottomView.OnOkListener() {
                    @Override
                    public void onOk(Goods goods, int amount) {
                        goodsSheetBottomView.dismiss();
                        addGoodsToShoppingCart(goods.getGoodsId(), amount);
                    }
                });
                goodsSheetBottomView.showWhithGoods(rootContent, goods);
                break;
            case R.id.goods_info_buy_view:
                if (goods == null) {
                    //showMsg(view, "商品数据还未加载完成，请稍后再试");
                    return;
                }
                goodsSheetBottomView.setOkText("确认购买");
                goodsSheetBottomView.setOnOkListener(new GoodsCartOrBuySheetBottomView.OnOkListener() {
                    @Override
                    public void onOk(Goods goods, int amount) {
                        Intent intent = new Intent(getSelf(), OrderConfirmActivity.class);
                        ArrayList<OrderDetail> details = new ArrayList<OrderDetail>();
                        details.add(new OrderDetail(goods, amount));
                        intent.putExtra(OrderConfirmActivity.ORDER_DETAILS_KEY, details);
                        startActivity(intent);
                        goodsSheetBottomView.dismiss();
                    }
                });
                goodsSheetBottomView.showWhithGoods(rootContent, goods);
                break;
            case R.id.goods_info_service_view:
                if (this.goods == null) {
                    return;
                }
                saveMerchantInfo(this.goods.getMerchant());
                Intent intent = new Intent(this, SingleChatActivity.class);
                intent.putExtra(EaseConstant.EXTRA_USER_ID, goods.getMerchant().getEMUserId());
                startActivity(intent);
                break;
        }
    }

    /**
     * 将商家的环信用户基本信息保存到本地数据库
     *
     * @param merchant
     * @throws DbException
     */
    private void saveMerchantInfo(Merchant merchant) {
        EMUserInfo userInfo = new EMUserInfo(merchant.getEMUserId(), merchant.getMerchName(),
                merchant.getCoverImg().getUrl(), merchant.getMerchantId(), Constant.EXTEND_MERCHANT_USER);
        try {
            new DBUtils().saveUserInfo(userInfo);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加当前商品到购物车
     */
    private void addGoodsToShoppingCart(String goodsId, int amount) {
        progressDialog.show();
        new ShoppingCartModel().addShoppingCart(addShopCartCallback, goodsId, getUserId(), amount);
    }
}
