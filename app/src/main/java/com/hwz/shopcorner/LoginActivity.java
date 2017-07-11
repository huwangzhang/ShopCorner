package com.hwz.shopcorner;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.hwz.shopcorner.bean.User;
import com.hwz.shopcorner.global.Constants;
import com.hwz.shopcorner.global.SCApplication;
import com.hwz.shopcorner.http.OkHttpHelper;
import com.hwz.shopcorner.http.SpotsCallback;
import com.hwz.shopcorner.msg.LoginRespMsg;
import com.hwz.shopcorner.utils.DESUtils;
import com.hwz.shopcorner.utils.ToastUtils;
import com.hwz.shopcorner.widget.ClearEditText;
import com.hwz.shopcorner.widget.MyToolBar;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Response;

/**
 * Created by huwang on 2017/6/23.
 */

@ContentView(R.layout.activity_login)
public class LoginActivity extends BaseActivity {

    @ViewInject(R.id.toolbar)
    private MyToolBar mToolBar;
    @ViewInject(R.id.etxt_phone)
    private ClearEditText mEtxtPhone;
    @ViewInject(R.id.etxt_pwd)
    private ClearEditText mEtxtPwd;


    private OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initToolBar();
    }

    private void initToolBar() {
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.this.finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    @Event(value=R.id.btn_login)
    private void login(View view){

        String phone = mEtxtPhone.getText().toString().trim();
        if(TextUtils.isEmpty(phone)){
            ToastUtils.show(this, "请输入手机号码");
            return;
        }

        String pwd = mEtxtPwd.getText().toString().trim();
        if(TextUtils.isEmpty(pwd)){
            ToastUtils.show(this,"请输入密码");
            return;
        }


        Map<String,Object> params = new HashMap<>(2);
        params.put("phone",phone);
        params.put("password", DESUtils.encode(Constants.DES_KEY,pwd));

        okHttpHelper.post(Constants.LOGIN, params, new SpotsCallback<LoginRespMsg<User>>(this) {

            @Override
            public void onSuccess(Response response, LoginRespMsg<User> userLoginRespMsg) {

                SCApplication application =  SCApplication.getInstance();
                application.putUser(userLoginRespMsg.getData(), userLoginRespMsg.getToken());

                if(application.getIntent() == null){
                    /////////
                    setResult(RESULT_OK);
                    finish();
                }else{
                    // 登录成功，启动原有的意图
                    application.jumpToTargetActivity(LoginActivity.this);
                    finish();
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    @Event(R.id.txt_toReg)
    private void register(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}
