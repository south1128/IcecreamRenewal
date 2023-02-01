package com.icecream.icecream_renual;

import static com.icecream.icecream_renual.Constants.KOREA_TIMEZONE;
import static com.icecream.icecream_renual.Constants.NOTIFICATION_CHANNEL_ID;
import static com.icecream.icecream_renual.Constants.WORK_A_NAME;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;


public class NotificationHelper {


    private  Context mContext;
    private static final Integer WORK_A_NOTIFICATION_CODE = 0;
    private static final Integer WORK_B_NOTIFICATION_CODE = 1;

    NotificationHelper(Context context){
        mContext = context;
    }

    public void createNotification(String workName){
        Intent intent = new Intent (mContext,NotificationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT); // 대기열에 이미 있다면 MainActivity가 아닌 앱 활성화
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mContext, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setSmallIcon(R.mipmap.fridge) // 기본 제공되는 이미지
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                .setAutoCancel(true); // 클릭 시 Notification 제거

        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, WORK_A_NOTIFICATION_CODE, intent, PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Notification 제목, 컨텐츠 설정
        notificationBuilder.setContentTitle("WorkerA Notification").setContentText("set a Notification contents")
                .setContentIntent(pendingIntent);

        if (notificationManager != null) {
            notificationManager.notify(WORK_A_NOTIFICATION_CODE, notificationBuilder.build());
        }
    }

    public static void createNotificationChannel(Context context) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                // NotificationChannel 초기화
                NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, context.getString(R.string.app_name), NotificationManager.IMPORTANCE_DEFAULT);

                // Configure the notification channel
                notificationChannel.setDescription("푸시알림");
                notificationChannel.enableLights(true); // 화면활성화 설정
                notificationChannel.setVibrationPattern(new long[]{0, 1000, 500}); // 진동패턴 설정
                notificationChannel.enableVibration(true); // 진동 설정
                notificationManager.createNotificationChannel(notificationChannel); // channel 생성
            }
        } catch (NullPointerException nullException) {
            // notificationManager null 오류 raise
            Toast.makeText(context, "푸시 알림 채널 생성에 실패했습니다. 앱을 재실행하거나 재설치해주세요.", Toast.LENGTH_SHORT).show();
            nullException.printStackTrace();
        }
    }

    public static Boolean isNotificationChannelCreated(Context context) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                return notificationManager.getNotificationChannel(NOTIFICATION_CHANNEL_ID) != null;
            }
            return true;
        } catch (NullPointerException nullException) {
            Toast.makeText(context, "푸시 알림 기능에 문제가 발생했습니다. 앱을 재실행해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public static void setScheduledNotification(WorkManager workManager){
        setNotifySchedule(workManager);
    }

    private static void setNotifySchedule(WorkManager workManager){
        OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(Worker.class).build();
        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(Worker.class,24, TimeUnit.HOURS).build();
        try {
            // workerA 정보 조회
            List<WorkInfo> aWorkerNotifyWorkInfoList = workManager.getWorkInfosForUniqueWorkLiveData(WORK_A_NAME).getValue();
            for (WorkInfo workInfo : aWorkerNotifyWorkInfoList) {
                // worker의 동작이 종료된 상태라면 worker 재등록
                if (workInfo.getState().isFinished()) {
                    workManager.enqueue(oneTimeWorkRequest);
                    workManager.enqueueUniquePeriodicWork(WORK_A_NAME, ExistingPeriodicWorkPolicy.KEEP, periodicWorkRequest);
                }
            }
        } catch (NullPointerException nullPointerException) {
            // 알림 worker가 생성된 적이 없으면 worker 생성
            workManager.enqueue(oneTimeWorkRequest);
            workManager.enqueueUniquePeriodicWork(WORK_A_NAME, ExistingPeriodicWorkPolicy.KEEP, periodicWorkRequest);
        }
    }

    // 푸시 알림 허용 및 사용자에 의해 알림이 꺼진 상태가 아니라면 푸시 알림 백그라운드 갱신
    public static void refreshScheduledNotification(Context context) {
        try {
            Boolean isNotificationActivated = PreferenceHelper.getBoolean(context, Constants.SHARED_PREF_NOTIFICATION_KEY);
            if (isNotificationActivated) {
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                boolean isNotifyAllowed;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    int channelImportance = notificationManager.getNotificationChannel(Constants.NOTIFICATION_CHANNEL_ID).getImportance();
                    isNotifyAllowed = channelImportance != NotificationManager.IMPORTANCE_NONE;
                } else {
                    isNotifyAllowed = NotificationManagerCompat.from(context).areNotificationsEnabled();
                }
                if (isNotifyAllowed) {
                    NotificationHelper.setScheduledNotification(WorkManager.getInstance(context));
                }
            }
        } catch (NullPointerException nullException) {
            Toast.makeText(context, "푸시 알림 기능에 문제가 발생했습니다. 앱을 재실행해주세요.", Toast.LENGTH_SHORT).show();
            nullException.printStackTrace();
        }
    }

    public static Calendar getScheduledCalender(Integer scheduledTime) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(KOREA_TIMEZONE), Locale.KOREA);
        cal.set(Calendar.HOUR_OF_DAY, scheduledTime);
        cal.set(Calendar.MINUTE, 17);
        cal.set(Calendar.SECOND, 0);
        return cal;
    }


}



