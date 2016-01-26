package cn.com.zhihetech.online.core.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import cn.com.zhihetech.online.core.view.OnAdapterChangeHandle;

/**
 * Created by ShenYunjie on 2016/1/15.
 */
public abstract class ZhiheAdapter<T, VH extends ZhiheAdapter.BaseViewHolder> extends BaseAdapter implements OnAdapterChangeHandle<T> {
    protected List<T> mDatas = new ArrayList<>();
    protected Context mContext;
    protected int layoutId;

    public ZhiheAdapter(Context mContext, int layoutId) {
        this.mContext = mContext;
        this.layoutId = layoutId;
    }

    public ZhiheAdapter(Context mContext, int layoutId, List<T> mDatas) {
        this(mContext, layoutId);
        if (mDatas != null && mDatas.size() > 0) {
            this.mDatas = mDatas;
        }
    }

    @Override
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        VH viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(layoutId, parent, false);
            convertView.setTag(onCreateViewHolder(convertView));
        }
        viewHolder = (VH) convertView.getTag();
        onBindViewHolder(viewHolder, mDatas.get(position));
        return convertView;
    }

    public abstract VH onCreateViewHolder(View itemView);

    public abstract void onBindViewHolder(VH holder, T data);

    @Override
    public void refreshData(List<T> datas) {
        mDatas.clear();
        if (datas != null) {
            mDatas.addAll(datas);
        }
        notifyDataSetChanged();
    }

    @Override
    public void addDatas(List<T> datas) {
        if (datas != null && datas.size() > 0) {
            mDatas.addAll(datas);
            notifyDataSetChanged();
        }
    }

    @Override
    public void clearData() {
        if (mDatas.size() > 0) {
            mDatas.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public void insertData(int position, T data) {
        mDatas.add(position, data);
        notifyDataSetChanged();
    }

    @Override
    public void addData(T data) {
        mDatas.add(data);
        notifyDataSetChanged();
    }

    @Override
    public void update(T data) {
        for (int i = 0; i < mDatas.size(); i++) {
            T t = mDatas.get(i);
            if (t == data) {
                mDatas.remove(i);
                mDatas.add(i, t);
                notifyDataSetChanged();
                return;
            }
        }
    }

    @Override
    public void remove(T data) {
        mDatas.remove(data);
        notifyDataSetChanged();
    }

    @Override
    public void remove(int position) {
        mDatas.remove(position);
        notifyDataSetChanged();
    }

    /**
     * 基础ViewHolder类
     */
    public class BaseViewHolder {
        protected View itemView;

        public BaseViewHolder(View itemView) {
            this.itemView = itemView;
            x.view().inject(this, itemView);
        }
    }
}
