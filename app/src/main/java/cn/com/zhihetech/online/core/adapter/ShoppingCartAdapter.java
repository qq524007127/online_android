package cn.com.zhihetech.online.core.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.xutils.view.annotation.ViewInject;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.ShoppingCart;
import cn.com.zhihetech.online.core.util.ImageLoader;
import cn.com.zhihetech.online.ui.widget.GoodsInfoActivity;

/**
 * Created by ShenYunjie on 2016/1/22.
 */
public class ShoppingCartAdapter extends ZhiheAdapter<ShoppingCart, ShoppingCartAdapter.ShoppingCartHolder> {

    private List<ShoppingCart> checkedCarts = new ArrayList<>();

    private OnShoppingCatrAmountChangeListener onShoppingCatrAmountChangeListener;

    public ShoppingCartAdapter(Context mContext, int layoutId) {
        super(mContext, layoutId);
    }

    public ShoppingCartAdapter(Context mContext, int layoutId, List<ShoppingCart> mDatas) {
        super(mContext, layoutId, mDatas);
    }

    @Override
    public ShoppingCartHolder onCreateViewHolder(View itemView) {
        return new ShoppingCartHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ShoppingCartHolder holder, final ShoppingCart data) {
        holder.goodsNameTv.setText(data.getGoods().getGoodsName());
        ImageLoader.disPlayImage(holder.coverImg, data.getGoods().getCoverImg());
        holder.numberBtn.setText(String.valueOf(data.getAmount()));
        holder.numberBtn.setClickable(false);
        holder.priceTv.setText(MessageFormat.format(mContext.getString(R.string.goods_price), data.getGoods().getPrice()));
        if (checkedCarts.contains(data)) {
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setChecked(false);
        }
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkedCarts.add(data);
                } else {
                    checkedCarts.remove(data);
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, GoodsInfoActivity.class);
                intent.putExtra(GoodsInfoActivity.GOODS_ID_KEY, data.getGoods().getGoodsId());
                intent.putExtra(GoodsInfoActivity.GOODS_NAME_KEY, data.getGoods().getGoodsName());
                mContext.startActivity(intent);
            }
        });

        holder.reduceIb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ShoppingCartAdapter", holder.numberBtn.getText().toString());
                int amount = Integer.parseInt(holder.numberBtn.getText().toString());
                if (amount > 1 && onShoppingCatrAmountChangeListener != null) {
                    amount--;
                    //holder.numberBtn.setText(String.valueOf(amount));
                    onShoppingCatrAmountChangeListener.onAmountCanged(data, amount);
                }
            }
        });

        holder.addIb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ShoppingCartAdapter", holder.numberBtn.getText().toString());
                int amount = Integer.parseInt(holder.numberBtn.getText().toString());
                if (amount < data.getGoods().getCurrentStock() && onShoppingCatrAmountChangeListener != null) {
                    amount++;
                    //holder.numberBtn.setText(String.valueOf(amount));
                    onShoppingCatrAmountChangeListener.onAmountCanged(data, amount);
                }
            }
        });
    }

    @Override
    public void refreshData(List<ShoppingCart> datas) {
        this.checkedCarts.clear();
        super.refreshData(datas);
    }

    public List<ShoppingCart> getCheckedCarts() {
        return checkedCarts;
    }

    public void setOnShoppingCatrAmountChangeListener(OnShoppingCatrAmountChangeListener onShoppingCatrAmountChangeListener) {
        this.onShoppingCatrAmountChangeListener = onShoppingCatrAmountChangeListener;
    }

    public class ShoppingCartHolder extends ZhiheAdapter.BaseViewHolder {
        @ViewInject(R.id.shopping_cart_accb)
        public AppCompatCheckBox checkBox;
        @ViewInject(R.id.shopping_cart_cover_iv)
        public ImageView coverImg;
        @ViewInject(R.id.shopping_cart_goods_name_tv)
        public TextView goodsNameTv;
        @ViewInject(R.id.shopping_cart_reduce_ib)
        public ImageButton reduceIb;
        @ViewInject(R.id.shopping_cart_number_et)
        public Button numberBtn;
        @ViewInject(R.id.shopping_cart_add_ib)
        public ImageButton addIb;
        @ViewInject(R.id.shopping_cart_goods_price_tv)
        public TextView priceTv;

        public ShoppingCartHolder(View itemView) {
            super(itemView);
        }
    }

    public interface OnShoppingCatrAmountChangeListener {
        void onAmountCanged(ShoppingCart data, int amount);
    }
}
