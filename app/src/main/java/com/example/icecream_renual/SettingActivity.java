package com.example.icecream_renual;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.work.WorkManager;

import com.example.icecream_renual.databinding.ActivitySettingBinding;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    private static String time = "06:00:00";
    ActivitySettingBinding b;

    private Button save;
    private TimePicker timePicker;

    //MainActivity mainActivity = new MainActivity();

    private CompoundButton switchActivateNotify;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this,R.layout.activity_setting);

//        timePicker=(TimePicker)findViewById(R.id.time_picker);
//        save=(Button)findViewById(R.id.save);

//        save.setOnClickListener(v->{
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTimeInMillis(System.currentTimeMillis());
//            int hour=timePicker.getHour();
//            int minute=timePicker.getMinute();
//            calendar.set(Calendar.HOUR_OF_DAY,hour);
//            calendar.set(Calendar.MINUTE,minute);
//
//            if (calendar.before(Calendar.getInstance())) {
//                calendar.add(Calendar.DATE, 1);
//            }
//            Toast.makeText(this,hour + " : " + minute + "알람이 저장되었습니다.",Toast.LENGTH_LONG).show();
//            //임의로 날짜와 시간을 지정
//            setTime(hour, minute);
//        });
        b.whattime.setText(time);
//
        initSwitchLayout(WorkManager.getInstance(getApplicationContext()));
    }

    @Override
    public void onResume() {
        super.onResume();
        b.sNotification.setOnClickListener(this);
        b.sSetting.setOnClickListener(this);
        b.whattime.setOnClickListener(this);
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
        if(v.getId()==R.id.whattime){
            timerDialog();
        }
    }

    public void timerDialog(){
        final TimerDialog timerDialog = new TimerDialog(this);
        timerDialog.show();

        timerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                boolean isSaved = timerDialog.getButtonstates();
                if (isSaved == true) {
                    String changed_time = timerDialog.getTime();
                    b.whattime.setText(changed_time);
                    time = changed_time;

                }
            }
        });
    }

//    public void addDialogMethod(){
//        final AddDialog addDialog = new AddDialog(this);
//        addDialog.show();
//
//        addDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialogInterface) {
//                boolean isSaved = addDialog.getButtonStates();
//                if (isSaved == true) {
//                    String[] elements = addDialog.getelements();
//                    String emoji = elements[0];
//                    String name = elements[1];
//                    String date = elements[2];
//                    String[] date_split = date.split("\\."); //YYYY.MM.DD 형식을 YYYY MM DD 로 나누기
//                    int year = Integer.parseInt(date_split[0]);
//                    int month = Integer.parseInt(date_split[1]);
//                    int day = Integer.parseInt(date_split[2]);
//                    String category = elements[3];
//                    String memo = elements[4];
//                    writeFile(name + ".txt", emoji + "|" +name + "|" + year + "|" + month + "|" + day + "|" + category + "|" + memo + "|");
//                }
//                onResume();
//            }
//        });
//    }


    public static String getTime(){
        return time;
    }


    private void initSwitchLayout(final WorkManager workManager) {
        switchActivateNotify = (CompoundButton) findViewById(R.id.switch1);
        switchActivateNotify.setChecked(PreferenceHelper.getBoolean(getApplicationContext(), Constants.SHARED_PREF_NOTIFICATION_KEY));
        switchActivateNotify.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    boolean isChannelCreated = NotificationHelper.isNotificationChannelCreated(getApplicationContext());
                    if(isChannelCreated){
                        PreferenceHelper.setBoolean(getApplicationContext(), Constants.SHARED_PREF_NOTIFICATION_KEY, true);
                        NotificationHelper.setScheduledNotification(workManager);
                    }
                    else {
                        NotificationHelper.createNotificationChannel(getApplicationContext());
                    }
                } else {
                    PreferenceHelper.setBoolean(getApplicationContext(), Constants.SHARED_PREF_NOTIFICATION_KEY, false);
                    workManager.cancelAllWork();
                }
            }
        });
    }
}
