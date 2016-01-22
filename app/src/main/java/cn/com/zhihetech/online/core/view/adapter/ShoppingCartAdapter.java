package cn.com.zhihetech.online.core.view.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.xutils.view.annotation.ViewInject;

import java.text.MessageFormat;
import java.util.List;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.ShoppingCart;
import cn.com.zhihetech.online.core.adapter.ZhiheAdapter;
import cn.com.zhihetech.online.core.util.ImageLoader;

/**
 * Created by ShenYunjie on 2016/1/22.
 */
public class ShoppingCartAdapter extends ZhiheAdapter<ShoppingCart, ShoppingCartAdapter.ShoppingCartHolder> {

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
    public void onBindViewHolder(ShoppingCartHolder holder, ShoppingCart data) {
        holder.goodsNameTv.setText(data.getGoods().getGoodsName());
        ImageLoader.disPlayImage(holder.coverImg, data.getGoods().getCoverImg());
        holder.numberEt.setText(String.valueOf(data.getCount()));
        holder.priceTv.setText(MessageFormat.format(mContext.getString(R.string.goods_price), data.getGoods().getPrice()));
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
        public EditText numberEt;
        @ViewInject(R.id.shopping_cart_add_ib)
        public ImageButton addIb;
        @ViewInject(R.id.shopping_cart_goods_price_tv)
        public TextView priceTv;

        public ShoppingCartHolder(View itemView) {
            super(itemView);
        }
    }
}
