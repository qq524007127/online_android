package cn.com.zhihetech.online.core.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import cn.com.zhihetech.online.R;

/**
 * Created by ShenYunjie on 2016/1/25.
 */
public abstract class SheetBottomView extends PopupWindow {
    protected Context mContext;
    protected OnShowListener onShowListener;

    public SheetBottomView(Context context) {
        super(context);
        this.mContext = context;
        initParams();
    }

    private void initParams() {
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable());
        setAnimationStyle(R.style.PopWindowStyle);
    }

    @Override
    public void dismiss() {
        setActivityAlpha(1f);
        super.dismiss();
    }

    /**
     * 从底部弹出
     *
     * @param parentView
     */
    public void show(View parentView) {
        setActivityAlpha(0.5f);
        if (onShowListener != null) {
            onShowListener.onShow();
        }
        showAtLocation(parentView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    /**
     * 设置Activity的背景透明度
     *
     * @param bgAlpha
     */
    protected void setActivityAlpha(float bgAlpha) {
        if (!(mContext instanceof android.app.Activity)) {
            return;
        }
        android.app.Activity activity = activity = (android.app.Activity) mContext;
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        activity.getWindow().setAttributes(lp);
    }

    public void setOnShowListener(OnShowListener onShowListener) {
        this.onShowListener = onShowListener;
    }

    /**
     * SheetBottomView显示回调
     */
    public interface OnShowListener {
        void onShow();
    }
}
