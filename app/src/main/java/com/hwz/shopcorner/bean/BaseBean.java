package com.hwz.shopcorner.bean;

import java.io.Serializable;

/**
 * Created by huwang on 2017/6/19.
 */
public class BaseBean implements Serializable {


    protected long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
