package com.example.asus.pillreminder;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * Created by asus on 5/26/2016.
 */
public class Myfragment3 extends android.support.v4.app.Fragment{

    private String TAG = "calender";
    private ImageButton pre;
    private ImageButton next;
    private TextView Text_calendar;
    private ListView litongyuList;
    private mainlistAdapter litongyuAdapter = null;
    private LinkedList<mainlistData> mData = null;

    private SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMM - yyyy", Locale.getDefault());
    private String[] days = {"星期一","星期二","星期三","星期四","星期五","星期六","星期日"};
    public Myfragment3() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_history, null);
        final CompactCalendarView compactCalendarView = (CompactCalendarView)view.findViewById(R.id.compactcalendar_view);
        compactCalendarView.setDayColumnNames(days);
        compactCalendarView.shouldScrollMonth(false);
        Text_calendar = (TextView)view.findViewById(R.id.text_calendar);
        mData = new LinkedList<mainlistData>();
        litongyuList = (ListView)view.findViewById(R.id.litongyu_list);
        litongyuAdapter = new mainlistAdapter(mData, getActivity());
        litongyuList.setAdapter(litongyuAdapter);

        Text_calendar.setText(dateFormatForMonth.format(compactCalendarView.getFirstDayOfCurrentMonth()));
        pre = (ImageButton)view.findViewById(R.id.prev_button);
        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                compactCalendarView.showPreviousMonth();
            }
        });
        next = (ImageButton)view.findViewById(R.id.next_button);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                compactCalendarView.showNextMonth();
            }
        });

        // Add event 1 on Sun, 07 Jun 2015 18:20:51 GMT
        Event ev1 = new Event(Color.GREEN, 1433701251000L, "Some extra data that I want to store.");
        compactCalendarView.addEvent(ev1);

        // Added event 2 GMT: Sun, 07 Jun 2015 19:10:51 GMT
        Event ev2 = new Event(Color.GREEN, 1433704251000L);
        compactCalendarView.addEvent(ev2);

        // Query for events on Sun, 07 Jun 2015 GMT.
        // Time is not relevant when querying for events, since events are returned by day.
        // So you can pass in any arbitary DateTime and you will receive all events for that day.
        List<Event> events = compactCalendarView.getEvents(1433701251000L); // can also take a Date object

        // events has size 2 with the 2 events inserted previously


        // define a listener to receive callbacks when certain events happen.
        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                mData.clear();
                List<Event> events = compactCalendarView.getEvents(dateClicked);
                Log.d(TAG, "Day was clicked: " + dateClicked + " with events " + events);
                String key = "" + (1900+dateClicked.getYear()) + (dateClicked.getMonth()+1) + dateClicked.getDate();
                Log.i(TAG, key);
                SharedPreferences sp = getActivity().getSharedPreferences("hehe", Context.MODE_PRIVATE);
                String records = sp.getString(key, null);
                litongyuAdapter.notifyDataSetChanged();
                if(records != null){
                    Gson gson = new Gson();
                    List<mainlistData> listDatas = gson.fromJson(records, new TypeToken<ArrayList<mainlistData>>(){}.getType());
                    mData.addAll(listDatas);
                    litongyuAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                Text_calendar.setText(dateFormatForMonth.format(firstDayOfNewMonth));
                Log.d(TAG, "Month was scrolled to: " + firstDayOfNewMonth);
            }
        });
        return view;
    }
}


