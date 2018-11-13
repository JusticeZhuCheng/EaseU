package com.example.asus.pillreminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Jay on 2015/10/25 0025.
 */
public class AlarmReceiver extends BroadcastReceiver{

    private static Calendar ref_calendar;
    public static Timer timer;
    public static TimerTask timerTask;

    @Override
    public void onReceive(Context context, final Intent intent) {
        Intent i = new Intent(context, RemindPage.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);

        //

        if (ref_calendar == null || System.currentTimeMillis() - ref_calendar.getTimeInMillis() > 29 * 60 * 1000) {
            timer = new Timer();

            timerTask = new TimerTask() {
                @Override
                public void run() {
                    SmsManager manager = SmsManager.getDefault();
                    manager.sendTextMessage(intent.getStringExtra("phone"), null, "您的亲友这个时间的药还没有服用，关心一下他吧~", null, null);
                }
            };
            timer.schedule(timerTask, 1 * 1000 * 60 * 30);
            if (ref_calendar == null) {
                ref_calendar = Calendar.getInstance();
            }
            ref_calendar.setTimeInMillis(System.currentTimeMillis());
        }
    }

    public static void StopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
    }
}
