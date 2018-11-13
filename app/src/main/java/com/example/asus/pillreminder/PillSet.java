package com.example.asus.pillreminder;

/**
 * Created by asus on 5/30/2016.
 */

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class PillSet extends Activity {

    private ImageButton back;
    private ImageButton save;
    private ImageButton btn_dialog;
    private Context mContext;
    private boolean[] checkItems;
    private AlertDialog alert = null;
    private AlertDialog.Builder builder = null;
    private TextView Text_time;
    private SharedHelper sh;
    private EditText Epillname;
    private EditText Esymptom;
    private EditText Eamount;
    private TextView Etaketime;
    private EditText Epnumber;
    private String Spillname;
    private String Ssymptom;
    private String Samount;
    private String Staketime;
    private String Spnumber;
    private String TAG = " ";
//    private AlarmManager alarmManager;
//    private PendingIntent pi;

    public PillSet() {

    }

    private void bindView() {
//        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Epillname = (EditText)findViewById(R.id.Epillname);
        Esymptom = (EditText)findViewById(R.id.Esymptom);
        Eamount = (EditText)findViewById(R.id.Eamount);
        Etaketime = (TextView)findViewById(R.id.Etaketime);
        Epnumber = (EditText)findViewById(R.id.Epnumber);
        save = (ImageButton) findViewById(R.id.button_save);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Spillname = Epillname.getText().toString();
                Ssymptom = Esymptom.getText().toString();
                Samount = Eamount.getText().toString();
                Staketime = Etaketime.getText().toString();
                Spnumber = Epnumber.getText().toString();
                if(Samount.length() <= 2 && Spnumber.length() == 11 && Spillname.length() != 0 && Ssymptom.length() != 0 && Staketime.length() > 3){
                    sh.save(Spillname, Ssymptom, Samount, Staketime, Spnumber);
                    Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
                    intent.putExtra("phone", Spnumber);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), Spillname.hashCode(), intent, 0);
                    AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);


                    // 根据用户选择的时间来设置Calendar对象
                    String[] times = Staketime.split(" ");
                    for(int i = 0; i < times.length; i++){
                        Calendar c = Calendar.getInstance();
                        String[] temp = times[i].split(":");
                        Log.i("flag", (c.get(Calendar.HOUR_OF_DAY) > Integer.parseInt(temp[0])) + "");
                        if(c.get(Calendar.HOUR_OF_DAY) > Integer.parseInt(temp[0]) ||
                                (c.get(Calendar.HOUR_OF_DAY) == Integer.parseInt(temp[0]) && c.get(Calendar.MINUTE) > Integer.parseInt(temp[1]))){
                            c.set(Calendar.DATE, c.get(Calendar.DATE) + 1);
                        }

//                    c.setTimeInMillis(System.currentTimeMillis() + 5000);
                        c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(temp[0]));
                        c.set(Calendar.MINUTE, Integer.parseInt(temp[1]));
                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), 24 * 3600 * 1000, pendingIntent);
                    }
                    finish();
                    Toast.makeText(getApplication(), "已保存", Toast.LENGTH_SHORT).show();}
                else {
                    Toast.makeText(getApplication(), "请正确填写噢~", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_dialog = (ImageButton) findViewById(R.id.button_detail);
        btn_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String[] menu = new String[]{"07:00","07:30","08:00","08:30","10:30", "11:00", "11:30", "12:00","12:30","17:30","18:00","18:30","19:00"};
                //定义一个用来记录个列表项状态的boolean数组
                checkItems = new boolean[]{false, false, false, false, false, false, false, false, false, false, false,false, false};
                alert = null;
                builder = new AlertDialog.Builder(mContext);
                alert = builder.setMultiChoiceItems(menu, checkItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        checkItems[which] = isChecked;
                    }
                })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String result = "";
                                for (int i = 0; i < checkItems.length; i++) {
                                    if (checkItems[i])
                                        result += menu[i] + " ";
                                }
                                Text_time = (TextView)findViewById(R.id.Etaketime);
                                Text_time.setText(result);
                            }
                        })
                        .setCancelable(false)
                        .create();
                alert.show();
            }
        });}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pill_set);
        mContext = PillSet.this;
        sh = new SharedHelper(mContext);
        bindView();
        back = (ImageButton) findViewById(R.id.button_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    }