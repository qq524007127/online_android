package cn.com.zhihetech.online.ui.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.easemob.easeui.EaseConstant;

import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.ActivityGoods;
import cn.com.zhihetech.online.bean.EMUserInfo;
import cn.com.zhihetech.online.bean.Goods;
import cn.com.zhihetech.online.bean.GoodsDetail;
import cn.com.zhihetech.online.bean.Merchant;
import cn.com.zhihetech.online.core.adapter.GoodsDetailAdapter;
import cn.com.zhihetech.online.core.common.Constant;
import cn.com.zhihetech.online.core.common.ResponseMessage;
import cn.com.zhihetech.online.core.common.ResponseStateCode;
import cn.com.zhihetech.online.core.db.DBUtils;
import cn.com.zhihetech.online.core.http.ArrayCallback;
import cn.com.zhihetech.online.core.http.ResponseMessageCallback;
import cn.com.zhihetech.online.core.util.StringUtils;
import cn.com.zhihetech.online.core.view.SeckillGoodsInfoHeaderView;
import cn.com.zhihetech.online.model.ActivityGoodsModel;
import cn.com.zhihetech.online.model.GoodsDetailModel;

/**
 * Created by ShenYunjie on 2016/3/7.
 */
@ContentView(R.layout.activity_seckill_goods_info)
public class SeckillGoodsInfoActivity extends MerchantBaseActivity {

    public final static String SECKILL_GOODS_ID_KEY = "_SECKILL_GOODS_ID";
    public final static String GOODS_NAME_KEY = "_SECKILL_GOODS_NAME";

    @ViewInject(R.id.seckill_goods_info_lv)
    private ListView seckillGoodsDetailLv;
    @ViewInject(R.id.seckill_goods_info_service_view)
    private View constactMerchantView;
    @ViewInject(R.id.seckill_goods_info_shop_view)
    private View enterMerchantHomneView;
    @ViewInject(R.id.seckill_goods_info_buy_view)
    private TextView buySeckillGoodsTv;

    private SeckillGoodsInfoHeaderView goodsInfoHeaderView;

    private String seckillGoodsId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        seckillGoodsId = getIntent().getStringExtra(SECKILL_GOODS_ID_KEY);
        if (StringUtils.isEmpty(seckillGoodsId)) {
            showMsg("出错了！");
            finish();
            return;
        }
        loadSeckillGoods(this.seckillGoodsId);
    }

    @Override
    protected CharSequence getToolbarTile() {
        String title = getIntent().getStringExtra(GOODS_NAME_KEY);
        if (!StringUtils.isEmpty(title)) {
            this.toolbar.setSubtitle(title);
        }
        return "秒杀商品";
    }

    private void initSeckillGoodsHeaderInfo(ActivityGoods activityGoods) {
        goodsInfoHeaderView = new SeckillGoodsInfoHeaderView(this, activityGoods.getGoods().getGoodsId());
        goodsInfoHeaderView.bindActivityGoods(activityGoods);
        seckillGoodsDetailLv.addHeaderView(goodsInfoHeaderView);
    }

    /**
     * 获取秒杀商品
     *
     * @param seckillGoodsId
     */
    private void loadSeckillGoods(String seckillGoodsId) {
        final ProgressDialog progressDialog = ProgressDialog.show(this, "", getString(R.string.data_loading));
        progressDialog.setCancelable(true);
        final Callback.Cancelable cancelable = new ActivityGoodsModel().getActivityGoodsById(new ResponseMessageCallback<ActivityGoods>() {
            @Override
            public void onResponseMessage(ResponseMessage<ActivityGoods> responseMessage) {
                if (responseMessage.getCode() != ResponseStateCode.SUCCESS) {
                    showMsg(responseMessage.getMsg());
                    return;
                }
                ActivityGoods activityGoods = responseMessage.getData();
                initSeckillGoodsHeaderInfo(responseMessage.getData());
                setupView(activityGoods);
                loadGoodsDetail(activityGoods.getGoods());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                showMsg("数据加载失败，请重试！");
            }

            @Override
            public void onFinished() {
                progressDialog.dismiss();
            }
        }, this.seckillGoodsId);

        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (!cancelable.isCancelled()) {
                    cancelable.cancel();
                }
            }
        });
    }

    /**
     * 加载商品详情图
     *
     * @param goods
     */
    private void loadGoodsDetail(Goods goods) {
        new GoodsDetailModel().getGoodsDetailByGoodsId(new ArrayCallback<GoodsDetail>() {
            @Override
            public void onArray(List<GoodsDetail> datas) {
                seckillGoodsDetailLv.setAdapter(new GoodsDetailAdapter(datas));
            }
        }, goods.getGoodsId());
    }

    /**
     * 设置各控件的点击事件
     *
     * @param activityGoods
     */
    private void setupView(final ActivityGoods activityGoods) {
        this.enterMerchantHomneView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getSelf(), MerchantHomeActivity.class);
                intent.putExtra(MerchantHomeActivity.MERCHANT_ID_KEY, activityGoods.getMerchant().getMerchantId());
                startActivity(intent);
            }
        });
        this.constactMerchantView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMerchantInfo(activityGoods.getMerchant());
                Intent intent = new Intent(getSelf(), SingleChatActivity.class);
                intent.putExtra(EaseConstant.EXTRA_USER_ID, activityGoods.getMerchant().getEMUserId());
                startActivity(intent);
            }
        });
        this.buySeckillGoodsTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getSelf(), ActivityGoodsOrderActivity.class);
                intent.putExtra(ActivityGoodsOrderActivity.ACTIVITY_GOODS_KEY,activityGoods);
                startActivity(intent);
            }
        });
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
}
