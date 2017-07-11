package com.hwz.shopcorner.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hwz.shopcorner.LoginActivity;
import com.hwz.shopcorner.bean.User;
import com.hwz.shopcorner.global.SCApplication;

import org.xutils.x;

/**
 * Created by huwang on 2017/6/20.
 */

public class BaseFragment extends Fragment {

    private boolean injected = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        injected = true;
        return x.view().inject(this, inflater, container);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!injected) {
            x.view().inject(this, this.getView());
        }
    }

    public void startActivity(Intent intent, boolean isNeedLogin) {

        if (isNeedLogin) {
            User user = SCApplication.getInstance().getUser();
            if (user != null) {
                super.startActivity(intent);
            } else {
                SCApplication.getInstance().putIntent(intent);
                Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                super.startActivity(loginIntent);
            }

        } else {
            super.startActivity(intent);
        }

    }
}
