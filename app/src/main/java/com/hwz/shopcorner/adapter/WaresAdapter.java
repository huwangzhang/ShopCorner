package com.hwz.shopcorner.adapter;

import android.content.Context;
import android.net.Uri;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hwz.shopcorner.R;
import com.hwz.shopcorner.bean.Wares;

import java.util.List;

/**
 * Created by huwang on 2017/6/21.
 */

public class WaresAdapter extends SimpleAdapter<Wares> {

    public WaresAdapter(List<Wares> datas, Context context) {
        super(datas, context, R.layout.template_grid_wares);
    }

    @Override
    public void bindData(BaseViewHolder holder, Wares wares) {
        holder.getTextView(R.id.text_title).setText(wares.getName());
        holder.getTextView(R.id.text_price).setText("￥"+wares.getPrice());
        SimpleDraweeView draweeView = (SimpleDraweeView) holder.getView(R.id.drawee_view);
        draweeView.setImageURI(Uri.parse(wares.getImgUrl()));
    }
}
