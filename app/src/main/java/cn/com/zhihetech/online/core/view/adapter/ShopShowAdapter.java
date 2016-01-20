package cn.com.zhihetech.online.core.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.ShopShow;
import cn.com.zhihetech.online.core.util.ImageLoader;

/**
 * Created by ShenYunjie on 2016/1/20.
 */
public class ShopShowAdapter extends RecyclerView.Adapter<ShopShowAdapter.ShopShowHolder> {

    List<ShopShow> shopShows;

    public ShopShowAdapter(List<ShopShow> shopShows) {
        this.shopShows = shopShows;
    }

    @Override
    public ShopShowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_shop_show_item, parent, false);
        return new ShopShowHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ShopShowHolder holder, int position) {
        ImageLoader.disPlayImage(holder.shopShowImg, shopShows.get(position).getImgInfo());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return this.shopShows == null ? 0 : shopShows.size();
    }

    public class ShopShowHolder extends RecyclerView.ViewHolder {
        @ViewInject(R.id.shop_show_iv)
        public ImageView shopShowImg;

        public ShopShowHolder(View itemView) {
            super(itemView);
            x.view().inject(this, itemView);
        }
    }
}
