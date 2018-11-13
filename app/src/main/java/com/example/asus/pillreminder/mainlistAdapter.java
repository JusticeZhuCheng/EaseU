package com.example.asus.pillreminder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.LinkedList;

/**
 * Created by asus on 6/7/2016.
 */
public class mainlistAdapter extends BaseAdapter {

    private LinkedList<mainlistData> mData;
    private Context mContext;

    public mainlistAdapter(LinkedList<mainlistData> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.main_list,parent,false);
        TextView main_list1 = (TextView) convertView.findViewById(R.id.main_list1);
        TextView main_list2 = (TextView) convertView.findViewById(R.id.main_list2);
        TextView main_list3 = (TextView) convertView.findViewById(R.id.main_list3);
        main_list1.setText(mData.get(position).getaName());
        main_list2.setText(mData.get(position).getaSpeak());
        main_list3.setText("数量 " + mData.get(position).getaNumber());

        return convertView;
    }
}
