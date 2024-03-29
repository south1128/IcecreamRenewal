package com.icecream.icecream_renual;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.work.WorkManager;

import com.icecream.icecream_renual.databinding.ActivitySettingBinding;

import java.util.Objects;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    private static String time ;
    ActivitySettingBinding b;

    private Button save;
    private TimePicker timePicker;

    //MainActivity mainActivity = new MainActivity();

    private CompoundButton switchActivateNotify;

    String color;
    int themecolor;
    SharedPreferences sharedPreferences;
    String shared = "thememode";


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
//        sharedPreferences = getSharedPreferences(shared,0);
//        String value = sharedPreferences.getString("color","");
//        if (Objects.equals(value, "red")){
//            setTheme(R.style.Theme_App_Red);
//        }
//        if (Objects.equals(value, "yellow")){
//            setTheme(R.style.Theme_App_Yellow);
//        }
//        if (Objects.equals(value, "green")){
//            setTheme(R.style.Theme_App_green);
//        }
//        else {setTheme(R.style.Theme_Icecream_renual);}
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
//        b.whattime.setText(time);
//
        initSwitchLayout(WorkManager.getInstance(getApplicationContext()));


    }
    @Override
    public Resources.Theme getTheme() {
        Resources.Theme theme = super.getTheme();
        sharedPreferences = getSharedPreferences(shared,0);
        String value = sharedPreferences.getString("color","");
        if (Objects.equals(value, "red")){
            theme.applyStyle(R.style.Theme_App_Red,true);
        }
        else if (Objects.equals(value, "yellow")){
            theme.applyStyle(R.style.Theme_App_Yellow,true);
        }
        else if (Objects.equals(value, "green")){
            theme.applyStyle(R.style.Theme_App_green,true);
        }
        else {
            theme.applyStyle(R.style.Theme_Icecream_renual,true);
        }

        return theme;
    }

    @Override
    public void onResume() {
        super.onResume();
        b.sNotification.setOnClickListener(this);
        b.sSetting.setOnClickListener(this);
        b.tvAlarmTimer.setOnClickListener(this);

        sharedPreferences = getSharedPreferences(shared,0);
        String value = sharedPreferences.getString("color","");
        if (Objects.equals(value, "red")){
            b.ivBlue.setImageDrawable(null);
            b.ivRed.setImageDrawable(getDrawable(R.mipmap.stroke));
            b.ivYellow.setImageDrawable(null);
            b.ivGreen.setImageDrawable(null);
        }
        else if (Objects.equals(value, "yellow")){
            b.ivBlue.setImageDrawable(null);
            b.ivRed.setImageDrawable(null);
            b.ivYellow.setImageDrawable(getDrawable(R.mipmap.stroke));
            b.ivGreen.setImageDrawable(null);
        }
        else if (Objects.equals(value, "green")){
            b.ivBlue.setImageDrawable(null);
            b.ivRed.setImageDrawable(null);
            b.ivYellow.setImageDrawable(null);
            b.ivGreen.setImageDrawable(getDrawable(R.mipmap.stroke));
        }
        else {
            b.ivBlue.setImageDrawable(getDrawable(R.mipmap.stroke));
            b.ivRed.setImageDrawable(null);
            b.ivYellow.setImageDrawable(null);
            b.ivGreen.setImageDrawable(null);
        }
//        b.redtheme.setImageDrawable(null);
//        b.yellowtheme.setImageDrawable(null);
//        b.greentheme.setImageDrawable(null);

        b.ivBlue.setOnClickListener(this);
        b.ivRed.setOnClickListener(this);
        b.ivYellow.setOnClickListener(this);
        b.ivGreen.setOnClickListener(this);
        b.vSave.setOnClickListener(this);

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
        if(v.getId()==R.id.tv_time){
            timerDialog();
        }

        sharedPreferences = getSharedPreferences(shared ,MODE_PRIVATE);
        if(v.getId()==R.id.iv_blue){
            b.ivBlue.setImageDrawable(getDrawable(R.mipmap.stroke));
            b.ivRed.setImageDrawable(null);
            b.ivYellow.setImageDrawable(null);
            b.ivGreen.setImageDrawable(null);
            color = "blue";

        }
        if(v.getId()==R.id.iv_red){
            b.ivBlue.setImageDrawable(null);
            b.ivRed.setImageDrawable(getDrawable(R.mipmap.stroke));
            b.ivYellow.setImageDrawable(null);
            b.ivGreen.setImageDrawable(null);
            color = "red";

        }
        if(v.getId()==R.id.iv_yellow){
            b.ivBlue.setImageDrawable(null);
            b.ivRed.setImageDrawable(null);
            b.ivYellow.setImageDrawable(getDrawable(R.mipmap.stroke));
            b.ivGreen.setImageDrawable(null);
            color = "yellow";

        }
        if(v.getId()==R.id.iv_green){
            b.ivBlue.setImageDrawable(null);
            b.ivRed.setImageDrawable(null);
            b.ivYellow.setImageDrawable(null);
            b.ivGreen.setImageDrawable(getDrawable(R.mipmap.stroke));
            color = "green";

        }
        if(v.getId()==R.id.v_save){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("color",color);
            editor.commit();
            Intent intent = new Intent(this.getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
//            setTheme(R.style.Theme_App_Red);

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
                    b.tvTime.setText(changed_time);
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
        switchActivateNotify = (CompoundButton) findViewById(R.id.sw_timer);
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
