package com.example.asus.pillreminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.LinkedList;

/**
 * Created by asus on 6/7/2016.
 */
public class pillsetlistAdapter extends BaseAdapter {

    private LinkedList<PillData> mData;
    private Context mContext;

    public pillsetlistAdapter(LinkedList<PillData> mData, Context mContext) {
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

    public void remove(PillData data) {
        if(mData != null) {
            mData.remove(data);
        }
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.pillset_list,parent,false);
        TextView txt_pill_name = (TextView) convertView.findViewById(R.id.item_pillname);
        TextView txt_pill_symptom = (TextView) convertView.findViewById(R.id.item_symptom);
        txt_pill_name.setText(mData.get(position).getPillName());
        txt_pill_symptom.setText(mData.get(position).getSyptom());
        ImageButton btn_delete = (ImageButton)convertView.findViewById(R.id.item_delete);

        final String pill_name = txt_pill_name.getText().toString();
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sp = mContext.getSharedPreferences("mysp", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.remove(mData.get(position).getPillName());
                editor.commit();

                AlarmManager am = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
                Intent i = new Intent(mContext.getApplicationContext(), AlarmReceiver.class);
                PendingIntent p = PendingIntent.getBroadcast(mContext.getApplicationContext(), pill_name.hashCode(), i, 0);
                am.cancel(p);
                p.cancel();

                mData.remove(position);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }
}
