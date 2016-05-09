package cn.com.zhihetech.online.ui.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.core.util.ImageLoader;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by ShenYunjie on 2016/3/31.
 */
@ContentView(R.layout.content_big_imageinfo_fragment)
public class BigImageFragment extends BaseFragment {

    public final static String IMAGE_URL = "image_url";
    public final static String IMAGE_DESC = "image_desc";
    public final static String SHOW_IMAGE_DESC = "show_image_desc";

    @ViewInject(R.id.big_image_desc_tv)
    private TextView imageDesc;

    @ViewInject(R.id.big_photo_view)
    private PhotoView bigPhotoView;

    private PhotoViewAttacher mAttacher;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle args = getArguments();
        if (args == null) {
            return;
        }
        imageDesc.setVisibility(args.getBoolean(SHOW_IMAGE_DESC, false) ? View.VISIBLE : View.GONE);
        if (imageDesc.getVisibility() == View.VISIBLE && args.getString(IMAGE_DESC) != null) {
            imageDesc.setText(args.getString(IMAGE_DESC));
        }

        if (args.getString(IMAGE_URL) != null) {
            ImageLoader.disPlayImage(bigPhotoView, args.getString(IMAGE_URL), new ImageLoader.OnBindImageCallback() {
                @Override
                public void onBinded(Drawable result) {
                    mAttacher = new PhotoViewAttacher(bigPhotoView);
                    mAttacher.update();
                    result.setCallback(null);
                }

                @Override
                public void onError() {

                }
            });
        }
    }
}
