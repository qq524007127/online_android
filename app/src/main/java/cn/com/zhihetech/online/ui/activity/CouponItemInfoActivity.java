package cn.com.zhihetech.online.ui.activity;

import android.app.ProgressDialog;
import android.app.admin.DeviceAdminInfo;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.TextView;

import com.easemob.easeui.widget.EaseImageView;

import org.w3c.dom.Text;
import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.Coupon;
import cn.com.zhihetech.online.bean.CouponItem;
import cn.com.zhihetech.online.core.common.Constant;
import cn.com.zhihetech.online.core.common.ResponseMessage;
import cn.com.zhihetech.online.core.common.ResponseStateCode;
import cn.com.zhihetech.online.core.http.ResponseMessageCallback;
import cn.com.zhihetech.online.core.util.ImageLoader;
import cn.com.zhihetech.online.core.util.StringUtils;
import cn.com.zhihetech.online.model.CouponItemModel;

/**
 * Created by ShenYunjie on 2016/3/17.
 */
@ContentView(R.layout.activity_coupon_item_info)
public class CouponItemInfoActivity extends BaseActivity {

    public final static String COUPON_ITEM_ID_KEY = "COUPON_ITEM_ID_KEY";

    @ViewInject(R.id.coupon_info_merchant_cover_iv)
    private EaseImageView merchantCoverImg;
    @ViewInject(R.id.coupon_info_merchant_name_tv)
    private TextView merchantNameTv;
    @ViewInject(R.id.coupon_item_face_value_tv)
    private TextView couponItemFaceValueTv;
    @ViewInject(R.id.coupon_info_type_tv)
    private TextView couponTypeTv;
    @ViewInject(R.id.coupon_info_code_tv)
    private TextView couponCodeTv;
    @ViewInject(R.id.coupon_item_validity_tv)
    private TextView viladityTv;
    @ViewInject(R.id.coupon_item_state_tv)
    private TextView couponStateTv;
    @ViewInject(R.id.coupon_item_detail_tv)
    private TextView couponDetailTv;

    private String couponItemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        couponItemId = getIntent().getStringExtra(COUPON_ITEM_ID_KEY);
        if (StringUtils.isEmpty(couponItemId)) {
            showMsg("出错了");
            finish();
            return;
        }
        initViewAndData(couponItemId);
    }

    private void initViewAndData(String couponItemId) {
        final ProgressDialog progressDialog = ProgressDialog.show(this, "", getString(R.string.data_loading));
        progressDialog.setCancelable(true);
        final Callback.Cancelable cancelable = new CouponItemModel().getCouponItemInfoById(new ResponseMessageCallback<CouponItem>() {
            @Override
            public void onResponseMessage(ResponseMessage<CouponItem> responseMessage) {
                if (responseMessage.getCode() != ResponseStateCode.SUCCESS) {
                    showMsg(responseMessage.getMsg());
                    finish();
                    return;
                }
                bindData(responseMessage.getData());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                showMsg("加载数据失败,请重试！");
                finish();
            }

            @Override
            public void onFinished() {
                progressDialog.dismiss();
            }
        }, couponItemId);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (!cancelable.isCancelled()) {
                    cancelable.cancel();
                    finish();
                }
            }
        });
    }

    private void bindData(CouponItem data) {
        Coupon coupon = data.getCoupon();
        ImageLoader.disPlayImage(merchantCoverImg, coupon.getMerchant().getCoverImg());
        merchantNameTv.setText(coupon.getMerchant().getMerchName());
        couponItemFaceValueTv.setText(coupon.getCouponType() == Constant.COUPON_DISCOUNT_TYPE ?
                coupon.getFaceValue() + "折" : "￥" + coupon.getFaceValue());
        couponTypeTv.setText(coupon.getCouponType() == Constant.COUPON_DISCOUNT_TYPE ?
                "打折券" : "优惠券");
        couponCodeTv.setText("券码:" + data.getCode());
        couponStateTv.setText(data.isUseState() ? "已使用" : "未使用");
        couponDetailTv.setText(coupon.getCouponMsg());
    }
}
