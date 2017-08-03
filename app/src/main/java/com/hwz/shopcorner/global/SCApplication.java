package com.hwz.shopcorner.global;

import android.content.Context;
import android.content.Intent;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.hwz.shopcorner.bean.User;
import com.hwz.shopcorner.utils.UserLocalData;
import com.mob.MobApplication;

import org.xutils.BuildConfig;
import org.xutils.x;

/**
 * Created by huwang on 2017/6/20.
 */

public class SCApplication extends MobApplication {
    private User user;
    private static SCApplication mInstance;

    public static SCApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        initUser();
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.
        Fresco.initialize(this);
    }

    private void initUser() {

        this.user = UserLocalData.getUser(this);
    }


    public User getUser() {
        return user;
    }


    public void putUser(User user, String token) {
        this.user = user;
        UserLocalData.putUser(this, user);
        UserLocalData.putToken(this, token);
    }

    public void clearUser() {
        this.user = null;
        UserLocalData.clearUser(this);
        UserLocalData.clearToken(this);
    }

    public String getToken() {

        return UserLocalData.getToken(this);
    }


    private Intent intent;

    public void putIntent(Intent intent) {
        this.intent = intent;
    }

    public Intent getIntent() {
        return this.intent;
    }

    public void jumpToTargetActivity(Context context) {
        context.startActivity(intent);
        this.intent = null;
    }
}
