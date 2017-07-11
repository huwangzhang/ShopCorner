package com.hwz.shopcorner.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.hwz.shopcorner.R;
import com.lzy.ninegrid.NineGridView;
import com.squareup.picasso.Picasso;

/**
 * Created by huwang on 2017/6/25.
 */

public class PicassoImageLoader implements NineGridView.ImageLoader {

    @Override
    public void onDisplayImage(Context context, ImageView imageView, String url) {
        Picasso.with(context).load(url)//
                .placeholder(R.drawable.ic_default_color)//
                .error(R.drawable.ic_default_color)//
                .into(imageView);
    }

    @Override
    public Bitmap getCacheImage(String url) {
        return null;
    }
}
