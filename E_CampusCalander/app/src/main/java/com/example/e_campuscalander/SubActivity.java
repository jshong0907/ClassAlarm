package com.example.e_campuscalander;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.format.DateFormat;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class SubActivity extends AppCompatActivity {
    private String response;
    private TextView mText1;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

        Calendar alarmCal = Calendar.getInstance();
        alarmCal.setTimeInMillis(System.currentTimeMillis());


        mText1 = (android.widget.TextView) findViewById(R.id.textView2);
        mText1.setText("");
        mText1.setMovementMethod(LinkMovementMethod.getInstance());
        Intent intent = getIntent();
        response = intent.getStringExtra("response");
        System.out.println(response);
        try {
            JSONObject json = new JSONObject(response);
            JSONArray jArray = new JSONArray(json.getString("calander_info"));
            for (int i = 0; i < jArray.length(); i++) {
                //mText1.append("Response: " + jArray.getJSONObject(i).getString("course_name") + "\n");
                Date date = calendar.getTime();
                String monthNumber  = (String) DateFormat.format("MM",   date);
                String dateNumber  = (String) DateFormat.format("dd",   date);
                String deadDay = jArray.getJSONObject(i).getString("dead_day");
                String deadMonth = jArray.getJSONObject(i).getString("dead_month");
                String deadHour = jArray.getJSONObject(i).getString("dead_hour");
                String deadMin = jArray.getJSONObject(i).getString("dead_min");

                if (monthNumber.equals(deadMonth) && dateNumber.equals(deadDay)) {
                    String course_name = jArray.getJSONObject(i).getString("course_name");
                    String report_name = jArray.getJSONObject(i).getString("report_name");
                    String href = jArray.getJSONObject(i).getString("href");
                    mText1.append("과목: " + course_name + "\n");

                    String str_text = "<a href=" + href + ">" + report_name + "</a>";
                    mText1.append(Html.fromHtml(str_text, Html.FROM_HTML_MODE_LEGACY));
                    mText1.append("\n\n");


                    context = this;
                    AlarmManager alarmMgr = (AlarmManager) getSystemService(ALARM_SERVICE);
                    AlarmManager alarmMgr30 = (AlarmManager) getSystemService(ALARM_SERVICE);
                    AlarmManager alarmMgr5 = (AlarmManager) getSystemService(ALARM_SERVICE);

                    Intent intentAlarm = new Intent(context, Alarm_Receiver.class);
                    intentAlarm.putExtra("course_name", course_name);
                    intentAlarm.putExtra("report_name", report_name);
                    intentAlarm.putExtra("href", href);

                    PendingIntent alarmIntent;
                    PendingIntent alarmIntent30;
                    PendingIntent alarmIntent5;

                    alarmIntent = PendingIntent.getBroadcast(context, jArray.length()+i, intentAlarm, 0);
                    alarmIntent30 = PendingIntent.getBroadcast(context, 2*jArray.length()+i, intentAlarm, 0);
                    alarmIntent5 = PendingIntent.getBroadcast(context, 3*jArray.length()+i, intentAlarm, 0);


                    calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(deadHour)-1);
                    calendar.set(Calendar.MINUTE, Integer.parseInt(deadMin));
                    // 1시간전 알람 설정
                    alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
                    calendar.add(Calendar.MINUTE, 30);
                    // 30분전 알람 설정
                    alarmMgr30.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent30);
                    calendar.add(Calendar.MINUTE, 25);
                    // 5분전 알람 설정
                    alarmMgr5.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent5);
                }

            }

        }
        catch (JSONException e) {
            mText1.setText(e.toString());
        }


    }

}
