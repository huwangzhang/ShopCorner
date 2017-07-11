package com.hwz.shopcorner;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.hwz.shopcorner.bean.Tab;
import com.hwz.shopcorner.fragment.CartFragment;
import com.hwz.shopcorner.fragment.CategoryFragment;
import com.hwz.shopcorner.fragment.HomeFragment;
import com.hwz.shopcorner.fragment.HotFragment;
import com.hwz.shopcorner.fragment.MineFragment;
import com.hwz.shopcorner.widget.FragmentTabHost;
import com.hwz.shopcorner.widget.MyToolBar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private FragmentTabHost mTabHost;
    private LayoutInflater mInflater;
    private MyToolBar mToolbar;
    private List<Tab> mTabs;
    private CartFragment cartFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mInflater = LayoutInflater.from(this);
        initToolBar();
        initTab();
    }

    private void initToolBar() {
        mToolbar = (MyToolBar) findViewById(R.id.toolBar);
    }

    private void initTab() {
        mTabs = new ArrayList<>();
        Tab tab_home = new Tab(R.string.home, R.drawable.selector_icon_home, HomeFragment.class);
        Tab tab_hot = new Tab(R.string.hot, R.drawable.selector_icon_hot, HotFragment.class);
        Tab tab_category = new Tab(R.string.catagory, R.drawable.selector_icon_category, CategoryFragment.class);
        Tab tab_cart = new Tab(R.string.cart, R.drawable.selector_icon_cart, CartFragment.class);
        Tab tab_mine = new Tab(R.string.mine, R.drawable.selector_icon_mine, MineFragment.class);

        mTabs.add(tab_home);
        mTabs.add(tab_hot);
        mTabs.add(tab_category);
        mTabs.add(tab_cart);
        mTabs.add(tab_mine);

        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.tab_content);

        for (Tab tab : mTabs) {
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(getString(tab.getTitle()));
            tabSpec.setIndicator(buildIndicator(tab));
            mTabHost.addTab(tabSpec, tab.getFragment(), null);
        }

        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (tabId.equals(getString(R.string.mine))) {
                    mToolbar.setVisibility(View.GONE);
                } else {
                    mToolbar.setVisibility(View.VISIBLE);
                }
                if (tabId.equals(getString(R.string.cart))) {
                    refreshCartData();
                } else {
                    mToolbar.showSearchView();
                    mToolbar.hideTitleView();
                    mToolbar.getRightButton().setVisibility(View.GONE);
                }
            }
        });
        mTabHost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
        mTabHost.setCurrentTab(0);
    }

    private void refreshCartData() {
        if (cartFragment != null) {
            cartFragment.refreshData();
            cartFragment.changeToolBar();
        } else {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(getString(R.string.cart));
            if (fragment != null) {
                cartFragment = (CartFragment) fragment;
                cartFragment.refreshData();
                cartFragment.changeToolBar();
            }
        }
    }

    private View buildIndicator(Tab tab) {
        View view = mInflater.inflate(R.layout.tab_indicator, null);
        ImageView img = (ImageView) view.findViewById(R.id.icon_tab);
        TextView text = (TextView) view.findViewById(R.id.txt_tab);
        img.setBackgroundResource(tab.getIcon());
        text.setText(tab.getTitle());
        return view;
    }
}
