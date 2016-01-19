package cn.com.zhihetech.online;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.alibaba.fastjson.JSON;

import org.junit.Test;

import java.util.List;

import cn.com.zhihetech.online.bean.Activity;
import cn.com.zhihetech.online.bean.Banner;
import cn.com.zhihetech.online.core.common.PageData;
import cn.com.zhihetech.online.core.common.Pager;
import cn.com.zhihetech.online.core.http.ArrayCallback;
import cn.com.zhihetech.online.core.http.PageDataCallback;
import cn.com.zhihetech.online.model.ActivityModel;
import cn.com.zhihetech.online.model.BannerModel;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    @Test
    public void BannerModelTest() {
        new BannerModel().getBanners(new ArrayCallback<Banner>() {
            @Override
            public void onArray(List<Banner> datas) {
                if (datas != null) {
                    for (Banner banner : datas) {
                        Log.d("BannerModelTest", JSON.toJSONString(banner));
                    }
                }
            }
        });
    }

    @Test
    public void ActivityModelTest() {
        new ActivityModel().getActivities(new PageDataCallback<Activity>() {
            @Override
            public void onPageData(PageData<Activity> result, List<Activity> rows) {
                Log.d("ApplicationTest", JSON.toJSONString(rows));
            }
        }, new Pager());
    }
}