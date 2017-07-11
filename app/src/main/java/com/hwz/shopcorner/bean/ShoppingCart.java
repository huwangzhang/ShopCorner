package com.hwz.shopcorner.bean;

import java.io.Serializable;

/**
 * Created by huwang on 2017/6/21.
 */

public class ShoppingCart extends Wares implements Serializable {


    private int count;
    private boolean isChecked = true;


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

}
