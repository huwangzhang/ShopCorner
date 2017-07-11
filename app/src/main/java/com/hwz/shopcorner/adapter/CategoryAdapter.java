package com.hwz.shopcorner.adapter;

import android.content.Context;

import com.hwz.shopcorner.R;
import com.hwz.shopcorner.bean.Category;

import java.util.List;

/**
 * Created by huwang on 2017/6/20.
 */

public class CategoryAdapter extends SimpleAdapter<Category> {
    public CategoryAdapter(List<Category> datas, Context context) {
        super(datas, context, R.layout.template_single_text);
    }

    @Override
    public void bindData(BaseViewHolder holder, Category category) {
        holder.getTextView(R.id.textView).setText(category.getName());
    }
}
