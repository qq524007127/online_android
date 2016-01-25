package cn.com.zhihetech.online.core.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.MessageFormat;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.Goods;
import cn.com.zhihetech.online.core.util.ImageLoader;

/**
 * Created by ShenYunjie on 2016/1/25.
 */
public class GoodsCartOrBuySheetBottomView extends SheetBottomView {
    @ViewInject(R.id.sheet_goods_cover_iv)
    private ImageView goodsCoverIv;
    @ViewInject(R.id.sheet_goods_name_tv)
    private TextView goodsNameTv;
    @ViewInject(R.id.sheet_goods_price_tv)
    private TextView goodsPriceTv;
    @ViewInject(R.id.sheet_goods_stock_tv)
    private TextView goodsStockTv;
    @ViewInject(R.id.sheet_goods_total_price_tv)
    private TextView totalPriceTv;
    @ViewInject(R.id.sheet_goods_total_btn)
    private Button goodsTotalBtn;
    @ViewInject(R.id.sheet_goods_ok_btn)
    private Button sheetOkBtn;

    private Goods goods;

    private OnOkListener onOkListener;

    public GoodsCartOrBuySheetBottomView(Context context) {
        super(context);
        initContentView();
    }

    private void initContentView() {
        View contentView = LayoutInflater.from(this.mContext).inflate(R.layout.content_add_shopping_cart, null);
        x.view().inject(this, contentView);
        setContentView(contentView);
    }

    public void setOkText(String text) {
        sheetOkBtn.setText(text);
    }

    public void showWhithGoods(View parent, Goods goods) {
        this.goods = goods;
        bindData();
        show(parent);
    }

    private void bindData() {
        goodsTotalBtn.setText(String.valueOf(1));
        ImageLoader.disPlayImage(goodsCoverIv, goods.getCoverImg());
        this.goodsNameTv.setText(this.goods.getGoodsName());
        goodsPriceTv.setText(MessageFormat.format(mContext.getString(R.string.goods_price), goods.getPrice()));
        goodsStockTv.setText(MessageFormat.format(mContext.getString(R.string.goods_stock), goods.getCurrentStock()));
        totalPriceTv.setText(String.valueOf(goods.getPrice() * getGoodsCount()));
        sheetOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onOkListener != null) {
                    onOkListener.onOk(goods, getGoodsCount());
                }
            }
        });
    }

    protected int getGoodsCount() {
        int total = Integer.parseInt(this.goodsTotalBtn.getText().toString());
        return total;
    }

    protected void setGoodsCount(int amount) {
        goodsTotalBtn.setText(String.valueOf(amount));
        totalPriceTv.setText(String.valueOf(goods.getPrice() * amount));
    }

    @Event({R.id.sheet_goods_reduce_ib, R.id.sheet_goods_add_ib})
    private void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.sheet_goods_reduce_ib:
                reduceGoodsCount(1);
                break;
            case R.id.sheet_goods_add_ib:
                addGoodsCount(1);
                break;
        }
    }

    private void reduceGoodsCount(int i) {
        int count = getGoodsCount();
        if (count <= 1 || count <= i) {
            return;
        }
        count -= i;
        setGoodsCount(count);
    }

    private void addGoodsCount(int i) {
        int count = getGoodsCount();
        if (count >= goods.getCurrentStock() || (count + i) > goods.getCurrentStock()) {
            return;
        }
        count += i;
        setGoodsCount(count);
    }

    public void setOnOkListener(OnOkListener onOkListener) {
        this.onOkListener = onOkListener;
    }

    /**
     * 确定按钮回调
     */
    public interface OnOkListener {
        void onOk(Goods goods, int amount);
    }
}
