package com.etrans.jt.bluetooth.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.etrans.jt.bluetooth.R;
import com.etrans.jt.btlibrary.domin.ContactBean;

import java.util.ArrayList;
import java.util.List;

public class QueryContactAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    List<ContactBean> mLstContact = new ArrayList<ContactBean>();

    public QueryContactAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
    }

    public void setData(List<ContactBean> lstContactData) {
        mLstContact = lstContactData;
    }

    @Override
    public int getCount() {
        return mLstContact == null ? 0 : mLstContact.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    public ContactBean get(int pos) {
        return (mLstContact == null) ? null : (pos >= mLstContact.size() ? null : mLstContact.get(pos));
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
            convertView = mInflater.inflate(R.layout.item_dial_querycontact, null);
            holder.txtViewNumber = (TextView) convertView.findViewById(R.id.item_dial_querycontact_phonenumber);
            holder.txtViewName = (TextView) convertView.findViewById(R.id.item_dial_querycontact_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ContactBean data = mLstContact.get(position);
        String name = (data.getName() == null) ? data.getMobilePhone() : data.getName();
        holder.txtViewName.setText(name);
        holder.txtViewNumber.setText(data.getMobilePhone());
        return convertView;
    }

    class ViewHolder {
        TextView txtViewNumber;
        TextView txtViewName;
    }

}
