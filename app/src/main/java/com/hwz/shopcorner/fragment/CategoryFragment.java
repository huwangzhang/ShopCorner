package com.hwz.shopcorner.fragment;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.hwz.shopcorner.R;
import com.hwz.shopcorner.adapter.BaseAdapter;
import com.hwz.shopcorner.adapter.CategoryAdapter;
import com.hwz.shopcorner.adapter.DividerGridItemDecoration;
import com.hwz.shopcorner.adapter.WaresAdapter;
import com.hwz.shopcorner.bean.Banner;
import com.hwz.shopcorner.bean.Category;
import com.hwz.shopcorner.bean.Page;
import com.hwz.shopcorner.bean.Wares;
import com.hwz.shopcorner.global.Constants;
import com.hwz.shopcorner.http.OkHttpHelper;
import com.hwz.shopcorner.http.SimpleCallback;
import com.hwz.shopcorner.http.SpotsCallback;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Response;

/**
 * Created by huwang on 2017/6/19.
 */
@ContentView(R.layout.fragment_category)
public class CategoryFragment extends BaseFragment {

    @ViewInject(R.id.recyclerview_category)
    private RecyclerView mRecyclerView;

    private int mCurPage = 1;
    private int mPageSize = 10;
    private int mTotalPage;

    private static final int STATE_NORMAL = 0;
    private static final int STATE_REFREH = 1;
    private static final int STATE_MORE = 2;

    private int state = STATE_NORMAL;
    private long categoryId;

    @ViewInject(R.id.recyclerview_wares)
    private RecyclerView mRecyclerviewWares;

    @ViewInject(R.id.refresh_layout)
    private MaterialRefreshLayout mRefreshLayout;

    @ViewInject(R.id.slider)
    private SliderLayout mSliderLayout;

    private CategoryAdapter mCategoryAdapter;
    private WaresAdapter mAdapter;

    private List<Banner> mBanner;

    private OkHttpHelper mHttpHelper = OkHttpHelper.getInstance();

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requestCategoryData();
        initRefreshLayout();
    }

    private void requestCategoryData() {
        mHttpHelper.get(Constants.CATEGORY_LIST, new SpotsCallback<List<Category>>(getContext()) {
            @Override
            public void onSuccess(Response response, List<Category> categories) {
                showCategoryData(categories);
                if (categories != null && categories.size() > 0) {
                    categoryId = categories.get(0).getId();
                    requestWares(categoryId);
                    requestImages();
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    private void initRefreshLayout() {
        mRefreshLayout.setLoadMore(true);
        mRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                //refreshing...
                refreshData();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                //load more refreshing...
                if (mCurPage <= mTotalPage) {
                    loadMoreData();
                } else {
                    Toast.makeText(getContext(), "没有更多数据了！", Toast.LENGTH_SHORT).show();
                    mRefreshLayout.finishRefreshLoadMore();
                }
            }
        });
    }

    private void refreshData() {
        mCurPage = 1;
        state = STATE_REFREH;
        requestWares(categoryId);
    }

    private void loadMoreData() {
        mCurPage++;
        state = STATE_MORE;
        requestWares(categoryId);
    }

    private void showCategoryData(final List<Category> categories) {
        mCategoryAdapter = new CategoryAdapter(categories, getContext());

        mCategoryAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Category category = categories.get(position);
                mCurPage = 1;
                state = STATE_NORMAL;
                categoryId = category.getId();
                requestWares(category.getId());
            }
        });
        mRecyclerView.setAdapter(mCategoryAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
    }

    private void requestWares(long categoryId) {
        requestImages();
        String url = Constants.WARES_LIST +
                "?curPage=" + mCurPage + "&pageSize=" + mPageSize + "&categoryId=" + categoryId;
        mHttpHelper.get(url, new SimpleCallback<Page<Wares>>(getContext()) {
            @Override
            public void onSuccess(Response response, Page<Wares> waresPage) {
                mCurPage = waresPage.getCurrentPage();
                mTotalPage =waresPage.getTotalPage();

                showWaresData(waresPage.getList());
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    private void showWaresData(List<Wares> wares) {
        switch (state) {
            case STATE_NORMAL:
                if (mAdapter == null) {
                    mAdapter = new WaresAdapter(wares, getContext());
                    mRecyclerviewWares.setAdapter(mAdapter);

                    mRecyclerviewWares.setLayoutManager(new GridLayoutManager(getContext(), 2));
                    mRecyclerviewWares.setItemAnimator(new DefaultItemAnimator());
                    mRecyclerviewWares.addItemDecoration(new DividerGridItemDecoration(getContext()));
                } else {
                    mAdapter.clearData();
                    mAdapter.addData(wares);
                }

                break;

            case STATE_REFREH:
                mAdapter.clearData();
                mAdapter.addData(wares);
                mRecyclerviewWares.scrollToPosition(0);
                mRefreshLayout.finishRefresh();
////                state = STATE_NORMAL;
                break;

            case STATE_MORE:
                mAdapter.addData(mAdapter.getDatas().size(), wares);
                mRecyclerviewWares.scrollToPosition(mAdapter.getDatas().size());
                mRefreshLayout.finishRefreshLoadMore();
////                state = STATE_NORMAL;
                break;
        }

    }

    private void requestImages() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", "1");
        mHttpHelper.post(Constants.BANNERS_URL, map, new SpotsCallback<List<Banner>>(getContext()) {
            @Override
            public void onSuccess(Response response, List<Banner> banners) {
                mBanner = banners;
                initSlideView();
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    private void initSlideView() {

        if (mBanner != null) {
            for (Banner banner : mBanner) {
                TextSliderView textSliderView = new TextSliderView(this.getActivity());
                textSliderView.image(banner.getImgUrl());
                textSliderView.description(banner.getName());
                textSliderView.setScaleType(BaseSliderView.ScaleType.Fit);
                mSliderLayout.addSlider(textSliderView);

            }
        }
//        mSliderLayout.setCustomIndicator(mIndicator);
        mSliderLayout.setPresetTransformer(SliderLayout.Transformer.RotateDown);
        mSliderLayout.setDuration(3000);

        mSliderLayout.addOnPageChangeListener(new ViewPagerEx.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

        @Override
    public void onStop() {
        mSliderLayout.stopAutoCycle();
        super.onStop();
    }
}
