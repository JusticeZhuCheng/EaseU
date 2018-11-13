package com.example.asus.pillreminder;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by asus on 5/26/2016.
 */
public class Myfragment1 extends android.support.v4.app.Fragment{

    public Myfragment1() {
    }

    private String mYear;
    private String mMonth;
    private String mDay;
    private String mWay;
    private String hour;
    private String minute;
    private TextView main_time;
    private TextView main_date;
    private TextView main_day;
    private static Handler myHandler;
    private List<mainlistData> mData = null;
    private Context mContext;
    private mainlistAdapter mAdapter = null;
    private ListView list_two;
    private TextView txt_empty1;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, null);
        mContext = getActivity();
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        mYear = String.valueOf(c.get(Calendar.YEAR));// 获取当前年份
        mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
        mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
        minute = String.valueOf(c.get(Calendar.MINUTE));
        if ("1".equals(mWay)) {
            mWay = "天";
        } else if ("2".equals(mWay)) {
            mWay = "一";
        } else if ("3".equals(mWay)) {
            mWay = "二";
        } else if ("4".equals(mWay)) {
            mWay = "三";
        } else if ("5".equals(mWay)) {
            mWay = "四";
        } else if ("6".equals(mWay)) {
            mWay = "五";
        } else if ("7".equals(mWay)) {
            mWay = "六";
        }
        main_time = (TextView)view.findViewById(R.id.main_time);
        main_day = (TextView)view.findViewById(R.id.main_day);
        main_date = (TextView)view.findViewById(R.id.main_date);
        main_day.setText("星期" + mWay);
        main_date.setText(mYear +"年" + mMonth + "月" + mDay + "日");
        main_time.setText(hour + "：" + minute);
        myHandler = new Handler() {
            @Override
            //重写handleMessage方法,根据msg中what的值判断是否执行后续操作
            public void handleMessage(Message msg) {
                Calendar c1 = Calendar.getInstance();
                c1.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
                if(msg.what == 0x123)
                {
                    hour = String.valueOf(c1.get(Calendar.HOUR_OF_DAY));
                    if(hour.length() < 2){
                        hour = "0"+hour;
                    }
                    minute = String.valueOf(c1.get(Calendar.MINUTE));
                    if(minute.length() < 2){
                        minute = "0"+minute;
                    }
                    main_time.setText(hour + "：" + minute);
                    main_time.refreshDrawableState();
                }
                super.handleMessage(msg);
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(!Thread.currentThread().isInterrupted()){
                    myHandler.sendEmptyMessage(0x123);
                    try{
                        Thread.sleep(1000);
                    }
                    catch (Exception e){
                    }
                }
            }
        }).start();

        list_two = (ListView)view.findViewById(R.id.list_two);
        mData = new LinkedList<mainlistData>();

        mAdapter = new mainlistAdapter((LinkedList<mainlistData>) mData, mContext);
        list_two.setAdapter(mAdapter);
        txt_empty1 = (TextView)view.findViewById(R.id.txt_empty1);
        list_two.setEmptyView(txt_empty1);
        add2List();
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            add2List();
        }
    }

    private void add2List(){
        try {
            mData.clear();
            SharedHelper sp = new SharedHelper(getActivity());
            ArrayList<PillData> pillRecords = sp.read();
            mData = new LinkedList<mainlistData>();
            for(int i = 0; i < pillRecords.size(); i++){
                for(int j = 0; j < pillRecords.get(i).getAllTime().length; j++){
                    mData.add(new mainlistData(pillRecords.get(i).getAllTime()[j], pillRecords.get(i).getPillName(), pillRecords.get(i).getAmount()));
                }
            }
            mAdapter = new mainlistAdapter((LinkedList<mainlistData>) mData, mContext);
            list_two.setAdapter(mAdapter);
        }
        catch(Exception e){

        }
    }
}
