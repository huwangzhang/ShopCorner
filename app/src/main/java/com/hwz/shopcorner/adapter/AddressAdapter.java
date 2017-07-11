package com.hwz.shopcorner.adapter;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.hwz.shopcorner.R;
import com.hwz.shopcorner.bean.Address;

import java.util.List;


/**
 * Created by <a href="http://www.cniao5.com">菜鸟窝</a>
 * 一个专业的Android开发在线教育平台
 */
public class AddressAdapter extends SimpleAdapter<Address> {


    private AddressListener mListener;

    public AddressAdapter(Context context, List<Address> datas, AddressListener listener) {
        super(datas, context, R.layout.template_address);

        this.mListener = listener;


    }


    @Override
    public void bindData(BaseViewHolder viewHoder, final Address item) {

        viewHoder.getTextView(R.id.txt_name).setText(item.getConsignee());
        viewHoder.getTextView(R.id.txt_phone).setText(replacePhoneNum(item.getPhone()));
        viewHoder.getTextView(R.id.txt_address).setText(item.getAddr());

        final CheckBox checkBox = viewHoder.getCheckBox(R.id.cb_is_defualt);

        final boolean isDefault = item.getIsDefault();
        checkBox.setChecked(isDefault);


        if (isDefault) {
            checkBox.setText("默认地址");
        } else {

            checkBox.setClickable(true);

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (isChecked && mListener != null) {

                        item.setIsDefault(true);
                        mListener.setDefault(item);
                    }
                }
            });


        }


    }


    public String replacePhoneNum(String phone) {

        return phone.substring(0, phone.length() - (phone.substring(3)).length()) + "****" + phone.substring(7);
    }


    public interface AddressListener {
        void setDefault(Address address);
    }
}
