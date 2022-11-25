package com.example.icecream_renual;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.example.icecream_renual.databinding.ActivitySettingBinding;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    private static String time = "06:00:00";
    ActivitySettingBinding b;

    private Button save;
    private TimePicker timePicker;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this,R.layout.activity_setting);

        timePicker=(TimePicker)findViewById(R.id.time_picker);
        save=(Button)findViewById(R.id.save);

        save.setOnClickListener(v->{
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            int hour=timePicker.getHour();
            int minute=timePicker.getMinute();
            calendar.set(Calendar.HOUR_OF_DAY,hour);
            calendar.set(Calendar.MINUTE,minute);

            if (calendar.before(Calendar.getInstance())) {
                calendar.add(Calendar.DATE, 1);
            }
            Toast.makeText(this,"알람이 저장되었습니다.",Toast.LENGTH_LONG).show();
            //임의로 날짜와 시간을 지정
            setTime(hour, minute);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        b.sNotification.setOnClickListener(this);
        b.sSetting.setOnClickListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0,0);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.s_notification){
            Intent tonotification = new Intent(this,NotificationActivity.class);
            tonotification.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(tonotification);
        }
        if(v.getId() == R.id.s_setting){
            Intent tomain = new Intent(this,MainActivity.class);
            tomain.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(tomain);
        }
    }

    public void setTime(int hour, int minute){
        time = hour + ":" + minute + ":00";
    }

    public static String getTime(){
        return time;
    }
}
