package com.hwz.shopcorner;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hwz.shopcorner.adapter.BaseAdapter;
import com.hwz.shopcorner.adapter.CardViewtemDecortion;
import com.hwz.shopcorner.adapter.MyOrderAdapter;
import com.hwz.shopcorner.bean.Order;
import com.hwz.shopcorner.global.Constants;
import com.hwz.shopcorner.global.SCApplication;
import com.hwz.shopcorner.http.OkHttpHelper;
import com.hwz.shopcorner.http.SpotsCallback;
import com.hwz.shopcorner.utils.PicassoImageLoader;
import com.hwz.shopcorner.widget.MyToolBar;
import com.lzy.ninegrid.NineGridView;

import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Response;

/**
 * Created by huwang on 2017/6/25.
 */

@ContentView(R.layout.activity_my_order)
public class MyOrderActivity extends BaseActivity {
    public static final int STATUS_ALL = 1000;
    public static final int STATUS_SUCCESS = 1; //支付成功的订单
    public static final int STATUS_PAY_FAIL = -2; //支付失败的订单
    public static final int STATUS_PAY_WAIT = 0; //：待支付的订单
    private int status = STATUS_ALL;

    @ViewInject(R.id.toolbar)
    private MyToolBar mToolbar;

    @ViewInject(R.id.tab_layout)
    private TabLayout mTablayout;

    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerview;

    private MyOrderAdapter mAdapter;

    private OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NineGridView.setImageLoader(new PicassoImageLoader());
        initToolBar();
        initTab();
        getOrders();
    }

    private void initToolBar() {

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initTab() {
        TabLayout.Tab tab = mTablayout.newTab();
        tab.setTag(STATUS_ALL);
        tab.setText("全部");
        mTablayout.addTab(tab);

        tab = mTablayout.newTab();
        tab.setText("支付成功");
        tab.setTag(STATUS_SUCCESS);
        mTablayout.addTab(tab);

        tab = mTablayout.newTab();
        tab.setText("待支付");
        tab.setTag(STATUS_PAY_WAIT);
        mTablayout.addTab(tab);

        tab = mTablayout.newTab();
        tab.setText("支付失败");
        tab.setTag(STATUS_PAY_FAIL);
        mTablayout.addTab(tab);


        mTablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                status = (int) tab.getTag();
                getOrders();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void getOrders() {


        Long userId = SCApplication.getInstance().getUser().getId();

        Map<String, Object> params = new HashMap<>();

        params.put("user_id", userId);
        params.put("status", status);


        okHttpHelper.get(Constants.ORDER_LIST, params, new SpotsCallback<List<Order>>(this) {
            @Override
            public void onSuccess(Response response, List<Order> orders) {
                showOrders(orders);
            }

            @Override
            public void onError(Response response, int code, Exception e) {

                LogUtil.d("code:" + code);
            }
        });
    }

    private void showOrders(List<Order> orders) {

        if (mAdapter == null) {
            mAdapter = new MyOrderAdapter(this, orders);
            mRecyclerview.setAdapter(mAdapter);
            mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerview.addItemDecoration(new CardViewtemDecortion());

            mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
                @Override
                public void onClick(View view, int position) {
                    toDetailActivity(position);
                }
            });
        } else {
            mAdapter.refreshData(orders);
            mRecyclerview.setAdapter(mAdapter);
        }
    }

    private void toDetailActivity(int position) {
        Intent intent = new Intent(this, OrderDetailActivity.class);
        Order order = mAdapter.getItem(position);
        intent.putExtra("order", order);
        startActivity(intent, true);
    }
}
