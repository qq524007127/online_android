package cn.com.zhihetech.online.ui.customview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.Banner;
import cn.com.zhihetech.online.bean.Navigation;
import cn.com.zhihetech.online.core.common.Constant;
import cn.com.zhihetech.online.core.http.ArrayCallback;
import cn.com.zhihetech.online.core.util.ImageLoader;
import cn.com.zhihetech.online.model.BannerModel;
import cn.com.zhihetech.online.model.NavigationModel;
import cn.com.zhihetech.online.ui.widget.DailyNewActivity;

/**
 * Created by ShenYunjie on 2016/1/18.
 */
public class HomeHeaderView extends FrameLayout {

    @ViewInject(R.id.home_banner_cb)
    private ConvenientBanner headerBanner;
    @ViewInject(R.id.home_navigation_rv)
    private RecyclerView navRV;

    public HomeHeaderView(Context context) {
        this(context, null);
    }

    public HomeHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HomeHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public HomeHeaderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.content_home_header, null);
        addView(rootView);
        x.view().inject(this, rootView);
        initBanners();
        initNavigations();
    }

    private void initNavigations() {
        navRV.setHasFixedSize(true);
        navRV.setItemAnimator(new DefaultItemAnimator());
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        navRV.setLayoutManager(layoutManager);
        new NavigationModel().getNavigations(new ArrayCallback<Navigation>() {
            @Override
            public void onArray(final List<Navigation> datas) {
                navRV.setAdapter(new RecyclerView.Adapter<NavigationViewHolder>() {
                    @Override
                    public NavigationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        View itemView = LayoutInflater.from(getContext()).inflate(R.layout.content_navigation_item, parent, false);
                        return new NavigationViewHolder(itemView);
                    }

                    @Override
                    public void onBindViewHolder(NavigationViewHolder holder, int position) {
                        final Navigation nav = datas.get(position);
                        ImageLoader.disPlayImage(holder.navIcon, nav.getImg());
                        holder.navTitle.setText(nav.getNavigationName());
                        holder.itemView.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                doNavigation(nav);
                            }
                        });
                    }

                    @Override
                    public int getItemCount() {
                        return datas == null ? 0 : datas.size();
                    }
                });
            }
        });
    }

    /**
     * 点击导航跳转到相应的界面
     *
     * @param nav
     */
    private void doNavigation(Navigation nav) {
        switch (nav.getViewTargert()) {
            case Constant.NAV_VIEWTARGET_DAILY_NEW:
                Intent intent = new Intent(getContext(), DailyNewActivity.class);
                getContext().startActivity(intent);
                break;
            case Constant.NAV_VIEWTARGET_BUG_KUMING:

                break;
            case Constant.NAV_VIEWTARGET_BUG_PREFECTURES:

                break;
            case Constant.NAV_VIEWTARGET_BIG_BRAND:

                break;
            case Constant.NAV_VIEWTARGET_TYPE_CATEGOR:

                break;
            case Constant.NAV_VIEWTARGET_TYPE_ACTIVITY_ZONE:

                break;
        }
    }

    private void initBanners() {

        new BannerModel().getBanners(new ArrayCallback<Banner>() {
            @Override
            public void onArray(List<Banner> datas) {
                headerBanner.setPages(new CBViewHolderCreator<BannerHolder>() {
                    @Override
                    public BannerHolder createHolder() {
                        return new BannerHolder();
                    }
                }, datas);
            }
        });
    }

    public class NavigationViewHolder extends RecyclerView.ViewHolder {
        @ViewInject(R.id.nav_icon_iv)
        public ImageView navIcon;
        @ViewInject(R.id.nav_title_tv)
        public TextView navTitle;

        public NavigationViewHolder(View itemView) {
            super(itemView);
            x.view().inject(this, itemView);
        }
    }

}
