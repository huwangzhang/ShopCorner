package com.hwz.shopcorner.http;

import android.content.Context;

import dmax.dialog.SpotsDialog;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by huwang on 2017/6/20.
 */

public abstract class SpotsCallback<T> extends SimpleCallback<T> {
    private SpotsDialog mDialog;

    public SpotsCallback(Context context){
        super(context);
        mDialog = new SpotsDialog(context, "拼命加载中...");
    }

    public void showDialog() {
        mDialog.show();
    }

    public void dismissDialog() {
       if (mDialog != null) {
           mDialog.dismiss();
       }
    }

    public void setMessage(String msg) {
        mDialog.setMessage(msg);
    }

    @Override
    public void onRequestBefore(Request request) {
        showDialog();
    }

    @Override
    public void onFailure(Request request, Exception e) {
        dismissDialog();
    }

    @Override
    public void onResponse(Response response) {
        dismissDialog();
    }
}
