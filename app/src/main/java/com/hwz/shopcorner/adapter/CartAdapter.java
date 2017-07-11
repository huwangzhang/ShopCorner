package com.hwz.shopcorner.adapter;

import android.content.Context;
import android.net.Uri;
import android.text.Html;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hwz.shopcorner.R;
import com.hwz.shopcorner.utils.ShopCartProvider;
import com.hwz.shopcorner.bean.ShoppingCart;
import com.hwz.shopcorner.widget.NumberAddMinusView;

import java.util.Iterator;
import java.util.List;

/**
 * Created by huwang on 2017/6/21.
 */

public class CartAdapter extends SimpleAdapter<ShoppingCart> implements BaseAdapter.OnItemClickListener{

    public static final String TAG="CartAdapter";

    private CheckBox checkBox;
    private TextView textView;

    private ShopCartProvider cartProvider;

    public CartAdapter(List<ShoppingCart> datas, Context context, final CheckBox checkBox, TextView textView) {
        super(datas, context, R.layout.template_cart);
        this.checkBox = checkBox;
        this.textView = textView;
        cartProvider = new ShopCartProvider(context);
        setOnItemClickListener(this);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAll_None(checkBox.isChecked());
                showTotalPrice();
//                checkBox.isChecked();
            }
        });
        showTotalPrice();
    }

    @Override
    public void bindData(BaseViewHolder holder, final ShoppingCart cart) {
        holder.getTextView(R.id.text_title).setText(cart.getName());
        holder.getTextView(R.id.text_price).setText("￥"+cart.getPrice());
        SimpleDraweeView draweeView = (SimpleDraweeView) holder.getView(R.id.drawee_view);
        draweeView.setImageURI(Uri.parse(cart.getImgUrl()));

        CheckBox checkBox = (CheckBox) holder.getView(R.id.checkbox);
        checkBox.setChecked(cart.isChecked());

        NumberAddMinusView numberAddSubView = (NumberAddMinusView) holder.getView(R.id.num_control);

        numberAddSubView.setValue(cart.getCount());

        numberAddSubView.setOnButtonClickListener(new NumberAddMinusView.onButtonClickListener() {
            @Override
            public void onButtonAddClick(View view, int value) {
                cart.setCount(value);
                cartProvider.update(cart);
                showTotalPrice();
            }

            @Override
            public void onButtonMinusClick(View view, int value) {
                cart.setCount(value);
                cartProvider.update(cart);
                showTotalPrice();
            }
        });

    }

    public void showTotalPrice() {
        float total = getTotalPrice();
        textView.setText(Html.fromHtml("合计 ￥<span style='color:#eb4f38'>" + total + "</span>"), TextView.BufferType.SPANNABLE);
    }

    private float getTotalPrice() {
        float sum=0;
        if(!isNotNull())
            return sum;

        for (ShoppingCart cart:
                mDatas) {
            if(cart.isChecked())
                sum += cart.getCount()*cart.getPrice();
        }
        return sum;
    }

    private boolean isNotNull(){

        return (mDatas !=null && mDatas.size()>0);
    }

    @Override
    public void onClick(View view, int position) {
        ShoppingCart item = getItem(position);
        item.setIsChecked(!item.isChecked());
        notifyItemChanged(position);
        isCheckAll();
        showTotalPrice();
    }

    private void isCheckAll() {
        boolean flag = true;
        if (mDatas != null && mDatas.size() > 0) {
            for (ShoppingCart cart : mDatas) {
                if (!cart.isChecked()) {
                    flag = false;
                    break;
                }
            }
        }
        checkBox.setChecked(flag);
    }

    public void checkAll_None(boolean isChecked){
        if(!isNotNull())
            return ;

        int i=0;
        for (ShoppingCart cart :mDatas){
            cart.setIsChecked(isChecked);
            notifyItemChanged(i);
            i++;
        }
    }

    public void delCart() {
        if (!isNotNull())
            return;
        //////////////////////////////////////////
        for (Iterator iterator = mDatas.iterator(); iterator.hasNext();) {
            ShoppingCart  cart = (ShoppingCart) iterator.next();
            if (cart.isChecked()) {
                int position = mDatas.indexOf(cart);
                cartProvider.delete(cart);
                iterator.remove();
                notifyItemRemoved(position);
            }
        }
    }
}
