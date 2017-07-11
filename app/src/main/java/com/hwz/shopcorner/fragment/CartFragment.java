package com.hwz.shopcorner.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.hwz.shopcorner.CreateOrderActivity;
import com.hwz.shopcorner.MainActivity;
import com.hwz.shopcorner.R;
import com.hwz.shopcorner.adapter.CartAdapter;
import com.hwz.shopcorner.bean.ShoppingCart;
import com.hwz.shopcorner.http.OkHttpHelper;
import com.hwz.shopcorner.utils.ShopCartProvider;
import com.hwz.shopcorner.widget.MyToolBar;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * Created by huwang on 2017/6/19.
 */
@ContentView(R.layout.fragment_cart)
public class CartFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = "CartFragment";
    private Context mContext;
    public static final int ACTION_EDIT = 1;
    public static final int ACTION_COMPLETE = 2;


    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerView;

    @ViewInject(R.id.checkbox_all)
    private CheckBox mCheckBox;

    @ViewInject(R.id.txt_total)
    private TextView mTextTotal;

    @ViewInject(R.id.btn_order)
    private Button mBtnOrder;

    @ViewInject(R.id.btn_del)
    private Button mBtnDel;

    private MyToolBar mToolbar;

    private CartAdapter mAdapter;
    private ShopCartProvider cartProvider;

    private OkHttpHelper mOkHttpHelper = OkHttpHelper.getInstance();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        changeToolBar();
        cartProvider = new ShopCartProvider(getContext());
        showData();
    }

    private void showData() {
        List<ShoppingCart> carts = cartProvider.getAll();
        mAdapter = new CartAdapter(carts, getContext(), mCheckBox, mTextTotal);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
    }

    public void refreshData() {
        mAdapter.clearData();
        List<ShoppingCart> carts = cartProvider.getAll();
        mAdapter.addData(carts);
        mAdapter.showTotalPrice();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        if (context instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) context;
            mToolbar = (MyToolBar) mainActivity.findViewById(R.id.toolBar);
        }
    }

    public void changeToolBar() {
        mToolbar.hideSearchView();
        mToolbar.showTitleView();
        mToolbar.setTitle(R.string.cart);
        mToolbar.getRightButton().setVisibility(View.VISIBLE);
        mToolbar.getRightButton().setText("编辑");
        mToolbar.getRightButton().setOnClickListener(this);
        mToolbar.getRightButton().setTag(ACTION_EDIT);
    }

    @Event(value = R.id.btn_del, type = View.OnClickListener.class)
    private void delCart(View view) { // 私有方法，共有的不起作用
        mAdapter.delCart();
    }

    @Override
    public void onClick(View v) {
        int action = (int) v.getTag();
        switch (action) {
            case ACTION_EDIT:
                showDelBtn();
                break;
            case ACTION_COMPLETE:
                hideDelBtn();
                break;
            default:
                break;
        }
    }

    private void hideDelBtn() {
        mToolbar.getRightButton().setText("编辑");
        mTextTotal.setVisibility(View.VISIBLE);
        mBtnOrder.setVisibility(View.VISIBLE);
        mBtnDel.setVisibility(View.GONE);
        mToolbar.getRightButton().setTag(ACTION_EDIT);
        mAdapter.checkAll_None(true);
        mCheckBox.setChecked(true);
    }

    private void showDelBtn() {
        mToolbar.getRightButton().setText("完成");
        mTextTotal.setVisibility(View.GONE);
        mBtnOrder.setVisibility(View.GONE);
        mBtnDel.setVisibility(View.VISIBLE);
        mToolbar.getRightButton().setTag(ACTION_COMPLETE);

        mAdapter.checkAll_None(false);
        mCheckBox.setChecked(false);
    }

    @Event(value = R.id.btn_order)
    private void toOrder(View view) {
        Intent intent = new Intent(getActivity(), CreateOrderActivity.class);
        startActivity(intent, true);
    }
}
