package com.hwz.shopcorner.utils;

import android.content.Context;
import android.text.TextUtils;

import com.hwz.shopcorner.bean.User;
import com.hwz.shopcorner.global.Constants;

public class UserLocalData {


    public static void putUser(Context context, User user) {


        String user_json = JSONUtils.toJSON(user);
        PreferencesUtils.putString(context, Constants.USER_JSON, user_json);

    }

    public static void putToken(Context context, String token) {

        PreferencesUtils.putString(context, Constants.TOKEN, token);
    }


    public static User getUser(Context context) {

        String user_json = PreferencesUtils.getString(context, Constants.USER_JSON);
        if (!TextUtils.isEmpty(user_json)) {

            return JSONUtils.fromJson(user_json, User.class);
        }
        return null;
    }

    public static String getToken(Context context) {

        return PreferencesUtils.getString(context, Constants.TOKEN);

    }


    public static void clearUser(Context context) {


        PreferencesUtils.putString(context, Constants.USER_JSON, "");

    }

    public static void clearToken(Context context) {

        PreferencesUtils.putString(context, Constants.TOKEN, "");
    }

}
