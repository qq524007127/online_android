package cn.com.zhihetech.online.core.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.xutils.view.annotation.ViewInject;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.Activity;
import cn.com.zhihetech.online.core.common.Constant;
import cn.com.zhihetech.online.core.util.DateUtils;
import cn.com.zhihetech.online.core.util.ImageLoader;
import cn.com.zhihetech.online.ui.activity.ActivityInfoActivity;

/**
 * Created by ShenYunjie on 2016/1/18.
 */
public class ActivityAdapter extends ZhiheAdapter<Activity, ActivityAdapter.ActivityHolder> {

    private OnActivityClickListener onActivityClickListener;

    public ActivityAdapter(Context mContext, int layoutId) {
        super(mContext, layoutId);
    }

    public ActivityAdapter(Context mContext, int layoutId, List<Activity> mDatas) {
        super(mContext, layoutId, mDatas);
    }

    @Override
    public ActivityHolder onCreateViewHolder(View itemView) {
        return new ActivityHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ActivityHolder holder, final Activity data) {
        //ImageLoader.disPlayImage(holder.coverImage, data.getCoverImg());
        ImageLoader.disPlayImage(holder.coverImage, data.getReceptionRoom().getCoverImg());
        holder.ingFlag.setVisibility(data.getCurrentState() == Constant.ACTIVITY_STATE_STARTED ? View.VISIBLE : View.GONE);
        String today = "";
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        if (DateUtils.formatDate(calendar.getTime()).equals(DateUtils.formatDate(data.getBeginDate()))) {
            today = "今日";
        }
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date tormary = calendar.getTime();
        if (DateUtils.formatDate(tormary).equals(DateUtils.formatDate(data.getBeginDate()))) {
            today = "明日";
        }
        String text = MessageFormat.format(mContext.getString(R.string.activity_date_msg), today,
                DateUtils.formatDateByFormat(data.getBeginDate(), "MM/dd"),
                DateUtils.formatDateByFormat(data.getBeginDate(), "HH:mm"),
                DateUtils.formatDateByFormat(data.getEndDate(), "HH:mm"));
        holder.dateTv.setText(text);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onActivityClickListener == null) {
                    Intent intent = new Intent(mContext, ActivityInfoActivity.class);
                    intent.putExtra(ActivityInfoActivity.ACTIVITY_NAME_KEY, data.getActivitName());
                    intent.putExtra(ActivityInfoActivity.ACTIVITY_ID_KEY, data.getActivitId());
                    mContext.startActivity(intent);
                } else {
                    onActivityClickListener.onAcitvityLick(data);
                }
            }
        });
    }

    public void setOnActivityClickListener(OnActivityClickListener onActivityClickListener) {
        this.onActivityClickListener = onActivityClickListener;
    }

    public class ActivityHolder extends ZhiheAdapter.BaseViewHolder {

        @ViewInject(R.id.activity_cover_iv)
        public ImageView coverImage;
        @ViewInject(R.id.activity_ing_iv)
        public ImageView ingFlag;
        @ViewInject(R.id.activity_date_tv)
        public TextView dateTv;

        public ActivityHolder(View itemView) {
            super(itemView);
        }
    }

    public interface OnActivityClickListener {
        void onAcitvityLick(Activity activity);
    }
}
