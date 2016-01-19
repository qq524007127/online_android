package cn.com.zhihetech.online.core.util;

import android.support.annotation.NonNull;
import android.widget.ImageView;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.ImgInfo;

/**
 * Created by ShenYunjie on 2016/1/18.
 */
public class ImageLoader {
    public static void disPlayImage(@NonNull ImageView targetView, ImgInfo imgInfo) {
        if (imgInfo == null || StringUtils.isEmpty(imgInfo.getUrl())) {
            disPlayImage(targetView, "");
            return;
        }
        disPlayImage(targetView, imgInfo.getUrl());
    }

    public static void disPlayImage(ImageView targetView, String url) {
        x.image().bind(targetView, url, createImageOptions());
    }

    public static ImageOptions createImageOptions() {
        ImageOptions options = new ImageOptions.Builder()
                .setPlaceholderScaleType(ImageView.ScaleType.CENTER_CROP)
                .setFailureDrawableId(R.mipmap.ic_launcher).build();
        return options;
    }
}
