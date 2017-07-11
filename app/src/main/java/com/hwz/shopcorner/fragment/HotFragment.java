package com.hwz.shopcorner.fragment;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cjj.MaterialRefreshLayout;
import com.google.gson.reflect.TypeToken;
import com.hwz.shopcorner.R;
import com.hwz.shopcorner.utils.Pager;
import com.hwz.shopcorner.adapter.HotBaseAdapter;
import com.hwz.shopcorner.bean.Page;
import com.hwz.shopcorner.bean.Wares;
import com.hwz.shopcorner.global.Constants;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * Created by huwang on 2017/6/19.
 */
@ContentView(R.layout.fragment_hot)
public class HotFragment extends BaseFragment {

//    private OkHttpHelper mOkHttpHelper = OkHttpHelper.getInstance();
//    private int mCurPage = 1;
//    private int mPageSize = 10;
//    private int mTotalPage;
    private HotBaseAdapter mAdapter;
//    private List<Wares> datas;
    @ViewInject(R.id.recyclerview)
    private RecyclerView mRecyclerView;

//    private static final int STATE_NORMAL = 0;
//    private static final int STATE_REFREH = 1;
//    private static final int STATE_MORE = 2;
//
//    private int state = STATE_NORMAL;

    @ViewInject(R.id.refresh)
    private MaterialRefreshLayout mRefreshLayout;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        initRefreshLayout();
//        getData();

        Pager pager = Pager.newBuilder().setUrl(Constants.HOT_WARES)
                .setHasMore(true)
                .setOnPageListener(new Pager.OnPageListener() {
                    @Override
                    public void load(List datas, int totalPage, int totalCount) {
                        mAdapter = new HotBaseAdapter(datas, getContext());
                        mRecyclerView.setAdapter(mAdapter);

                        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
                    }

                    @Override
                    public void refresh(List datas, int totalPage, int totalCount) {
                        mAdapter.clearData();
                        mAdapter.addData(datas);
                        mRecyclerView.scrollToPosition(0);
                    }

                    @Override
                    public void loadMore(List datas, int totalPage, int totalCount) {
                        mAdapter.addData(mAdapter.getDatas().size(), datas);
                        mRecyclerView.scrollToPosition(mAdapter.getDatas().size());
                    }
                }).setPageSize(10)
                .setRefreshLayout(mRefreshLayout)
                .builder(getContext(), new TypeToken<Page<Wares>>() {}.getType());
        pager.request();
    }

//    private void getData() {
//        String url = Constants.HOT_WARES + "?curPage=" + mCurPage + "&pageSize=" + mPageSize;
//        mOkHttpHelper.get(url, new SpotsCallback<Page<Wares>>(getContext()) {
//            @Override
//            public void onSuccess(Response response, Page<Wares> waresPage) {
//                datas = waresPage.getList();
//                mCurPage = waresPage.getCurrentPage();
//                mTotalPage = waresPage.getTotalPage();
//                showData();
//            }
//
//            @Override
//            public void onError(Response response, int code, Exception e) {
//
//            }
//        });
//    }
//
//    private void initRefreshLayout() {
//        mRefreshLayout.setLoadMore(true);
//        mRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
//            @Override
//            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
//                //refreshing...
//                refreshData();
//            }
//
//            @Override
//            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
//                //load more refreshing...
//                if (mCurPage <= mTotalPage) {
//                    loadMoreData();
//                } else {
//                    Toast.makeText(getContext(), "没有更多数据了！", Toast.LENGTH_SHORT).show();
//                    mRefreshLayout.finishRefreshLoadMore();
//                }
//            }
//        });
//    }
//
//    private void refreshData() {
//        mCurPage = 1;
//        state = STATE_REFREH;
//        getData();
//    }
//
//    private void loadMoreData() {
//        mCurPage++;
//        state = STATE_MORE;
//        getData();
//    }
//
//    private void showData() {
//        switch (state) {
//            case STATE_NORMAL:
//                mAdapter = new HotBaseAdapter(datas, getContext());
//                mRecyclerView.setAdapter(mAdapter);
//
//                mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
//                mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
//                break;
//
//            case STATE_REFREH:
//                mAdapter.clearData();
//                mAdapter.addData(datas);
//                mRecyclerView.scrollToPosition(0);
//                mRefreshLayout.finishRefresh();
////                state = STATE_NORMAL;
//                break;
//
//            case STATE_MORE:
//                mAdapter.addData(mAdapter.getDatas().size(), datas);
//                mRecyclerView.scrollToPosition(mAdapter.getDatas().size());
//                mRefreshLayout.finishRefreshLoadMore();
////                state = STATE_NORMAL;
//                break;
//        }
//
//    }
}
