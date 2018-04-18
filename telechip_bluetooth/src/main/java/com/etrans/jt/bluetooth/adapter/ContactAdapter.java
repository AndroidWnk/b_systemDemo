package com.etrans.jt.bluetooth.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.etrans.jt.bluetooth.R;
import com.etrans.jt.btlibrary.domin.ContactBean;
import com.etrans.jt.btlibrary.module.BluetoothPhoneModule;

import java.util.List;


/***
 * @author zhaodongping
 *         联系人界面的listview 适配器
 */

public class ContactAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    Context mContext;
    List<ContactBean> mLstContactData;

    public ContactAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public int getCount() {
        return mLstContactData == null || mLstContactData.size() == 0 ? 0 : mLstContactData.size();

    }

    public void setData(List<ContactBean> lstContactData) {
        mLstContactData = lstContactData;
        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {

        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_contact_list, null);
            holder.mImgContact = (ImageView) convertView.findViewById(R.id.img_contact);
            holder.mContactName = (TextView) convertView.findViewById(R.id.contact_name);
            holder.mContactTelephone = (TextView) convertView.findViewById(R.id.contact_telephone);
            holder.mBtnDial = (ImageView) convertView.findViewById(R.id.btn_dial);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (mLstContactData == null || mLstContactData.size() == 0)
            return convertView;


        ContactBean data = mLstContactData.get(position);

        if (data.getHeadBitmap() != null) {
            holder.mImgContact.setImageBitmap(data.getHeadBitmap());
        } else {
            holder.mImgContact.setImageResource(R.drawable.item_contactlist_img);
        }

        holder.mContactName.setText(data.getName());
        holder.mContactTelephone.setText(data.getMobilePhone());
        final int pos = position;

        holder.mBtnDial.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (pos >= 0 && pos < mLstContactData.size()) {
                    String phoneNumber = mLstContactData.get(pos).getMobilePhone();
//					if(XxBluetoothPhoneManager.getInstance().getState() == XxBluetoothState.STATE_LINKED){
//
//						XxBluetoothPhoneManager.getInstance().call(phoneNumber);
//
//					}else{
//						PhoneUtil.call(mContext, phoneNumber);
//						//Toast.makeText(mContext, mContext.getResources().getString(R.string.dial_buletooth_waiting_connect), Toast.LENGTH_SHORT).show();
//					}
                    BluetoothPhoneModule.getInstance().dial(phoneNumber);

                }

            }
        });

        return convertView;
    }


    static class ViewHolder {
        ImageView mImgContact;
        ImageView mBtnDial;
        TextView mContactName;
        TextView mContactTelephone;
        RelativeLayout mContactInfo;
        RelativeLayout mContactListItem;
    }
}
