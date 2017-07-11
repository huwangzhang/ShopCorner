package com.hwz.shopcorner.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.TintTypedArray;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hwz.shopcorner.R;

/**
 * Created by huwang on 2017/6/19.
 */

public class MyToolBar extends Toolbar {
    private LayoutInflater mInflater;

    private View mView;
    private TextView mTextTitle;
    private EditText mSearchView;
    private Button mRightButton;

    public MyToolBar(Context context) {
        this(context, null);
    }

    public MyToolBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyToolBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews();
        setContentInsetsRelative(10, 10);

        if (attrs != null) {
            final TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(), attrs,
                    R.styleable.MyToolBar, defStyleAttr, 0);


            final Drawable rightIcon = a.getDrawable(R.styleable.MyToolBar_rightButtonIcon);
            if (rightIcon != null) {
                setRightButtonIcon(rightIcon);
            }


            boolean isShowSearchView = a.getBoolean(R.styleable.MyToolBar_isShowSearchView, false);

            if (isShowSearchView) {
                showSearchView();
                hideTitleView();
            }

            CharSequence rightButtonText = a.getText(R.styleable.MyToolBar_rightButtonText);
            if (rightButtonText != null) {
                setRightButtonText(rightButtonText);
            }


            a.recycle();
        }
    }


    public void setRightButtonIcon(Drawable icon) {
        if (mRightButton != null) {

            mRightButton.setBackgroundDrawable(icon);
            mRightButton.setVisibility(VISIBLE);
        }
    }

    public void setRightButtonIcon(int resId) {
        if (mRightButton != null) {
            mRightButton.setBackgroundResource(resId);
            mRightButton.setVisibility(VISIBLE);
        }
    }


    public void setRightButtonOnClickListener(OnClickListener li) {

        mRightButton.setOnClickListener(li);
    }

    public void setRightButtonText(int id) {
        setRightButtonText(getResources().getString(id));
    }

    public void setRightButtonText(CharSequence text) {
        mRightButton.setText(text);
        mRightButton.setVisibility(VISIBLE);
    }

    private void initViews() {
        if (mView == null) {
            mInflater = LayoutInflater.from(getContext());
            mView = mInflater.inflate(R.layout.toolbar, null);

            mTextTitle = (TextView) mView.findViewById(R.id.toolbar_title);
            mSearchView = (EditText) mView.findViewById(R.id.toolbar_searchview);
            mRightButton = (Button) mView.findViewById(R.id.toolbar_rightButton);


            LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL);

            addView(mView, lp);
        }
    }

    @Override
    public void setTitle(int resId) {
        setTitle(getContext().getText(resId));
    }

    @Override
    public void setTitle(CharSequence title) {
        initViews();
        if (mTextTitle != null) {
            mTextTitle.setText(title);
            showTitleView();
        }
    }

    public void showSearchView() {

        if (mSearchView != null)
            mSearchView.setVisibility(VISIBLE);

    }


    public void hideSearchView() {
        if (mSearchView != null)
            mSearchView.setVisibility(GONE);
    }

    public void showTitleView() {
        if (mTextTitle != null)
            mTextTitle.setVisibility(VISIBLE);
    }


    public void hideTitleView() {
        if (mTextTitle != null)
            mTextTitle.setVisibility(GONE);

    }

    public Button getRightButton() {

        return this.mRightButton;
    }
}
