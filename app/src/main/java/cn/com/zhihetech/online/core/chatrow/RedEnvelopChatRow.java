package cn.com.zhihetech.online.core.chatrow;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMMessage;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.RedEnvelopItem;
import cn.com.zhihetech.online.core.ZhiheApplication;
import cn.com.zhihetech.online.core.common.ResponseMessage;
import cn.com.zhihetech.online.core.common.ResponseStateCode;
import cn.com.zhihetech.online.core.http.ResponseMessageCallback;
import cn.com.zhihetech.online.model.RedEnvelopModel;
import cn.com.zhihetech.online.ui.activity.RedEnvelopItemInfoActivity;

/**
 * Created by ShenYunjie on 2016/3/3.
 */
public class RedEnvelopChatRow extends BaseChatRow {

    protected TextView redEnvelopMsgTv;
    protected ProgressDialog progressDialog;

    public RedEnvelopChatRow(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

    @Override
    protected void onInflatView() {
        inflater.inflate(message.direct == EMMessage.Direct.RECEIVE ?
                R.layout.chat_row_received_red_envelop : R.layout.chat_row_send_red_envelop, this);
    }

    @Override
    protected void onFindViewById() {
        redEnvelopMsgTv = (TextView) findViewById(R.id.chat_row_envelop_msg_tv);
    }

    @Override
    public void onSetUpView() {
        this.redEnvelopMsgTv.setText(jsonObject.getString("envelopMsg"));
        handleTextMessage();
    }

    @Override
    protected void onBubbleClick() {
        switch (getUserType()) {
            case ZhiheApplication.MERCHANT_USER_TYPE:
                return;
            default:
                gradEnvelop(ZhiheApplication.getInstance().getUserId(), jsonObject.getString("redEnvelopId"));
        }
    }

    /**
     * 抢红包回调
     */
    private ResponseMessageCallback<RedEnvelopItem> callback = new ResponseMessageCallback<RedEnvelopItem>() {
        @Override
        public void onResponseMessage(final ResponseMessage<RedEnvelopItem> responseMessage) {
            int code = responseMessage.getCode();
            switch (code) {
                case ResponseStateCode.SUCCESS:
                    new AlertDialog.Builder(getContext())
                            .setMessage("恭喜你，抢到红包了")
                            .setPositiveButton("查看", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(getContext(), RedEnvelopItemInfoActivity.class);
                                    intent.putExtra(RedEnvelopItemInfoActivity.RED_ENVELOP_ITEM_ID, responseMessage.getData().getEnvelopItemId());
                                    getContext().startActivity(intent);
                                }
                            })
                            .setNegativeButton("知道了", null)
                            .show();
                    break;
                case ResponseStateCode.RED_ENVELOP_FINISHED:
                    new AlertDialog.Builder(getContext())
                            .setTitle("提示")
                            .setMessage("红包已抢完！")
                            .setPositiveButton("知道了", null)
                            .show();
                    break;
                case ResponseStateCode.RED_ENVELOP_ALREADY_RECEIVED:
                    new AlertDialog.Builder(getContext())
                            .setTitle("提示")
                            .setMessage("你已领取过红包！")
                            .setPositiveButton("知道了", null)
                            .show();
                    break;
                default:
                    new AlertDialog.Builder(getContext())
                            .setTitle("提示")
                            .setMessage(responseMessage.getMsg())
                            .setPositiveButton("知道了", null)
                            .show();
            }
        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            Toast.makeText(getContext(), "抢红包失败，请重试", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFinished() {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    };

    /**
     * 抢红包
     *
     * @param userId    用户ID
     * @param envelopId 红包ID
     */
    protected void gradEnvelop(@NonNull String userId, @NonNull String envelopId) {
        new RedEnvelopModel().gradEnvelop(callback, userId, envelopId);
        progressDialog = ProgressDialog.show(getContext(), "", "正在抢红包...");
    }
}
