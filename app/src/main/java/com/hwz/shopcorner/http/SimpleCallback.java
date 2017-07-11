package com.hwz.shopcorner.http;

import android.content.Context;
import android.content.Intent;

import com.hwz.shopcorner.LoginActivity;
import com.hwz.shopcorner.R;
import com.hwz.shopcorner.global.SCApplication;
import com.hwz.shopcorner.utils.ToastUtils;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by huwang on 2017/6/23.
 */

public abstract class SimpleCallback<T> extends BaseCallback<T> {

    protected Context mContext;

    public SimpleCallback(Context context) {

        mContext = context;

    }

    @Override
    public void onRequestBefore(Request request) {

    }

    @Override
    public void onFailure(Request request, Exception e) {

    }

    @Override
    public void onResponse(Response response) {

    }

    @Override
    public void onTokenError(Response response, int code) {
        ToastUtils.show(mContext, mContext.getString(R.string.token_error));

        Intent intent = new Intent();
        intent.setClass(mContext, LoginActivity.class);
        mContext.startActivity(intent);

        SCApplication.getInstance().clearUser();

    }
}
