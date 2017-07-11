package com.hwz.shopcorner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hwz.shopcorner.bean.User;
import com.hwz.shopcorner.global.SCApplication;

import org.xutils.x;

/**
 * Created by huwang on 2017/6/21.
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
    }

    public void startActivity(Intent intent, boolean isNeedLogin) {

        if (isNeedLogin) {
            User user = SCApplication.getInstance().getUser();
            if (user != null) {
                super.startActivity(intent);
            } else {

                SCApplication.getInstance().putIntent(intent);
                Intent loginIntent = new Intent(this
                        , LoginActivity.class);
                super.startActivity(loginIntent);

            }

        } else {
            super.startActivity(intent);
        }

    }
}
