package com.hwz.shopcorner;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hwz.shopcorner.adapter.BaseAdapter;
import com.hwz.shopcorner.adapter.CardViewtemDecortion;
import com.hwz.shopcorner.adapter.FavoriteAdatper;
import com.hwz.shopcorner.bean.Favorites;
import com.hwz.shopcorner.global.Constants;
import com.hwz.shopcorner.global.SCApplication;
import com.hwz.shopcorner.http.OkHttpHelper;
import com.hwz.shopcorner.http.SpotsCallback;
import com.hwz.shopcorner.widget.MyToolBar;

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

@ContentView(R.layout.activity_my_favorite)
public class MyFavoriteActivity extends BaseActivity {
    @ViewInject(R.id.toolbar)
    private MyToolBar mToolbar;

    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerview;

    private FavoriteAdatper mAdapter;

    private OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolBar();
        getFavorites();
    }

    private void initToolBar() {

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getFavorites() {

        Long userId = SCApplication.getInstance().getUser().getId();

        Map<String, Object> params = new HashMap<>();
        params.put("user_id", userId);

        okHttpHelper.get(Constants.FAVORITE_LIST, params, new SpotsCallback<List<Favorites>>(this) {
            @Override
            public void onSuccess(Response response, List<Favorites> favorites) {
                showFavorites(favorites);
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                LogUtil.d("code:" + code);
            }
        });
    }

    private void showFavorites(List<Favorites> favorites) {

        mAdapter = new FavoriteAdatper(this, favorites);
        mRecyclerview.setAdapter(mAdapter);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerview.addItemDecoration(new CardViewtemDecortion());

        mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {

            }
        });
    }
}
