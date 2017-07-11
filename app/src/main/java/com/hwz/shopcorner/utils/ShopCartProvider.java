package com.hwz.shopcorner.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.SparseArray;

import com.google.gson.reflect.TypeToken;
import com.hwz.shopcorner.bean.ShoppingCart;
import com.hwz.shopcorner.bean.Wares;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huwang on 2017/6/21.
 * 数据存储
 * 本地存储的
 */

public class ShopCartProvider {
    private static final String CART_JSON = "CART_JSON";
    private SparseArray<ShoppingCart> datas;
    private Context mContext;

    public ShopCartProvider(Context context) {
        datas = new SparseArray<>();
        this.mContext = context;
        listToSparse();  // 本地数据加入内存中
    }

    public void put(ShoppingCart cart) {
        ShoppingCart cart1 = datas.get(cart.getId().intValue());
        if (cart1 != null) {
            cart1.setCount(cart1.getCount() + 1);
        } else {
            cart1 = cart;
            cart1.setCount(1);
        }
        datas.put(cart1.getId().intValue(), cart1);
        commit();
    }

    public ShoppingCart convertData(Wares item) {

        ShoppingCart cart = new ShoppingCart();

        cart.setId(item.getId());
        cart.setDescription(item.getDescription());
        cart.setImgUrl(item.getImgUrl());
        cart.setName(item.getName());
        cart.setPrice(item.getPrice());

        return cart;
    }

    public void put(Wares wares) {


        ShoppingCart cart = convertData(wares);
        put(cart);
    }

    public void update(ShoppingCart cart) {
        datas.put(cart.getId().intValue(), cart);
        commit();
    }

    public void delete(ShoppingCart cart) {
        datas.delete(cart.getId().intValue());
        commit();
    }

    public void clearAll() {
        datas.removeAtRange(0, datas.size());
        commit();
    }


    public void commit() {
        List<ShoppingCart> carts = sparseToList();
        PreferencesUtils.putString(mContext, CART_JSON, JSONUtils.toJSON(carts));
    }

    private void listToSparse() {

        List<ShoppingCart> carts = getDataFromLocal();

        if (carts != null && carts.size() > 0) {

            for (ShoppingCart cart :
                    carts) {

                datas.put(cart.getId().intValue(), cart);
            }
        }

    }

    private List<ShoppingCart> sparseToList() {
        int size = datas.size();

        List<ShoppingCart> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {

            list.add(datas.valueAt(i));
        }
        return list;
    }

    public List<ShoppingCart> getAll() {
        return getDataFromLocal();
    }

    public List<ShoppingCart> getDataFromLocal() {
        List<ShoppingCart> carts = null;
        String json = PreferencesUtils.getString(mContext, CART_JSON);
        if (!TextUtils.isEmpty(json)) {
            carts = JSONUtils.fromJson(json, new TypeToken<List<ShoppingCart>>() {
            }.getType());
        }
        return carts;
    }

}
