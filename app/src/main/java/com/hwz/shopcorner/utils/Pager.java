package com.hwz.shopcorner.utils;

import android.content.Context;
import android.text.TextUtils;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.hwz.shopcorner.bean.Page;
import com.hwz.shopcorner.http.OkHttpHelper;
import com.hwz.shopcorner.http.SpotsCallback;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Response;

/**
 * Created by huwang on 2017/6/21.
 */

public class Pager {

    private static Builder sBuilder;

    private OkHttpHelper mOkHttpHelper;
    private static final int STATE_NORMAL = 0;
    private static final int STATE_REFREH = 1;
    private static final int STATE_MORE = 2;

    private int state = STATE_NORMAL;


    private Pager() {
        mOkHttpHelper = OkHttpHelper.getInstance();
        initRefreshLayout();
    }

    public void request() {
        requestData();
    }

    public void putParam(String key, Object object) {
        sBuilder.params.put(key, object);
    }


    public static Builder newBuilder() {
        sBuilder = new Builder();
        return sBuilder;
    }

    private void initRefreshLayout() {
        sBuilder.mRefreshLayout.setLoadMore(sBuilder.hasMore);
        sBuilder.mRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                //refreshing...
                refreshData();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                //load more refreshing...
                if (sBuilder.mCurPage <= sBuilder.mTotalPage) {
                    loadMoreData();
                } else {
                    ToastUtils.show(sBuilder.mContext, "没有更多数据了！");
                    sBuilder.mRefreshLayout.finishRefreshLoadMore();
                }
            }
        });
    }

    private void refreshData() {
        sBuilder.mCurPage = 1;
        state = STATE_REFREH;
        requestData();
    }

    private void loadMoreData() {
        sBuilder.mCurPage++;
        state = STATE_MORE;
        requestData();
    }

    private String buildUrl() {
        return sBuilder.url + "?" + buildUrlParams();
    }

    private String buildUrlParams() {


        HashMap<String, Object> map = sBuilder.params;

        map.put("curPage", sBuilder.mCurPage);
        map.put("pageSize", sBuilder.mPageSize);

        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            sb.append(entry.getKey() + "=" + entry.getValue());
            sb.append("&");
        }
        String s = sb.toString();
        if (s.endsWith("&")) {
            s = s.substring(0, s.length() - 1);
        }
        return s;
    }

    private void requestData() {
        String url = buildUrl();
        mOkHttpHelper.get(url, new RequestCallBack(sBuilder.mContext));
    }

    private <T> void showData(List<T> datas, int totalPage, int totalCount) {
        switch (state) {
            case STATE_NORMAL:
                if (sBuilder.mListener != null) {
                    sBuilder.mListener.load(datas, totalPage, totalCount);
                }
                break;

            case STATE_REFREH:
                if (sBuilder.mListener != null) {
                    sBuilder.mListener.refresh(datas, totalPage, totalCount);
                }
                sBuilder.mRefreshLayout.finishRefresh();
                break;

            case STATE_MORE:
                if (sBuilder.mListener != null) {
                    sBuilder.mListener.loadMore(datas, totalPage, totalCount);
                }
                sBuilder.mRefreshLayout.finishRefreshLoadMore();
                break;
        }

    }

    public static class Builder {
        private Type mType;
        private String url;
        private Context mContext;
        private HashMap<String, Object> params = new HashMap<String, Object>();
        private int mCurPage = 0;
        private int mPageSize = 10;
        private int mTotalPage = 1;
        private MaterialRefreshLayout mRefreshLayout;
        private boolean hasMore;
        private OnPageListener mListener;

        public Builder setOnPageListener(OnPageListener listener) {
            this.mListener = listener;
            return sBuilder;
        }

        public Builder setHasMore(boolean hasMore) {
            this.hasMore = hasMore;
            return sBuilder;
        }

        public Builder setUrl(String url) {
            this.url = url;
            return sBuilder;
        }

        public Builder setPageSize(int pageSize) {
            this.mPageSize = pageSize;
            return sBuilder;
        }

        public Builder setRefreshLayout(MaterialRefreshLayout materialRefreshLayout) {
            this.mRefreshLayout = materialRefreshLayout;
            return sBuilder;
        }

        public Builder putParams(String key, Object object) {
            params.put(key, object);
            return sBuilder;
        }

        public Pager builder(Context context,  Type type) {
            this.mContext = context;
            this.mType = type;
            valid();
            return new Pager();
        }

        private void valid() {
            if (this.mContext == null)
                throw new RuntimeException("content can't be null");
            if (TextUtils.isEmpty(this.url))
                throw new RuntimeException("url can't be  null");
            if (this.mRefreshLayout == null)
                throw new RuntimeException("MaterialRefreshLayout can't be  null");
        }
    }

    public interface OnPageListener<T> {
        void load(List<T> datas, int totalPage, int totalCount);

        void refresh(List<T> datas, int totalPage, int totalCount);

        void loadMore(List<T> datas, int totalPage, int totalCount);
    }

    class RequestCallBack<T> extends SpotsCallback<Page<T>> {

        public RequestCallBack(Context context) {
            super(context);
            super.mType = sBuilder.mType;
        }

        @Override
        public void onSuccess(Response response, Page<T> page) {
            sBuilder.mCurPage = page.getCurrentPage();
            sBuilder.mTotalPage = page.getTotalPage();
            showData(page.getList(), sBuilder.mTotalPage, page.getTotalCount());
        }

        @Override
        public void onError(Response response, int code, Exception e) {

        }
    }

}
