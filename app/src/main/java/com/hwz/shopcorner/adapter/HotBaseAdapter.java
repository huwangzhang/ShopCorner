package com.hwz.shopcorner.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.Button;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hwz.shopcorner.R;
import com.hwz.shopcorner.utils.ShopCartProvider;
import com.hwz.shopcorner.utils.ToastUtils;
import com.hwz.shopcorner.bean.ShoppingCart;
import com.hwz.shopcorner.bean.Wares;

import java.util.List;

/**
 * Created by huwang on 2017/6/20.
 */

public class HotBaseAdapter extends SimpleAdapter<Wares> {
    private ShopCartProvider mCart;
    private Context mContext;

    public HotBaseAdapter(List<Wares> datas, Context context) {
        super(datas, context, R.layout.template_hot_wares);
        mCart = new ShopCartProvider(context);
        mContext = context;
    }

    @Override
    public void bindData(BaseViewHolder holder, final Wares wares) {
        SimpleDraweeView draweeView = (SimpleDraweeView) holder.getView(R.id.drawee_view);
        draweeView.setImageURI(Uri.parse(wares.getImgUrl()));
        holder.getTextView(R.id.text_title).setText(wares.getName());
        holder.getTextView(R.id.text_price).setText("￥" + wares.getPrice());
        Button payButton = holder.getButton(R.id.payButton);
        if (payButton != null) {
            payButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCart.put(convertData(wares));
                    ToastUtils.show(mContext, "已添加到购物车");
                }
            });
        }

    }

    public ShoppingCart convertData(Wares item) {

        ShoppingCart cart = new ShoppingCart();

        cart.setId(item.getId());
        cart.setDescription(item.getDescription());
        cart.setImgUrl(item.getImgUrl());
        cart.setName(item.getName());
        cart.setPrice(item.getPrice());

        return cart;
    }

    public void resetLayout(int layoutId) {
        this.mLayoutResId = layoutId;
        notifyItemRangeChanged(0, mDatas.size()-1);
    }
}
