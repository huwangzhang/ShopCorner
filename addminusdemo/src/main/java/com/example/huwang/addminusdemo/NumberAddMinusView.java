package com.example.huwang.addminusdemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by huwang on 2017/6/21.
 */

public class NumberAddMinusView extends LinearLayout implements View.OnClickListener {
    private LayoutInflater mInflater;
    private Button mBtnAdd;
    private Button mBtnMinus;
    private TextView mTvNumber;

    private int value;
    private int minValue;
    private int maxValue;

    private onButtonClickListener mListener;

    public NumberAddMinusView(Context context) {
        this(context, null);
    }

    public NumberAddMinusView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NumberAddMinusView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mInflater = LayoutInflater.from(context);

        initView();

        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NumberAddMinusView, defStyleAttr, defStyleAttr);
            int val = typedArray.getInt(R.styleable.NumberAddMinusView_value, 0);
            setValue(val);
            int minVal = typedArray.getInt(R.styleable.NumberAddMinusView_minvalue, 0);
            setMinValue(minVal);
            int maxVal = typedArray.getInt(R.styleable.NumberAddMinusView_maxvalue, 0);
            setMaxValue(maxVal);

            Drawable drawableBtnAdd =typedArray.getDrawable(R.styleable.NumberAddMinusView_btnAddBackgroud);
            Drawable drawableBtnSub =typedArray.getDrawable(R.styleable.NumberAddMinusView_btnMinusBackgroud);
            Drawable drawableTextView =typedArray.getDrawable(R.styleable.NumberAddMinusView_btnTvNumberBackgroud);

            setButtonAddBackgroud(drawableBtnAdd);
            setButtonSubBackgroud(drawableBtnSub);
            setTexViewtBackground(drawableTextView);

            typedArray.recycle();
        }
    }

    private void initView() {
        View view = mInflater.inflate(R.layout.number_add_minus, this, true);
        mBtnAdd = (Button) view.findViewById(R.id.btn_add);
        mTvNumber = (TextView) view.findViewById(R.id.tv_number);
        mBtnMinus = (Button) view.findViewById(R.id.btn_minus);
        mBtnMinus.setOnClickListener(this);
        mBtnAdd.setOnClickListener(this);
    }

    public int getValue() {
        String valueStr = mTvNumber.getText().toString();
        if (TextUtils.isEmpty(valueStr)) {
            return value;
        }
        this.value = Integer.parseInt(valueStr);
        return value;
    }

    public void setValue(int value) {
        mTvNumber.setText(value + "");
        this.value = value;
    }

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_add:
                numberAdd();
                if (mListener != null) {
                    mListener.onButtonAddClick(v, value);
                }
                break;
            case R.id.btn_minus:
                numberMinus();
                if (mListener != null) {
                    mListener.onButtonMinusClick(v, value);
                }
                break;
            default:
                break;
        }

    }

    private void numberAdd() {
        if (value < maxValue) {
            value++;
        }

        mTvNumber.setText(value + "");
    }

    private void numberMinus() {
        if (value > minValue) {
            value--;
        }

        mTvNumber.setText(value + "");
    }

    public void setOnButtonClickListener(onButtonClickListener listener) {
        mListener = listener;
    }

    public interface onButtonClickListener {
        void onButtonAddClick(View view, int value);

        void onButtonMinusClick(View view, int value);
    }

    public void setTexViewtBackground(Drawable drawable){

        mTvNumber.setBackgroundDrawable(drawable);

    }

    public void setTextViewBackground(int drawableId){

        setTexViewtBackground(getResources().getDrawable(drawableId));

    }


    public void setButtonAddBackgroud(Drawable backgroud){
        this.mBtnAdd.setBackgroundDrawable(backgroud);
    }


    public void setButtonSubBackgroud(Drawable backgroud){
        this.mBtnMinus.setBackgroundDrawable(backgroud);
    }
}
