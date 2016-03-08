package cn.com.zhihetech.online.ui.activity;

import android.app.ProgressDialog;
import android.os.Bundle;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.RedEnvelopItem;
import cn.com.zhihetech.online.core.adapter.RedEnvelopItemAdapter;
import cn.com.zhihetech.online.core.common.PageData;
import cn.com.zhihetech.online.core.view.LoadMoreListView;

/**
 * Created by ShenYunjie on 2016/3/8.
 */
@ContentView(R.layout.activity_red_envelop_item_list)
public class MyRedEnvelopItemListActivity extends BaseActivity {

    @ViewInject(R.id.envelop_item_list_view)
    private LoadMoreListView envelopItemListView;

    private ProgressDialog progressDialog;

    private PageData<RedEnvelopItem> pageData;
    private RedEnvelopItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
