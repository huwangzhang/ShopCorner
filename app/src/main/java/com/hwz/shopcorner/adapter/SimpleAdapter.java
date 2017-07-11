package com.hwz.shopcorner.adapter;

import android.content.Context;

import java.util.List;

/**
 * Created by huwang on 2017/6/20.
 */

public abstract class SimpleAdapter<T> extends BaseAdapter<T, BaseViewHolder> {
    public SimpleAdapter(List<T> datas, Context context, int layoutResId) {
        super(datas, context, layoutResId);
    }
}
