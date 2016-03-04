package cn.com.zhihetech.online.ui.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.easemob.easeui.widget.EaseImageView;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.RedEnvelopItem;
import cn.com.zhihetech.online.core.common.ResponseMessage;
import cn.com.zhihetech.online.core.common.ResponseStateCode;
import cn.com.zhihetech.online.core.http.ObjectCallback;
import cn.com.zhihetech.online.core.http.ResponseMessageCallback;
import cn.com.zhihetech.online.core.util.ImageLoader;
import cn.com.zhihetech.online.core.util.StringUtils;
import cn.com.zhihetech.online.model.RedEnvelopItemModel;

/**
 * Created by ShenYunjie on 2016/3/3.
 */
@ContentView(R.layout.activity_red_envelop_item_detail)
public class RedEnvelopItemDetailActivity extends BaseActivity {

    public final static String RED_ENVELOP_ITEM_ID = "RED_ENVELOP_ITEM_ID";

    @ViewInject(R.id.envelop_item_detail_merchant_cover_iv)
    private EaseImageView merchantCoverIv;
    @ViewInject(R.id.envelop_item_detail_name_tv)
    private TextView envelopItemNameTv;
    @ViewInject(R.id.envelop_item_amount_money_tv)
    private TextView envelopItemAmoutTv;
    @ViewInject(R.id.envelop_item_save_state_tv)
    private TextView saveStateTv;
    @ViewInject(R.id.envelop_item_save_my_wallet_btn)
    private Button saveBtn;

    private ProgressDialog praProgressDialog;

    String envelopItemId;

    /**
     * 加载红包详情回调
     */
    private ResponseMessageCallback<RedEnvelopItem> loadCallback = new ResponseMessageCallback<RedEnvelopItem>() {
        @Override
        public void onResponseMessage(ResponseMessage<RedEnvelopItem> responseMessage) {
            if (responseMessage.getCode() != ResponseStateCode.SUCCESS) {
                showMsg(responseMessage.getMsg());
                return;
            }
            bindingData(responseMessage.getData());
        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            showMsg("数据加载失败");
        }

        @Override
        public void onFinished() {
            praProgressDialog.dismiss();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        envelopItemId = getIntent().getStringExtra(RED_ENVELOP_ITEM_ID);
        if (StringUtils.isEmpty(envelopItemId)) {
            showMsg("出错了");
            finish();
            return;
        }
        loadRedEnvelopItemDetail(envelopItemId);
    }

    @Event({R.id.envelop_item_save_my_wallet_btn})
    private void onVeiwClic(final View view) {
        final ProgressDialog progressDialog = ProgressDialog.show(this, "", getString(R.string.data_executing));
        final Callback.Cancelable cancelable = new RedEnvelopItemModel().extractRedEnvelopItem(new ObjectCallback<ResponseMessage>() {
            @Override
            public void onObject(ResponseMessage data) {
                int code = data.getCode();
                switch (code) {
                    case ResponseStateCode.SUCCESS:
                        showMsg(view, "操作成功！");
                        saveBtn.setVisibility(View.GONE);
                        saveStateTv.setVisibility(View.VISIBLE);
                        break;
                    default:
                        showMsg(view, data.getMsg());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                showMsg(view, "操作失败，请重试！");
            }

            @Override
            public void onFinished() {
                progressDialog.dismiss();
            }
        }, envelopItemId, getUserId());
        progressDialog.setCancelable(true);
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
     * 加载红包详情
     *
     * @param envelopItemId
     */
    private void loadRedEnvelopItemDetail(String envelopItemId) {
        praProgressDialog = ProgressDialog.show(this, "", getString(R.string.data_loading));
        praProgressDialog.setCancelable(true);
        final Callback.Cancelable cancelable = new RedEnvelopItemModel().getEnvelopItemByUserIdAndId(loadCallback, envelopItemId);
        praProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (!cancelable.isCancelled()) {
                    cancelable.cancel();
                    finish();
                }
            }
        });
    }

    /**
     * 将视图与数据绑定
     *
     * @param data
     */
    private void bindingData(RedEnvelopItem data) {
        ImageLoader.disPlayImage(merchantCoverIv, data.getRedEnvelop().getMerchant().getCoverImg());
        this.envelopItemNameTv.setText(data.getRedEnvelop().getMerchant().getMerchName() + "发送的红包");
        this.envelopItemAmoutTv.setText(data.getAmountOfMoney() + "元");
        if (data.isExtractState()) {
            this.saveStateTv.setVisibility(View.VISIBLE);
            this.saveBtn.setVisibility(View.GONE);
        } else {
            this.saveStateTv.setVisibility(View.GONE);
            this.saveBtn.setVisibility(View.VISIBLE);
        }
    }
}
