package com.example.asus.pillreminder;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by asus on 5/26/2016.
 */
public class RemindPage extends Activity{

    public RemindPage() {
    }

    private MediaPlayer mediaPlayer;

    private Button btn_takelater, btn_taken, button_takenow;

    private List<mainlistData> mData = null;
    private mainlistAdapter mAdapter = null;
    private ListView list_remind;

    private static Calendar ref_calendar;
    private String mYear;
    private String mMonth;
    private String mDay;
    private String mWay;
    private String hour;
    private String minute;
    private TextView main_time1;
    private TextView main_date1;
    private TextView main_day1;
    private static Handler myHandler;
    private Context context;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        if (ref_calendar == null) {
            ref_calendar = Calendar.getInstance();
//            ref_calendar.set(Calendar.HOUR_OF_DAY, 17);
//            ref_calendar.set(Calendar.MINUTE, 30);
        }

        setContentView(R.layout.remindpage);
        sp = this.getSharedPreferences("hehe", Context.MODE_PRIVATE);
        editor = sp.edit();
        list_remind = (ListView)findViewById(R.id.list_remind);
        btn_takelater = (Button)findViewById(R.id.button_takelater);
        btn_taken = (Button)findViewById(R.id.btn_taken);
        button_takenow = (Button)findViewById(R.id.button_takenow);
        mData = new LinkedList<mainlistData>();

        mAdapter = new mainlistAdapter((LinkedList<mainlistData>) mData, this);
        list_remind.setAdapter(mAdapter);
        add2List();
        PlaySound();


        button_takenow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button_takenow.setVisibility(View.GONE);
                btn_takelater.setVisibility(View.GONE);
                btn_taken.setVisibility(View.VISIBLE);

            }
        });
        btn_taken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                AlarmReceiver.StopTimer();

                Calendar c = Calendar.getInstance();
                String key = "" + c.get(Calendar.YEAR) + (c.get(Calendar.MONTH)+1) + c.get(Calendar.DATE);
                Log.i("qisiwole", key);
                String result = sp.getString(key, "null");
                Gson gson = new Gson();
                if(!result.equals("null")){
                    ArrayList<mainlistData> records = gson.fromJson(result, new TypeToken<ArrayList<mainlistData>>(){}.getType());
                    records.addAll(mData);
                    editor.putString(key, gson.toJson(records, new TypeToken<ArrayList<mainlistData>>(){}.getType()));
                }
                else{
                    editor.putString(key, gson.toJson(mData, new TypeToken<ArrayList<mainlistData>>(){}.getType()));
                }
                editor.commit();
                finish();
            }
        });
        btn_takelater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), mData.get(0).getaSpeak().hashCode(), intent, 0);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(System.currentTimeMillis() + 5000 * 60);
                alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
                mediaPlayer.stop();
                ref_calendar.add(Calendar.MINUTE, -5);
                finish();
            }
        });
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        mYear = String.valueOf(c.get(Calendar.YEAR));// 获取当前年份
        mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
        mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
        Log.i("hour", hour);
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
        main_time1 = (TextView)findViewById(R.id.main_time1);
        main_day1 = (TextView)findViewById(R.id.main_day1);
        main_date1 = (TextView)findViewById(R.id.main_date1);
        main_day1.setText("星期" + mWay);
        main_date1.setText(mYear +"年" + mMonth + "月" + mDay + "日");
        main_time1.setText(hour+"："+minute);
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
                    main_time1.setText(hour + "：" + minute);
                    main_time1.refreshDrawableState();
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


    }

    private void PlaySound() {
        mediaPlayer = mediaPlayer.create(this,R.raw.pig);
        mediaPlayer.start();
    }

    private void add2List(){
        mData.clear();
        SharedHelper sp = new SharedHelper(this);
        ArrayList<PillData> pillRecords = sp.read();
        mData = new LinkedList<mainlistData>();
        for(int i = 0; i < pillRecords.size(); i++){
            String[] allTimes = pillRecords.get(i).getAllTime();
            for(int j = 0; j < allTimes.length; j++){
                String[] temp = allTimes[j].split(":");
                if (ref_calendar.getTime().getHours() == Integer.parseInt(temp[0])
//                if ( 17 == Integer.parseInt(temp[0])
                        && ref_calendar.getTime().getMinutes() == Integer.parseInt(temp[1])) {
//                    && 30 == Integer.parseInt(temp[1])) {
                    mData.add(new mainlistData(allTimes[j], pillRecords.get(i).getPillName(), pillRecords.get(i).getAmount()));
                }
            }
        }
        mAdapter = new mainlistAdapter((LinkedList<mainlistData>) mData, this);
        list_remind.setAdapter(mAdapter);
    }
}

