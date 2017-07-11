package com.hwz.shopcorner;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.google.gson.reflect.TypeToken;
import com.hwz.shopcorner.adapter.BaseAdapter;
import com.hwz.shopcorner.adapter.HotBaseAdapter;
import com.hwz.shopcorner.bean.Page;
import com.hwz.shopcorner.bean.Wares;
import com.hwz.shopcorner.global.Constants;
import com.hwz.shopcorner.utils.Pager;
import com.hwz.shopcorner.widget.MyToolBar;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * Created by huwang on 2017/6/21.
 */

@ContentView(R.layout.activity_warelist)
public class WareListActivity extends BaseActivity implements Pager.OnPageListener<Wares>, TabLayout.OnTabSelectedListener {
    private static final String TAG = "WareListActivity";

    public static final int TAG_DEFAULT = 0;
    public static final int TAG_SALE = 1;
    public static final int TAG_PRICE = 2;

    public static final int ACTION_LIST = 1;
    public static final int ACTION_GIRD = 2;

    @ViewInject(R.id.tab_layout)
    private TabLayout mTablayout;

    @ViewInject(R.id.txt_summary)
    private TextView mTxtSummary;


    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerview_wares;

    @ViewInject(R.id.refresh_layout)
    private MaterialRefreshLayout mRefreshLayout;

    @ViewInject(R.id.toolbar)
    private MyToolBar mToolbar;


    private int orderBy = 0;
    private long campaignId = 0;


    private HotBaseAdapter mWaresAdapter;


    private Pager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        campaignId = getIntent().getLongExtra(Constants.COMPAINGAIN_ID, 0);
        mRecyclerview_wares.setHasFixedSize(false);
        initToolBar();
        initTab();
        getData();
    }

    private void initToolBar() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WareListActivity.this.finish();
            }
        });

        mToolbar.setRightButtonIcon(R.drawable.icon_grid_32);
        mToolbar.getRightButton().setTag(ACTION_LIST);

        mToolbar.getRightButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int action = (int) v.getTag();
                if (action == ACTION_GIRD) {
                    mToolbar.setRightButtonIcon(R.drawable.icon_grid_32);
                    mToolbar.getRightButton().setTag(ACTION_LIST);
                    mRecyclerview_wares.setLayoutManager(new LinearLayoutManager(WareListActivity.this));
                    mWaresAdapter.resetLayout(R.layout.template_hot_wares);
                } else if (action == ACTION_LIST) {
                    mToolbar.setRightButtonIcon(R.drawable.icon_list_32);
                    mToolbar.getRightButton().setTag(ACTION_GIRD);
                    mRecyclerview_wares.setLayoutManager(new GridLayoutManager(WareListActivity.this, 2));
                    mWaresAdapter.resetLayout(R.layout.template_grid_wares);
                }
            }
        });
    }


    private void getData() {
        pager = Pager.newBuilder()
                .setUrl(Constants.WARES_CAMPAIN_LIST)
                .putParams("campaignId", campaignId)
                .putParams("orderBy", orderBy)
                .setRefreshLayout(mRefreshLayout)
                .setHasMore(true)
                .setOnPageListener(this)
                .builder(this, new TypeToken<Page<Wares>>() {
                }.getType());
        pager.request();
    }


    private void initTab() {
        TabLayout.Tab tab = mTablayout.newTab();
        tab.setText("默认");
        tab.setTag(TAG_DEFAULT);
        mTablayout.addTab(tab);

        TabLayout.Tab tab1 = mTablayout.newTab();
        tab1.setText("销量");
        tab1.setTag(TAG_SALE);
        mTablayout.addTab(tab1);

        TabLayout.Tab tab2 = mTablayout.newTab();
        tab2.setText("价格");
        tab2.setTag(TAG_PRICE);
        mTablayout.addTab(tab2);

        mTablayout.setOnTabSelectedListener(this);
    }

    @Override
    public void load(List<Wares> datas, int totalPage, int totalCount) {
        mTxtSummary.setText("共有" + totalCount + "商品");

        if (mWaresAdapter == null) {
            mWaresAdapter = new HotBaseAdapter(datas, this);
            mWaresAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
                @Override
                public void onClick(View view, int position) {
                    Wares wares = mWaresAdapter.getItem(position);

                    Intent intent = new Intent(WareListActivity.this, WareDetailActivity.class);
                    intent.putExtra(Constants.WARE, wares);
                    startActivity(intent);
                }
            });
            mRecyclerview_wares.setAdapter(mWaresAdapter);
            mRecyclerview_wares.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerview_wares.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
            mRecyclerview_wares.setItemAnimator(new DefaultItemAnimator());
        } else {
            mWaresAdapter.refreshData(datas);
//            mWaresAdapter.clearData();
//            mWaresAdapter.addData(datas);
//            mRecyclerview_wares.scrollToPosition(0);
        }
    }

    @Override
    public void refresh(List<Wares> datas, int totalPage, int totalCount) {
//        mWaresAdapter.clearData();
//        mWaresAdapter.addData(datas);
//        mRecyclerview_wares.scrollToPosition(0);
        mWaresAdapter.refreshData(datas);
        mRecyclerview_wares.scrollToPosition(0);
    }

    @Override
    public void loadMore(List<Wares> datas, int totalPage, int totalCount) {
//        mWaresAdapter.addData(mWaresAdapter.getDatas().size(), datas);
//        mRecyclerview_wares.scrollToPosition(mWaresAdapter.getDatas().size());
        mWaresAdapter.loadMoreData(datas);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        orderBy = (int) tab.getTag();
        pager.putParam("orderBy", orderBy);
        pager.request();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
