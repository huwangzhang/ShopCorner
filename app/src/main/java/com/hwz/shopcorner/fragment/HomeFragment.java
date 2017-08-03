package com.hwz.shopcorner.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.hwz.shopcorner.R;
import com.hwz.shopcorner.WareListActivity;
import com.hwz.shopcorner.adapter.DividerItemDecortion;
import com.hwz.shopcorner.adapter.HomeCategoryAdapter;
import com.hwz.shopcorner.bean.Banner;
import com.hwz.shopcorner.bean.Campaign;
import com.hwz.shopcorner.bean.HomeCampaign;
import com.hwz.shopcorner.global.Constants;
import com.hwz.shopcorner.http.BaseCallback;
import com.hwz.shopcorner.http.OkHttpHelper;
import com.hwz.shopcorner.http.SpotsCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by huwang on 2017/6/19.
 */

public class HomeFragment extends Fragment {

    private SliderLayout mSliderShow;
    private PagerIndicator mIndicator;

    private RecyclerView mRecyclerView;

    private HomeCategoryAdapter mAdatper;

    private List<Banner> mBanner;

    private OkHttpHelper mOkHttpHelper = OkHttpHelper.getInstance();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_home, null);
        mSliderShow = (SliderLayout) view.findViewById(R.id.slider);
        mIndicator = (PagerIndicator) view.findViewById(R.id.custom_indicator);

        requestImages();

        initRecyclerView(view);
        return view;
    }

    private void requestImages() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", "1");
        mOkHttpHelper.post(Constants.BANNERS_URL, map, new SpotsCallback<List<Banner>>(getContext()) {
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

    private void initRecyclerView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);

        mOkHttpHelper.get(Constants.CAMPAIGN_HOME, new BaseCallback<List<HomeCampaign>>() {
            @Override
            public void onRequestBefore(Request request) {

            }

            @Override
            public void onFailure(Request request, Exception e) {

            }

            @Override
            public void onSuccess(Response response, List<HomeCampaign> homeCampaigns) {
                initData(homeCampaigns);
            }


            @Override
            public void onResponse(Response response) {

            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }

            @Override
            public void onTokenError(Response response, int code) {

            }
        });
    }

    private void initData(List<HomeCampaign> homeCampaigns) {
        mAdatper = new HomeCategoryAdapter(homeCampaigns, getContext());

        mAdatper.setCategoryListener(new HomeCategoryAdapter.onHomeCategoryListener() {
            @Override
            public void onClick(View view, Campaign campaign) {
                Intent intent = new Intent(getActivity(), WareListActivity.class);
                intent.putExtra(Constants.COMPAINGAIN_ID, campaign.getId());
                startActivity(intent);
            }
        });

        mRecyclerView.setAdapter(mAdatper);

        mRecyclerView.addItemDecoration(new DividerItemDecortion());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
    }


    private void initSlideView() {

        if (mBanner != null) {
            for (Banner banner : mBanner) {
                TextSliderView textSliderView = new TextSliderView(this.getActivity());
                textSliderView.image(banner.getImgUrl());
                textSliderView.description(banner.getName());
                textSliderView.setScaleType(BaseSliderView.ScaleType.Fit);
                mSliderShow.addSlider(textSliderView);
            }
        } else {
            TextSliderView textSliderView = new TextSliderView(this.getActivity());
            textSliderView.image("http://m.360buyimg.com/mobilecms/s300x98_jfs/t2416/102/20949846/13425/a3027ebc/55e6d1b9Ne6fd6d8f.jpg");
            textSliderView.description("新品推荐");
            textSliderView.setScaleType(BaseSliderView.ScaleType.Fit);
            textSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(BaseSliderView baseSliderView) {

                    Toast.makeText(HomeFragment.this.getActivity(), "新品推荐", Toast.LENGTH_LONG).show();

                }
            });


            TextSliderView textSliderView2 = new TextSliderView(this.getActivity());
            textSliderView2.image("http://m.360buyimg.com/mobilecms/s300x98_jfs/t1507/64/486775407/55927/d72d78cb/558d2fbaNb3c2f349.jpg");
            textSliderView2.description("时尚男装");
            textSliderView2.setScaleType(BaseSliderView.ScaleType.Fit);

            textSliderView2.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(BaseSliderView baseSliderView) {

                    Toast.makeText(HomeFragment.this.getActivity(), "时尚男装", Toast.LENGTH_LONG).show();

                }
            });


            TextSliderView textSliderView3 = new TextSliderView(this.getActivity());
            textSliderView3.image("http://m.360buyimg.com/mobilecms/s300x98_jfs/t1363/77/1381395719/60705/ce91ad5c/55dd271aN49efd216.jpg");
            textSliderView3.description("家电秒杀");
            textSliderView3.setScaleType(BaseSliderView.ScaleType.Fit);


            textSliderView3.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(BaseSliderView baseSliderView) {

                    Toast.makeText(HomeFragment.this.getActivity(), "家电秒杀", Toast.LENGTH_LONG).show();

                }
            });


            mSliderShow.addSlider(textSliderView);
            mSliderShow.addSlider(textSliderView2);
            mSliderShow.addSlider(textSliderView3);
        }
//        mSliderShow.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mSliderShow.setCustomIndicator(mIndicator);
        mSliderShow.setPresetTransformer(SliderLayout.Transformer.RotateDown);
        mSliderShow.setDuration(3000);

        mSliderShow.addOnPageChangeListener(new ViewPagerEx.OnPageChangeListener() {
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
        mSliderShow.stopAutoCycle();
        super.onStop();
    }
}
