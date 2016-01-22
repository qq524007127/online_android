package cn.com.zhihetech.online.ui.widget;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.core.view.SearchTypePopupWindow;
import cn.com.zhihetech.online.ui.fragment.SearchActivityFragment;
import cn.com.zhihetech.online.ui.fragment.SearchGoodsFragment;
import cn.com.zhihetech.online.ui.fragment.SearchMerchantFragment;

/**
 * Created by ShenYunjie on 2016/1/21.
 */
@ContentView(R.layout.activity_search)
public class SearchActivity extends BaseActivity {

    @ViewInject(R.id.search_type_btn)
    private Button searchTypeBtn;
    @ViewInject(R.id.search_submit_btn)
    private Button searchBtn;
    @ViewInject(R.id.search_input_et)
    private EditText searchInput;

    private SearchTypePopupWindow searchTypePop;

    private SearchActivityFragment activityFragment = new SearchActivityFragment();
    private SearchGoodsFragment goodsFragment = new SearchGoodsFragment();
    private SearchMerchantFragment merchantFragment = new SearchMerchantFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }

    private void initViews() {
        initSearchTypePop();
    }

    private void initSearchTypePop() {
        searchTypePop = new SearchTypePopupWindow(this);
        searchTypeBtn.setText(searchTypePop.getCurrentName());
        showSearchFragment(searchTypePop.getCurrentType());
        searchTypePop.setOnSearchTypeSelectedListener(new SearchTypePopupWindow.OnSearchTypeSelectedListener() {
            @Override
            public void onSearchTypeSelected(int searchType, String searchTypeName) {
                searchTypeBtn.setText(searchTypeName);
                searchTypeBtn.setTag(searchType);
                showSearchFragment(searchType);
            }
        });
    }

    @Event({R.id.search_type_btn, R.id.search_submit_btn})
    private void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.search_type_btn:
                searchTypePop.showAsDropDown(view);
                break;
            case R.id.search_submit_btn:
                /*if (StringUtils.isEmpty(searchInput.getText().toString())) {
                    showMsg(searchInput, "搜索类容不能为空");
                    return;
                }*/
                doSearch(searchInput.getText().toString());
                break;
        }
    }

    /**
     * 根据不同的搜索类别显示不同的界面
     *
     * @param searchType
     */
    private void showSearchFragment(int searchType) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (searchType) {
            case SearchTypePopupWindow.SEARCH_ACTIVITY_TYPE:
                transaction.replace(R.id.search_content_fl, activityFragment).commit();
                break;
            case SearchTypePopupWindow.SEARCH_GOODS_TYPE:
                transaction.replace(R.id.search_content_fl, goodsFragment).commit();
                break;
            case SearchTypePopupWindow.SEARCH_MERCHANT_TYPE:
                transaction.replace(R.id.search_content_fl, merchantFragment).commit();
                break;
        }
    }

    private void doSearch(String searchValue) {
        switch (searchTypePop.getCurrentType()) {
            case SearchTypePopupWindow.SEARCH_ACTIVITY_TYPE:
                activityFragment.doSearch(searchValue);
                break;
            case SearchTypePopupWindow.SEARCH_GOODS_TYPE:
                goodsFragment.doSearch(searchValue);
                break;
            case SearchTypePopupWindow.SEARCH_MERCHANT_TYPE:
                merchantFragment.doSearch(searchValue);
                break;
        }
    }
}
