package com.hwz.shopcorner.adapter;

import android.content.Context;
import android.net.Uri;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hwz.shopcorner.R;
import com.hwz.shopcorner.bean.ShoppingCart;

import java.util.List;

/**
 * Created by huwang on 2017/6/23.
 */

public class WareOrderAdapter extends SimpleAdapter<ShoppingCart> {


    public WareOrderAdapter(Context context, List<ShoppingCart> datas) {
        super(datas, context, R.layout.template_order_wares);

    }

    @Override
    public void bindData(BaseViewHolder viewHoder, final ShoppingCart item) {
        SimpleDraweeView draweeView = (SimpleDraweeView) viewHoder.getView(R.id.drawee_view);
        draweeView.setImageURI(Uri.parse(item.getImgUrl()));
    }


    public float getTotalPrice() {
        float sum = 0;
        if (!isNotNull())
            return sum;
        for (ShoppingCart cart : mDatas) {
            sum += cart.getCount() * cart.getPrice();
        }
        return sum;
    }


    private boolean isNotNull() {
        return (mDatas != null && mDatas.size() > 0);
    }

}
