package com.hwz.shopcorner;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.hwz.shopcorner.adapter.WareOrderAdapter;
import com.hwz.shopcorner.adapter.layoutmanager.FullyLinearLayoutManager;
import com.hwz.shopcorner.utils.ShopCartProvider;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by huwang on 2017/6/23.
 */

@ContentView(R.layout.activity_test)
public class TestActivity extends BaseActivity {

    private ShopCartProvider cartProvider;
    private WareOrderAdapter mAdapter;

    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showData();

    }

    public void showData() {
        cartProvider = new ShopCartProvider(this);
        mAdapter = new WareOrderAdapter(this, cartProvider.getAll());

        FullyLinearLayoutManager layoutManager = new FullyLinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setAdapter(mAdapter);
    }
}
