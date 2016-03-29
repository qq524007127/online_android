package cn.com.zhihetech.online.core.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.xutils.view.annotation.ViewInject;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.ShoppingCart;
import cn.com.zhihetech.online.core.util.ImageLoader;
import cn.com.zhihetech.online.ui.activity.GoodsInfoActivity;

/**
 * Created by ShenYunjie on 2016/1/22.
 */
public class ShoppingCartAdapter extends ZhiheAdapter<ShoppingCart, ShoppingCartAdapter.ShoppingCartHolder> {

    private List<ShoppingCart> checkedCarts = new ArrayList<>();

    private OnShoppingCartAmountChangeListener onShoppingCartAmountChangeListener;
    private OnCheckedShoppingCartsChangedListener onCheckedShoppingCartsChangedListener;

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

        /*List<ShoppingCart> _tmp = new ArrayList<>();
        for (ShoppingCart cart : checkedCarts) {
            for (ShoppingCart _cart : mDatas) {
                if (cart.getShoppingCartId().equals(_cart.getShoppingCartId())) {
                    _tmp.add(_cart);
                    break;
                }
            }
        }
        checkedCarts.clear();
        checkedCarts.addAll(_tmp);*/

        if (checkedCarts.contains(data)) {
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setChecked(false);
        }
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.checkBox.isChecked()) {
                    if (!checkedCarts.contains(data)) {
                        checkedCarts.add(data);
                    }
                } else {
                    if (checkedCarts.contains(data)) {
                        checkedCarts.remove(data);
                    }
                }
                if (onCheckedShoppingCartsChangedListener != null) {
                    onCheckedShoppingCartsChangedListener.onCheckedCartsChange(checkedCarts);
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
                int amount = Integer.parseInt(holder.numberBtn.getText().toString());
                if (amount == 1) {
                    Toast.makeText(mContext, "受不了了，宝贝不能再减少了！", Toast.LENGTH_SHORT).show();
                }
                if (amount > 1 && onShoppingCartAmountChangeListener != null) {
                    amount--;
                    onShoppingCartAmountChangeListener.onAmountChanged(data, amount);
                }
            }
        });

        holder.addIb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ShoppingCartAdapter", holder.numberBtn.getText().toString());
                int amount = Integer.parseInt(holder.numberBtn.getText().toString());
                if (amount >= data.getGoods().getCurrentStock()) {
                    Toast.makeText(mContext, "受不了了，宝贝不能再增加了！", Toast.LENGTH_SHORT).show();
                }
                if (amount < data.getGoods().getCurrentStock() && onShoppingCartAmountChangeListener != null) {
                    amount++;
                    //holder.numberBtn.setText(String.valueOf(amount));
                    onShoppingCartAmountChangeListener.onAmountChanged(data, amount);
                }
            }
        });
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        List<ShoppingCart> carts = new ArrayList<>();
        for (ShoppingCart cart : checkedCarts) {
            for (ShoppingCart data : mDatas) {
                if (cart.getShoppingCartId().equals(data.getShoppingCartId())) {
                    carts.add(data);
                    break;
                }
            }
        }

        checkedCarts.clear();
        checkedCarts.addAll(carts);

        if (onCheckedShoppingCartsChangedListener != null) {
            onCheckedShoppingCartsChangedListener.onCheckedCartsChange(checkedCarts);
        }
    }

    @Override
    public void refreshData(List<ShoppingCart> datas) {
        this.checkedCarts.clear();
        super.refreshData(datas);
    }

    /**
     * 全选或取消全选
     *
     * @param allChecked true:全选；false:取消全选
     */
    public void toggleAllChecked(boolean allChecked) {
        checkedCarts.clear();
        if (allChecked) {
            checkedCarts.addAll(mDatas);
        }
        notifyDataSetChanged();
        if (onCheckedShoppingCartsChangedListener != null) {
            onCheckedShoppingCartsChangedListener.onCheckedCartsChange(checkedCarts);
        }
    }

    public List<ShoppingCart> getCheckedCarts() {
        return checkedCarts;
    }

    public void setOnShoppingCartAmountChangeListener(OnShoppingCartAmountChangeListener onShoppingCartAmountChangeListener) {
        this.onShoppingCartAmountChangeListener = onShoppingCartAmountChangeListener;
    }

    public void setOnCheckedShoppingCartsChangedListener(OnCheckedShoppingCartsChangedListener onCheckedShoppingCartsChangedListener) {
        this.onCheckedShoppingCartsChangedListener = onCheckedShoppingCartsChangedListener;
    }

    public class ShoppingCartHolder extends ZhiheAdapter.BaseViewHolder {
        @ViewInject(R.id.shopping_cart_accb)
        public CheckBox checkBox;
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

    public interface OnShoppingCartAmountChangeListener {
        void onAmountChanged(ShoppingCart data, int amount);
    }

    public interface OnCheckedShoppingCartsChangedListener {
        void onCheckedCartsChange(List<ShoppingCart> checkedCarts);
    }
}
