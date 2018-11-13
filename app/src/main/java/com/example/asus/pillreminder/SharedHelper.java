package com.example.asus.pillreminder;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by asus on 6/2/2016.
 */
public class SharedHelper {
    private Context mContext;
    private String[] onegroup;
    public SharedHelper() {
    }

    public SharedHelper(Context mContext) {
        this.mContext = mContext;
    }


    //定义一个保存数据的方法
    public void save(String Epillname, String Esymptom, String Eamount, String Etaketime, String Epnumber) {
        PillData pillData = new PillData(Epillname, Esymptom, Eamount, Etaketime, Epnumber);
        Gson gson = new Gson();
        String record = gson.toJson(pillData, PillData.class);
        SharedPreferences sp = mContext.getSharedPreferences("mysp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Epillname, record);
        editor.commit();
    }


    //定义一个读取SP文件的方法
    public ArrayList<PillData> read() {
        ArrayList<PillData> result = new ArrayList<>();
        SharedPreferences sp = mContext.getSharedPreferences("mysp", Context.MODE_PRIVATE);
        Map<String, String> data = (Map<String, String>)sp.getAll();
        Gson gson = new Gson();
        for(String key : data.keySet()){
            result.add(gson.fromJson(data.get(key), PillData.class));
        }
        return result;
    }

    public void saveAlarms(String Epillname, Date date) {
        // check first if exist
        Gson gson = new Gson();
        String record = gson.toJson(date, Date.class);
        SharedPreferences sp = mContext.getSharedPreferences("alarmInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Epillname, record);
        editor.commit();
    }

    public HashMap<String, Date> readAlarms() {
        HashMap<String, Date> result = new HashMap<>();
        SharedPreferences sp = mContext.getSharedPreferences("alarmInfo", Context.MODE_PRIVATE);
        Map<String, String> data = (Map<String, String>)sp.getAll();
        Gson gson = new Gson();
        for(String key : data.keySet()){
            result.put(key, gson.fromJson(data.get(key), Date.class));
        }
        return result;
    }
}
