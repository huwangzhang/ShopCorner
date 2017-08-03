package com.hwz.shopcorner.http;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.hwz.shopcorner.global.SCApplication;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by huwang on 2017/6/19.
 */

public class OkHttpHelper {

    public static final int TOKEN_MISSING = 401;// token 丢失
    public static final int TOKEN_ERROR = 402; // token 错误
    public static final int TOKEN_EXPIRE = 403; // token 过期

    private static final String TAG = "OkHttpHelper";
    private static OkHttpClient mClient;
    private Gson mGson;
    private Handler mHandler;

    private static OkHttpHelper mInstance;

    static {
        mInstance = new OkHttpHelper();
    }

    private OkHttpHelper() {
        if (mClient == null) {
            mClient = new OkHttpClient();
        }
        mClient.newBuilder().connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS);
        mGson = new Gson();
        mHandler = new Handler(Looper.getMainLooper());
    }

    public static OkHttpHelper getInstance() {
        return mInstance;
    }

    public void get(String url, BaseCallback callback) {
        get(url, null, callback);
    }

    public void get(String url, Map<String, Object> params, BaseCallback callback) {
        Request request = buildGetRequest(url, params);
        doRequest(request, callback);
    }

    public void post(String url, Map<String, Object> params, BaseCallback callback) {
        Request request = buildPostRequest(url, params);
        doRequest(request, callback);
    }

    private Request buildPostRequest(String url, Map<String, Object> params) {

        return buildRequest(url, HttpMethodType.POST, params);
    }

    private Request buildGetRequest(String url, Map<String, Object> param) {

        return buildRequest(url, HttpMethodType.GET, param);
    }

    private Request buildRequest(String url, HttpMethodType methodType, Map<String, Object> params) {


        Request.Builder builder = new Request.Builder()
                .url(url);

        if (methodType == HttpMethodType.POST) {
            RequestBody body = buidFormData(params);
            builder.post(body);
        } else if (methodType == HttpMethodType.GET) {
            url = buildUrlParams(url, params);
            builder.url(url);
            builder.get();
        }


        return builder.build();
    }

    private String buildUrlParams(String url, Map<String, Object> params) {

        if (params == null)
            params = new HashMap<>(1);

        String token = SCApplication.getInstance().getToken();
        if (!TextUtils.isEmpty(token))
            params.put("token", token);

        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            sb.append(entry.getKey() + "=" + entry.getValue());
            sb.append("&");
        }
        String s = sb.toString();
        if (s.endsWith("&")) {
            s = s.substring(0, s.length() - 1);
        }

        if (url.indexOf("?") > 0) {
            url = url + "&" + s;
        } else {
            url = url + "?" + s;
        }

        return url;
    }

    public void doRequest(final Request request, final BaseCallback callback) {
        callback.onRequestBefore(request);
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "获取网络数据失败");
                callbackFailure(callback, request, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callbackResponse(callback, response);
                Log.d(TAG, "获取网络数据成功");
//                response.isSuccessful()
//                response.body().string();
                if (response.isSuccessful()) {
                    String resultStr = response.body().string();
                    if (callback.mType == String.class) {
                        callbackSuccess(callback, response, resultStr);
                    } else {
                        try {
                            Object object = mGson.fromJson(resultStr, callback.mType);
                            callbackSuccess(callback, response, object);
                        } catch (Exception e) {
                            callback.onError(response, response.code(), e);
                        }
                    }

                } else if (response.code() == TOKEN_ERROR || response.code() == TOKEN_EXPIRE || response.code() == TOKEN_MISSING) {

                    callbackTokenError(callback, response);
                } else {
                    callbackError(callback, response, null);
                }
            }
        });
    }


    private RequestBody buidFormData(Map<String, Object> params) {
        FormBody.Builder builder = new FormBody.Builder();
        if (params != null) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                builder.add(entry.getKey(), entry.getValue() + "");
            }
            // 加上权限验证
            String token = SCApplication.getInstance().getToken();
            if (!TextUtils.isEmpty(token)) {
                builder.add("token", token);
            }
        }
        return builder.build();
    }

    enum HttpMethodType {
        GET,
        POST
    }

    private void callbackTokenError(final BaseCallback callback, final Response response) {

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onTokenError(response, response.code());
            }
        });
    }

    private void callbackSuccess(final BaseCallback callback, final Response response, final Object obj) {

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(response, obj);
            }
        });
    }


    private void callbackError(final BaseCallback callback, final Response response, final Exception e) {

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onError(response, response.code(), e);
            }
        });
    }


    private void callbackFailure(final BaseCallback callback, final Request request, final IOException e) {

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onFailure(request, e);
            }
        });
    }


    private void callbackResponse(final BaseCallback callback, final Response response) {

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onResponse(response);
            }
        });
    }

}
