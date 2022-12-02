package com.example.icecream_renual;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import java.io.File;
import java.io.FilenameFilter;


public class AlarmRecevier extends BroadcastReceiver {

    public AlarmRecevier() {
    }

    int countAlarm = 0;

    NotificationManager manager;
    NotificationCompat.Builder builder;

    //오레오 이상은 반드시 채널을 설정해줘야 Notification이 작동함
    private static String CHANNEL_ID = "channel1";
    private static String CHANNEL_NAME = "Channel1";

    Func func = new Func();
    FoodData foodData = new FoodData();
    //파일 경로
    private String path = "/data/data/com.example.icecream_renual/files/";
    //파일 이름 저장
    File file = new File(path);

    FilenameFilter filter = new FilenameFilter() {
        @Override
        public boolean accept(File file, String s) {
            return s.contains(".txt");
        }
    };
    String[] fileNames = file.list(filter);

    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        if(fileNames.length > 0){
            for (int i = 0; i < (fileNames.length); i++) {
                String rFile = func.readFile(path + fileNames[i]);
                //읽어온 파일 나누기
                String[] txt_split = rFile.split("\\|");
                String name = txt_split[0];
                int year = Integer.parseInt(txt_split[1]);
                int month = Integer.parseInt(txt_split[2]);
                int day = Integer.parseInt(txt_split[3]);
                String category = txt_split[4];

                if(foodData.calculateDday(year, month, day) <= 7){
                    countAlarm++;
                }

            }
        }

        builder = null;
        manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            manager.createNotificationChannel(
                    new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            );
            builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        } else {
            builder = new NotificationCompat.Builder(context);
        }

        //알림창 클릭 시 activity 화면 부름
        Intent intent2 = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,101,intent2, PendingIntent.FLAG_IMMUTABLE);

        //알림창 제목
        builder.setContentTitle("아이스크림");
        //알림창 내용
        builder.setContentText(SettingActivity.getTime() + " : " + countAlarm + "개의 음식이 변하고 있어요!!");
        //알림창 아이콘
        builder.setSmallIcon(R.drawable.ic_launcher_main_foreground);
        //알림창 터치시 자동 삭제
        builder.setAutoCancel(true);

        builder.setContentIntent(pendingIntent);

        Notification notification = builder.build();
        manager.notify(1,notification);

    }
}