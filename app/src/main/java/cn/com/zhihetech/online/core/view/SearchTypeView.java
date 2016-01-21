package cn.com.zhihetech.online.core.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import cn.com.zhihetech.online.R;

/**
 * Created by ShenYunjie on 2016/1/21.
 */
public class SearchTypeView extends FrameLayout {

    private final static int SEARCH_ACTIVITY_TYPE = 0xA;
    private final static int SEARCH_GOODS_TYPE = 0xB;
    private final static int SEARCH_MERCHANT_TYPE = 0xC;

    private final int[] searchTypes = {SEARCH_ACTIVITY_TYPE, SEARCH_GOODS_TYPE, SEARCH_MERCHANT_TYPE};
    private final String[] types = {"活动", "商品", "店铺"};
    private final int[] drawables = {R.drawable.search_activity, R.drawable.search_goods, R.drawable.search_merchant};

    @ViewInject(R.id.searchtype_content_ls)
    private ListView listView;

    public SearchTypeView(Context context) {
        super(context);
        init();
    }

    private void init() {
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.content_search_type, null);
        addView(rootView);
        x.view().inject(this, rootView);
        listView.setAdapter(new SearchTypeAdapter());
    }

    class SearchTypeAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return searchTypes.length;
        }

        @Override
        public Object getItem(int position) {
            return searchTypes[position];
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
            holder.typeIcon.setImageDrawable(getResources().getDrawable(drawables[position]));
            holder.typeName.setText(types[position]);
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
}
