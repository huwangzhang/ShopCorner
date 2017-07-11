package com.hwz.shopcorner.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hwz.shopcorner.AddressListActivity;
import com.hwz.shopcorner.LoginActivity;
import com.hwz.shopcorner.MainActivity;
import com.hwz.shopcorner.MyFavoriteActivity;
import com.hwz.shopcorner.MyOrderActivity;
import com.hwz.shopcorner.R;
import com.hwz.shopcorner.bean.User;
import com.hwz.shopcorner.global.Constants;
import com.hwz.shopcorner.global.SCApplication;
import com.hwz.shopcorner.widget.MyToolBar;
import com.squareup.picasso.Picasso;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by huwang on 2017/6/19.
 */
@ContentView(R.layout.fragment_mine)
public class MineFragment extends BaseFragment {
    @ViewInject(R.id.img_head)
    private CircleImageView mImageHead;

    @ViewInject(R.id.txt_username)
    private TextView mTxtUserName;

    @ViewInject(R.id.btn_logout)
    private Button mBtnLogout;

    private MyToolBar mToolbar;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {
        User user = SCApplication.getInstance().getUser();
        showUser(user);
    }

    /////////////////////////////////////////
    @Event(value = {R.id.img_head, R.id.txt_username}, type = View.OnClickListener.class)
    private void toLogin(View view) {
        if (SCApplication.getInstance().getUser() == null) {
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivityForResult(intent, Constants.REQUEST_CODE);
        }
    }

    @Event(value = R.id.btn_logout)
    private void logout(View view) {

        SCApplication.getInstance().clearUser();
        showUser(null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 更改界面
        User user = SCApplication.getInstance().getUser();
        showUser(user);
    }

    private void showUser(User user) {

        if (user != null) {

            if (!TextUtils.isEmpty(user.getLogo_url())) {
                showHeadImage(user.getLogo_url());
            }

            mTxtUserName.setText(user.getUsername());

            mBtnLogout.setVisibility(View.VISIBLE);
        } else {
            mTxtUserName.setText(R.string.to_login);
            mBtnLogout.setVisibility(View.GONE);
        }
    }

    private void showHeadImage(String url) {

        Picasso.with(getActivity()).load(url).into(mImageHead);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) context;
            mToolbar = (MyToolBar) mainActivity.findViewById(R.id.toolBar);
            if (mToolbar != null) {
                mToolbar.setVisibility(View.GONE);
            }
        }
    }

    @Event(R.id.tv_address)
    private void listAddress(View view) {
        startActivity(new Intent(getActivity(), AddressListActivity.class));
    }

    @Event(R.id.txt_my_orders)
    private void listOrder(View view) {
        startActivity(new Intent(getActivity(), MyOrderActivity.class));
    }

    @Event(R.id.txt_favorite)
    private void listFavorite(View view) {
        startActivity(new Intent(getActivity(), MyFavoriteActivity.class));
    }
}
