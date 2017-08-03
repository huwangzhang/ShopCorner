package com.hwz.shopcorner.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Iterator;
import java.util.List;

/**
 * Created by huwang on 2017/6/20.
 */

public abstract class BaseAdapter<T, V extends BaseViewHolder> extends RecyclerView.Adapter<BaseViewHolder> {
    protected List<T> mDatas;
    protected LayoutInflater mInflater;
    protected Context mContext;
    protected int mLayoutResId;

    public BaseAdapter(List<T> datas, Context context, int layoutResId) {
        mDatas = datas;
        mContext = context;
        mLayoutResId = layoutResId;
        mInflater = LayoutInflater.from(mContext);
    }


    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(mLayoutResId, null);
        return new BaseViewHolder(view, mItemClickListener);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        T t = getItem(position);
        // 绑定数据
        bindData(holder, t);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public T getItem(int position) {
        return mDatas.get(position);
    }

    public abstract void bindData(BaseViewHolder holder, T t);


    protected OnItemClickListener mItemClickListener;

    public interface OnItemClickListener {
        void onClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mItemClickListener = listener;
    }


    public List<T> getDatas() {

        return mDatas;
    }

    /**
     * 从列表中删除某项
     *
     * @param t
     */
    public void removeItem(T t) {

        int position = mDatas.indexOf(t);
        mDatas.remove(position);
        notifyItemRemoved(position);
    }

    public void clearData() {
        for (Iterator it = mDatas.iterator(); it.hasNext(); ) {
            T t = (T) it.next();
            int position = mDatas.indexOf(t);
            it.remove();
            notifyItemRemoved(position);
        }
    }
    //TODO 有bug
//    public void clearData() { //  有bug
//
//        mDatas.clear();
//        notifyItemRangeRemoved(0, mDatas.size());
//    }

    public void addData(List<T> datas) {
        addData(0, datas);
    }

    public void addData(int position, List<T> datas) {

        if (datas != null && datas.size() > 0) {
            mDatas.addAll(datas);
            notifyItemRangeChanged(position, mDatas.size());
        }

    }

    public void refreshData(List<T> list) {
        if (list != null && list.size() > 0) {
            clearData();
            int size = list.size();
            for (int i = 0; i < size; i++) {
                mDatas.add(i, list.get(i));
                notifyItemInserted(i);
            }
        }
    }

    public void loadMoreData(List<T> list) {
        if (list != null && list.size() > 0) {
            int size = list.size();
            int begin = mDatas.size();
            for (int i = 0; i < size; i++) {
                mDatas.add(list.get(i));
                notifyItemInserted(i + begin);
            }
        }
    }
}
