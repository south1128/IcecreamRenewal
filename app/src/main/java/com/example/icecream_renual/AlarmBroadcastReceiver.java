package com.example.icecream_renual;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmBroadcastReceiver extends BroadcastReceiver {
    public AlarmBroadcastReceiver(){

    }
    @Override
    public void onReceive(Context context, Intent intent){
        Intent alarmIntentServiceIntent = new Intent(context, MainActivity.AlarmIntentService.class);
        context.startService(alarmIntentServiceIntent);
    }
}
