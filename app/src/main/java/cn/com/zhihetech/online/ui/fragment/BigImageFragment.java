package cn.com.zhihetech.online.ui.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.easemob.easeui.widget.photoview.EasePhotoView;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import cn.com.zhihetech.online.R;

/**
 * Created by ShenYunjie on 2016/3/31.
 */
@ContentView(R.layout.content_big_imageinfo_fragment)
public class BigImageFragment extends BaseFragment {

    public final static String IMAGE_URL = "image_url";
    public final static String IMAGE_DESC = "image_desc";
    public final static String SHOW_IMAGE_DESC = "show_image_desc";

    @ViewInject(R.id.big_image_pv)
    private SubsamplingScaleImageView photoView;
    @ViewInject(R.id.big_image_desc_tv)
    private TextView imageDesc;

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

        photoView.setMaxScale(2);

        if (args.getString(IMAGE_URL) != null) {
            x.image().loadDrawable(args.getString(IMAGE_URL), null, new Callback.CommonCallback<Drawable>() {
                @Override
                public void onSuccess(Drawable result) {
                    BitmapDrawable bd = (BitmapDrawable) result;
                    Bitmap bitmap = bd.getBitmap();
                    photoView.setImage(ImageSource.bitmap(bitmap));
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {

                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {

                }
            });
        }
    }
}
