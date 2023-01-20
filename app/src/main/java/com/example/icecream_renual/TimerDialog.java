package com.example.icecream_renual;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.work.WorkManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TimerDialog extends Dialog {
    public TimerDialog(@NonNull Context context) {
        super(context);}

    private static String time = "06:00:00";
    private CompoundButton switchActivateNotify;

    public TimePicker timePicker;
    public ImageView save;

    boolean buttonState = false;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_timer);



        timePicker=(TimePicker)findViewById(R.id.time_picker);
        save=(ImageView) findViewById(R.id.save);

        save.setOnClickListener(v->{
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

            int hour=timePicker.getHour();
            int minute=timePicker.getMinute();
            calendar.set(Calendar.HOUR_OF_DAY,hour);
            calendar.set(Calendar.MINUTE,minute);

            if (calendar.before(Calendar.getInstance())) {
                calendar.add(Calendar.DATE, 1);
            }
//            Toast.makeText(this,hour + " : " + minute + "알람이 저장되었습니다.",Toast.LENGTH_LONG).show();
            //임의로 날짜와 시간을 지정
//            time = hour + ":" + minute + ":00";
//            try {
//                sdf.parse(time);
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
            setTime(hour, minute);
            buttonState = true;
            dismiss();
        });


    }

    public void setTime(int hour, int minute){
        time = hour + ":" + minute + ":00";
    }

    public static String getTime(){
        return time;
    }
    public boolean getButtonstates(){return buttonState;}




}
