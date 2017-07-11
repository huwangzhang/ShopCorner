package com.hwz.shopcorner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.paysdk.ui.PayResultActivity;
import com.hwz.shopcorner.adapter.WareOrderAdapter;
import com.hwz.shopcorner.adapter.layoutmanager.FullyLinearLayoutManager;
import com.hwz.shopcorner.bean.Charge;
import com.hwz.shopcorner.bean.ShoppingCart;
import com.hwz.shopcorner.global.Constants;
import com.hwz.shopcorner.global.SCApplication;
import com.hwz.shopcorner.http.OkHttpHelper;
import com.hwz.shopcorner.http.SpotsCallback;
import com.hwz.shopcorner.msg.BaseRespMsg;
import com.hwz.shopcorner.msg.CreateOrderRespMsg;
import com.hwz.shopcorner.utils.JSONUtils;
import com.hwz.shopcorner.utils.ShopCartProvider;
import com.hwz.shopcorner.widget.MyToolBar;
import com.pingplusplus.android.Pingpp;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Response;

/**
 * Created by huwang on 2017/6/23.
 */

@ContentView(R.layout.activity_create_order)
public class CreateOrderActivity extends BaseActivity implements View.OnClickListener {
    /**
     * 银联支付渠道
     */
    private static final String CHANNEL_UPACP = "upacp";
    /**
     * 微信支付渠道
     */
    private static final String CHANNEL_WECHAT = "wx";
    /**
     * 支付支付渠道
     */
    private static final String CHANNEL_ALIPAY = "alipay";
    /**
     * 百度支付渠道
     */
    private static final String CHANNEL_BFB = "bfb";
    /**
     * 京东支付渠道
     */
    private static final String CHANNEL_JDPAY_WAP = "jdpay_wap";


    @ViewInject(R.id.toolbar)
    private MyToolBar mMyToolBar;

    @ViewInject(R.id.txt_order)
    private TextView txtOrder;

    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerView;


    @ViewInject(R.id.rl_alipay)
    private RelativeLayout mLayoutAlipay;

    @ViewInject(R.id.rl_wechat)
    private RelativeLayout mLayoutWechat;

    @ViewInject(R.id.rl_bd)
    private RelativeLayout mLayoutBd;


    @ViewInject(R.id.rb_alipay)
    private RadioButton mRbAlipay;

    @ViewInject(R.id.rb_webchat)
    private RadioButton mRbWechat;

    @ViewInject(R.id.rb_bd)
    private RadioButton mRbBd;

    @ViewInject(R.id.btn_createOrder)
    private Button mBtnCreateOrder;

    @ViewInject(R.id.txt_total)
    private TextView mTxtTotal;


    private ShopCartProvider cartProvider;

    private WareOrderAdapter mAdapter;


    private OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();

    private String orderNum;
    private String payChannel = CHANNEL_ALIPAY;
    private float amount;


    private HashMap<String, RadioButton> channels = new HashMap<>(3);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showData();

        init();
    }

    private void init() {
        mMyToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateOrderActivity.this.finish();
            }
        });

        channels.put(CHANNEL_ALIPAY, mRbAlipay);
        channels.put(CHANNEL_WECHAT, mRbWechat);
        channels.put(CHANNEL_BFB, mRbBd);

        mLayoutAlipay.setOnClickListener(this);
        mLayoutWechat.setOnClickListener(this);
        mLayoutBd.setOnClickListener(this);


        amount = mAdapter.getTotalPrice();
        mTxtTotal.setText("应付款： ￥" + amount);
    }


    public void showData() {
        cartProvider = new ShopCartProvider(this);
        mAdapter = new WareOrderAdapter(this, cartProvider.getAll());

        FullyLinearLayoutManager layoutManager = new FullyLinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setAdapter(mAdapter);
    }


    @Override
    public void onClick(View v) {
        selectPayChannel(v.getTag().toString());
    }


    public void selectPayChannel(String paychannel) {
        payChannel = paychannel;
        for (Map.Entry<String, RadioButton> entry : channels.entrySet()) {
            RadioButton rb = entry.getValue();
            if (entry.getKey().equals(paychannel)) {
                boolean isCheck = rb.isChecked();
                rb.setChecked(!isCheck);
            } else
                rb.setChecked(false);
        }
    }


    @Event(R.id.btn_createOrder)
    private void createNewOrder(View view) {
        postNewOrder();
    }


    private void postNewOrder() {


        final List<ShoppingCart> carts = mAdapter.getDatas();

        List<WareItem> items = new ArrayList<>(carts.size());
        for (ShoppingCart c : carts) {

            WareItem item = new WareItem(c.getId(), c.getPrice().intValue());
            items.add(item);

        }

        String item_json = JSONUtils.toJSON(items);

        Map<String, Object> params = new HashMap<>(5);
        params.put("user_id", SCApplication.getInstance().getUser().getId() + "");
        params.put("item_json", item_json);
        params.put("pay_channel", payChannel);
        params.put("amount", (int) amount + "");
        params.put("addr_id", 1 + "");


        mBtnCreateOrder.setEnabled(false);

        okHttpHelper.post(Constants.ORDER_CREATE, params, new SpotsCallback<CreateOrderRespMsg>(this) {
            @Override
            public void onSuccess(Response response, CreateOrderRespMsg respMsg) {

                cartProvider.clearAll();

                mBtnCreateOrder.setEnabled(true);
                // 没有权限，返回空信息
                orderNum = respMsg.getData().getOrderNum();
                Charge charge = respMsg.getData().getCharge();

                openPaymentActivity(JSONUtils.toJSON(charge));
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                mBtnCreateOrder.setEnabled(true);
            }
        });


    }


    private void openPaymentActivity(String charge) {
        if (null == charge) {
            showMsg("请求出错", "请检查URL", "URL无法获取charge");
            return;
        }
        Log.d("charge", charge);

        Pingpp.createPayment(this, charge);
//        Intent intent = new Intent();
//        String packageName = getPackageName();
//        ComponentName componentName = new ComponentName(packageName, packageName + ".wxapi.WXPayEntryActivity");
//        intent.setComponent(componentName);
//        intent.putExtra(PaymentActivity.EXTRA_CHARGE, charge);
//        startActivityForResult(intent, Constants.REQUEST_CODE_PAYMENT);
    }


    public void showMsg(String title, String msg1, String msg2) {
        String str = title;
        if (null != msg1 && msg1.length() != 0) {
            str += "\n" + msg1;
        }
        if (null != msg2 && msg2.length() != 0) {
            str += "\n" + msg2;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(str);
        builder.setTitle("提示");
        builder.setPositiveButton("OK", null);
        builder.create().show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //支付页面返回处理
        if (requestCode == Pingpp.REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getExtras().getString("pay_result");
                /* 处理返回值
                 * "success" - payment succeed
                 * "fail"    - payment failed
                 * "cancel"  - user canceld
                 * "invalid" - payment plugin not installed
                 */
                if (result.equals("success"))
                    changeOrderStatus(1);
                else if (result.equals("fail"))
                    changeOrderStatus(-1);
                else if (result.equals("cancel"))
                    changeOrderStatus(-2);
                else
                    changeOrderStatus(0);
//                String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
//                String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息
//                showMsg(result, errorMsg, extraMsg);
            }
        }
    }


    private void changeOrderStatus(final int status) {

        Map<String, Object> params = new HashMap<>(5);
        params.put("order_num", orderNum);
        params.put("status", status + "");


        okHttpHelper.post(Constants.ORDER_COMPLEPE, params, new SpotsCallback<BaseRespMsg>(this) {
            @Override
            public void onSuccess(Response response, BaseRespMsg o) {

                toPayResultActivity(status);
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                toPayResultActivity(-1);
            }
        });

    }


    private void toPayResultActivity(int status) {
        Intent intent = new Intent(this, PayResultActivity.class);
        intent.putExtra("status", status);

        startActivity(intent);
        this.finish();
    }


    class WareItem {
        private Long ware_id;
        private int amount;

        public WareItem(Long ware_id, int amount) {
            this.ware_id = ware_id;
            this.amount = amount;
        }

        public Long getWare_id() {
            return ware_id;
        }

        public void setWare_id(Long ware_id) {
            this.ware_id = ware_id;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }
    }
}
