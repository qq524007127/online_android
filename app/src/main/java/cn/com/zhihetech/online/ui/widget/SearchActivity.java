package cn.com.zhihetech.online.ui.widget;


import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.core.view.SearchTypeView;

/**
 * Created by ShenYunjie on 2016/1/21.
 */
@ContentView(R.layout.activity_search)
public class SearchActivity extends BaseActivity {

    @ViewInject(R.id.search_type_btn)
    private Button searchTypeBtn;
    @ViewInject(R.id.search_submit_btn)
    private Button searchBtn;

    private PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }

    private void initViews() {
        initSearchTypePop();
    }

    private void initSearchTypePop() {
        popupWindow = new PopupWindow(new SearchTypeView(this), ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                searchTypeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupWindow.showAsDropDown(view);
                    }
                });
            }
        });
    }

    @Event({R.id.search_type_btn})
    private void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.search_type_btn:
                popupWindow.showAsDropDown(view);
                break;
        }
    }
}
