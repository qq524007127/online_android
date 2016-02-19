package cn.com.zhihetech.online.core.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import org.xutils.common.util.DensityUtil;
import org.xutils.view.annotation.ViewInject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.Goods;
import cn.com.zhihetech.online.bean.OrderDetail;
import cn.com.zhihetech.online.core.util.ImageLoader;

/**
 * Created by ShenYunjie on 2016/2/19.
 */
public class EvaluateAdapter extends ZhiheAdapter<OrderDetail, EvaluateAdapter.EvaluateHolder> {

    private Map<String, Float> goodsScore = new HashMap<>();
    private Map<String, String> goodsEvaluate = new HashMap<>();

    private String touchedPosition;

    public EvaluateAdapter(Context mContext) {
        this(mContext, null);
    }

    public EvaluateAdapter(Context mContext, List<OrderDetail> mDatas) {
        this(mContext, R.layout.content_evaluate_item, mDatas);
    }

    public EvaluateAdapter(Context mContext, int layoutId, List<OrderDetail> mDatas) {
        super(mContext, layoutId, mDatas);
    }

    @Override
    public EvaluateHolder onCreateViewHolder(View itemView) {
        return new EvaluateHolder(itemView);
    }

    @Override
    public void onBindViewHolder(EvaluateHolder holder, final OrderDetail data) {
        ImageLoader.disPlayImage(holder.goodsCoverImg, data.getGoods().getCoverImg());
        holder.goodsNameTv.setText(data.getGoods().getGoodsName());

        //商品评分相关
        if (goodsScore.containsKey(data.getGoods().getGoodsId())) {
            holder.goodsScoreRb.setRating(goodsScore.get(data.getGoods().getGoodsId()));
        } else {
            holder.goodsScoreRb.setRating(5l);
        }
        holder.goodsScoreRb.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (fromUser) {
                    goodsScore.put(data.getGoods().getGoodsId(), rating);
                }
            }
        });
        goodsScore.put(data.getGoods().getGoodsId(), holder.goodsScoreRb.getRating());

        //商品评价相关
        if (goodsEvaluate.containsKey(data.getGoods().getGoodsId())) {
            holder.goodsEvaluate.setText(goodsEvaluate.get(data.getGoods().getGoodsId()));
        } else {
            holder.goodsEvaluate.setText("");
        }

        holder.goodsEvaluate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEvaluateDialog((Button) v, data.getGoods());
            }
        });
    }

    /**
     * 弹出评价对话框
     *
     * @param btn
     * @param goods
     */
    private void showEvaluateDialog(final Button btn, final Goods goods) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.content_evaluate_dialog_view, null);
        final EditText evaluateEt = (EditText) rootView.findViewById(R.id.evaluate_dialog_et);
        evaluateEt.setText(btn.getText());

        new AlertDialog.Builder(mContext)
                .setTitle("填写评价信息")
                .setView(rootView)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        btn.setText(evaluateEt.getText());
                        goodsEvaluate.put(goods.getGoodsId(), evaluateEt.getText().toString());
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
        evaluateEt.requestFocus();
    }

    /**
     * 解决ListView中EditView弹出输入键盘后失去焦点的问题
     *
     * @param holder
     * @param data
     */
    private void initGoodsEvaluateTouched(EvaluateHolder holder, final OrderDetail data) {
        holder.goodsEvaluate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    touchedPosition = data.getOrderDetailId();
                }
                return false;
            }
        });
        if (data.getOrderDetailId().equals(touchedPosition)) {
            holder.goodsEvaluate.requestFocus();
            if (holder.goodsEvaluate.getText() != null) {
                //holder.goodsEvaluate.setSelection(holder.goodsEvaluate.getText().length());
            }
        } else {
            holder.goodsEvaluate.clearFocus();
        }
    }

    /**
     * 获取商品评分
     *
     * @param position
     * @return
     */
    public float getGoodsScore(int position) {
        OrderDetail detail = getItem(position);
        return goodsScore.get(detail.getGoods().getGoodsId());
    }

    /**
     * 获取商品评价
     *
     * @param position
     * @return
     */
    public String getGoodsEvaluate(int position) {
        OrderDetail detail = getItem(position);
        return goodsEvaluate.get(detail.getGoods().getGoodsId());
    }

    public class EvaluateHolder extends ZhiheAdapter.BaseViewHolder {

        @ViewInject(R.id.evaluate_item_goods_cover_iv)
        public ImageView goodsCoverImg;
        @ViewInject(R.id.evaluate_item_goods_name_tv)
        public TextView goodsNameTv;
        @ViewInject(R.id.evaluate_item_goods_score_rb)
        public RatingBar goodsScoreRb;
        @ViewInject(R.id.evaluate_item_goods_msg_btn)
        public Button goodsEvaluate;

        public EvaluateHolder(View itemView) {
            super(itemView);
        }
    }
}
