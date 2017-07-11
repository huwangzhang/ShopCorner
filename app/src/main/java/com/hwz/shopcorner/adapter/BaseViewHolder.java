package com.hwz.shopcorner.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzy.ninegrid.NineGridView;

/**
 * Created by huwang on 2017/6/20.
 */

public class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    protected SparseArray<View> mViews;
    protected BaseAdapter.OnItemClickListener mItemClickListener;

    public BaseViewHolder(View itemView, BaseAdapter.OnItemClickListener itemClickListener) {
        super(itemView);
        mViews = new SparseArray<>();
        mItemClickListener = itemClickListener;
        itemView.setOnClickListener(this);
    }

    private <T extends View> T findView(int id) {
        View view = mViews.get(id);
        if (view == null) {
            view = itemView.findViewById(id);
            mViews.put(id, view);
        }
        return (T) view;
    }

    public View getView(int id) {
        return findView(id);
    }

    public TextView getTextView(int id) {
        return findView(id);
    }

    public ImageView getImageView(int id) {
        return findView(id);
    }

    public Button getButton(int id) {
        return findView(id);
    }

    public CheckBox getCheckBox(int id) {
        return findView(id);
    }

    public NineGridView getNineGridView(int id) {
        return findView(id);
    }

    @Override
    public void onClick(View v) {
        if (mItemClickListener != null) {
            mItemClickListener.onClick(v, getLayoutPosition());
        }
    }
}
