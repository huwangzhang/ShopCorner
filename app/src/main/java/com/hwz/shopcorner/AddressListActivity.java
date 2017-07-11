package com.hwz.shopcorner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hwz.shopcorner.adapter.AddressAdapter;
import com.hwz.shopcorner.bean.Address;
import com.hwz.shopcorner.global.Constants;
import com.hwz.shopcorner.global.SCApplication;
import com.hwz.shopcorner.http.OkHttpHelper;
import com.hwz.shopcorner.http.SpotsCallback;
import com.hwz.shopcorner.msg.BaseRespMsg;
import com.hwz.shopcorner.widget.MyToolBar;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Response;

/**
 * Created by huwang on 2017/6/24.
 */

@ContentView(R.layout.activity_address_list)
public class AddressListActivity extends BaseActivity {
    @ViewInject(R.id.toolbar)
    private MyToolBar mToolBar;

    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerview;

    @ViewInject(R.id.alert_txt_address)
    private TextView mAlertTxt;

    private AddressAdapter mAdapter;


    private OkHttpHelper mHttpHelper = OkHttpHelper.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initToolbar();

        initAddress();
    }


    private void initToolbar() {

        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mToolBar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toAddActivity();
            }
        });

    }


    private void toAddActivity() {

        Intent intent = new Intent(this, AddressAddActivity.class);
        startActivityForResult(intent, Constants.REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        initAddress();
    }

    private void initAddress() {

        Map<String, Object> params = new HashMap<>(1);
        params.put("user_id", SCApplication.getInstance().getUser().getId());

        mHttpHelper.get(Constants.ADDRESS_LIST, params, new SpotsCallback<List<Address>>(this) {

            public void onSuccess(Response response, List<Address> addresses) {
                System.out.println("response= " + response.toString());
                showAddress(addresses);
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                // 服务端
                System.out.println("response= " + response.toString());
            }
        });
    }


    private void showAddress(List<Address> addresses) {
        Collections.sort(addresses);

        if (mAdapter == null) {
            mAdapter = new AddressAdapter(this, addresses, new AddressAdapter.AddressListener() {
                @Override
                public void setDefault(Address address) {
                    updateAddress(address);
                }
            });
            mRecyclerview.setAdapter(mAdapter);
            mRecyclerview.setLayoutManager(new LinearLayoutManager(AddressListActivity.this));
            mRecyclerview.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        } else {
            mAdapter.refreshData(addresses);
            mRecyclerview.setAdapter(mAdapter);
        }
    }


    public void updateAddress(Address address) {

        Map<String, Object> params = new HashMap<>(1);
        params.put("id", address.getId());
        params.put("consignee", address.getConsignee());
        params.put("phone", address.getPhone());
        params.put("addr", address.getAddr());
        params.put("zip_code", address.getZipCode());
        params.put("is_default", address.getIsDefault());

        mHttpHelper.post(Constants.ADDRESS_UPDATE, params, new SpotsCallback<BaseRespMsg>(this) {

            @Override
            public void onSuccess(Response response, BaseRespMsg baseRespMsg) {
                if (baseRespMsg.getStatus() == BaseRespMsg.STATUS_SUCCESS) {
                    initAddress();
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });

    }

}
