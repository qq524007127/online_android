package cn.com.zhihetech.online.core.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.xutils.common.util.DensityUtil;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import cn.com.zhihetech.online.R;

/**
 * Created by ShenYunjie on 2016/1/22.
 */
public class SearchTypePopupWindow extends PopupWindow {

    public final static int SEARCH_ACTIVITY_TYPE = 0xA;
    public final static int SEARCH_GOODS_TYPE = 0xB;
    public final static int SEARCH_MERCHANT_TYPE = 0xC;

    private final int[] types = {SEARCH_ACTIVITY_TYPE, SEARCH_GOODS_TYPE, SEARCH_MERCHANT_TYPE};
    private final String[] names = {"活动", "商品", "店铺"};
    private final int[] drawables = {R.drawable.search_activity, R.drawable.search_goods, R.drawable.search_merchant};

    private int currentType = types[0];
    private String currentName = names[0];

    private OnSearchTypeSelectedListener onSearchTypeSelectedListener;

    @ViewInject(R.id.searchtype_content_ls)
    private ListView listView;

    private Context context;

    public SearchTypePopupWindow(Context context) {
        super(context);
        this.context = context;
        init();
    }

    private void init() {
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth(DensityUtil.dip2px(109));
        setFocusable(true);
        setOutsideTouchable(true);
        //setBackgroundDrawable(context.getResources().getDrawable(R.drawable.search_type_list_bg));
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        initContentView();
        update();
    }

    private void initContentView() {
        View contentView = LayoutInflater.from(this.context).inflate(R.layout.content_search_type, null);
        x.view().inject(this, contentView);
        setContentView(contentView);
        listView.setAdapter(new SearchTypeAdapter());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentType = types[position];
                currentName = names[position];
                if (onSearchTypeSelectedListener != null) {
                    onSearchTypeSelectedListener.onSearchTypeSelected(currentType, currentName);
                }
                dismiss();
            }
        });
    }

    public void setOnSearchTypeSelectedListener(OnSearchTypeSelectedListener onSearchTypeSelectedListener) {
        this.onSearchTypeSelectedListener = onSearchTypeSelectedListener;
    }

    public int getCurrentType() {
        return currentType;
    }

    public String getCurrentName() {
        return currentName;
    }

    class SearchTypeAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return types.length;
        }

        @Override
        public Object getItem(int position) {
            return types[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            SearchTypeHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_type_item, parent, false);
                holder = new SearchTypeHolder(convertView);
                convertView.setTag(holder);
            }
            holder = (SearchTypeHolder) convertView.getTag();
            holder.typeIcon.setImageDrawable(context.getResources().getDrawable(drawables[position]));
            holder.typeName.setText(names[position]);
            return convertView;
        }
    }

    class SearchTypeHolder {
        @ViewInject(R.id.search_type_iv)
        public ImageView typeIcon;
        @ViewInject(R.id.search_type_tv)
        public TextView typeName;

        public SearchTypeHolder(View itemView) {
            x.view().inject(this, itemView);
        }
    }

    public interface OnSearchTypeSelectedListener {
        void onSearchTypeSelected(int searchType, String searchTypeName);
    }
}
