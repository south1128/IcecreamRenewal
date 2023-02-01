package com.icecream.icecream_renual;

import static com.icecream.icecream_renual.Constants.A_MORNING_EVENT_TIME;
import static com.icecream.icecream_renual.Constants.KOREA_TIMEZONE;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.WorkerParameters;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class Worker extends androidx.work.Worker {
    public Worker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        NotificationHelper mNotificationHelper = new NotificationHelper(getApplicationContext());
        long currentMillis = Calendar.getInstance(TimeZone.getTimeZone(KOREA_TIMEZONE), Locale.KOREA).getTimeInMillis();

        Calendar eventCal = NotificationHelper.getScheduledCalender(A_MORNING_EVENT_TIME);
        long eventNotifyMinRange = eventCal.getTimeInMillis();

        eventCal.add(Calendar.HOUR_OF_DAY,Constants.NOTIFICATION_INTERVAL_HOUR);
        long eventNotifyMaxRange = eventCal.getTimeInMillis();

        boolean iseventnotifyrange = eventNotifyMinRange <= currentMillis && currentMillis<= eventNotifyMaxRange;
        boolean iseventNotifyAvailable = iseventnotifyrange;

        if(iseventNotifyAvailable){
            mNotificationHelper.createNotification(Constants.WORK_A_NAME);
        }else {

        }
        return Result.success();
    }
}
