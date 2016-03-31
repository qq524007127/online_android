package cn.com.zhihetech.online.core.adapter;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.com.zhihetech.online.bean.GoodsDetail;
import cn.com.zhihetech.online.bean.ImgInfo;
import cn.com.zhihetech.online.bean.ShopShow;
import cn.com.zhihetech.online.bean.ShowImageInfo;
import cn.com.zhihetech.online.core.util.ImageLoader;
import cn.com.zhihetech.online.core.view.SetWHImageView;
import cn.com.zhihetech.online.ui.activity.ShowBigImageActivity;

/**
 * Created by ShenYunjie on 2016/1/20.
 */
public class GoodsDetailAdapter extends BaseAdapter {
    private List<GoodsDetail> details;

    public GoodsDetailAdapter(List<GoodsDetail> details) {
        this.details = details;
    }

    @Override
    public int getCount() {
        return this.details == null ? 0 : details.size();
    }

    @Override
    public Object getItem(int position) {
        return details.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ImgInfo imgInfo = details.get(position).getImgInfo();
        SetWHImageView imageView;
        if (convertView == null) {
            convertView = new SetWHImageView(parent.getContext());
            convertView.setTag(convertView);
        }
        imageView = (SetWHImageView) convertView.getTag();
        if (imgInfo != null) {
            imageView.setResSize(imgInfo.getWidth(), imgInfo.getHeight());
        }
        ImageLoader.disPlayImage(imageView, imgInfo);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<ShowImageInfo> imageInfos = new ArrayList<>();
                for (GoodsDetail detail : details) {
                    ShowImageInfo imageInfo = new ShowImageInfo(detail.getImgInfo().getUrl(), "");
                    imageInfo.setShowDesc(false);
                    imageInfos.add(imageInfo);
                }
                Intent intent = new Intent(v.getContext(), ShowBigImageActivity.class);
                intent.putExtra(ShowBigImageActivity.IMAGE_LIST_KEY, imageInfos);
                intent.putExtra(ShowBigImageActivity.CURRENT_POSITION, position);
                v.getContext().startActivity(intent);
            }
        });
        return convertView;
    }


    /*public void onBindViewHolder(GoodsDetailHolder holder, int position) {
        ImgInfo imgInfo = goodsDetails.get(position).getImgInfo();
        if (imgInfo != null) {
            holder.detailImage.setResSize(imgInfo.getWidth(), imgInfo.getHeight());
        }
        ImageLoader.disPlayImage(holder.detailImage, goodsDetails.get(position).getImgInfo());
        holder.detailImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }*/
}
