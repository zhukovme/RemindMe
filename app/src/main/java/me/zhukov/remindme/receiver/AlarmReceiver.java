package me.zhukov.remindme.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.WakefulBroadcastReceiver;

import java.util.Calendar;

import me.zhukov.remindme.util.NotificationUtils;

public class AlarmReceiver extends WakefulBroadcastReceiver {

    private static final String EXTRA_ID = "reminderId";
    private static final String EXTRA_MESSAGE = "reminderMessage";

    private AlarmManager mAlarmManager;
    private PendingIntent mAlarmIntent;

    @Override
    public void onReceive(Context context, Intent intent) {
        int id = intent.getIntExtra(EXTRA_ID, -1);
        String message = intent.getStringExtra(EXTRA_MESSAGE);
        NotificationUtils.getInstance(context).createNotification(message, id);
    }

    private PendingIntent getAlarmIntent(Context context, int id, String message) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(EXTRA_ID, id);
        intent.putExtra(EXTRA_MESSAGE, message);
        return PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    private void setBootReceiver(int state, Context context) {
        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver, state, PackageManager.DONT_KILL_APP);
    }

    public void setAlarm(Context context, Calendar calendar, int id, String message) {
        mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        mAlarmIntent = getAlarmIntent(context, id, message);
        mAlarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), mAlarmIntent);
        setBootReceiver(PackageManager.COMPONENT_ENABLED_STATE_ENABLED, context);
    }

    public void setRepeatAlarm(Context context, Calendar calendar, long repeatTime, int id,
                               String message) {
        mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        mAlarmIntent = getAlarmIntent(context, id, message);
        mAlarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                repeatTime,
                mAlarmIntent
        );
        setBootReceiver(PackageManager.COMPONENT_ENABLED_STATE_ENABLED, context);
    }

    public void cancelAlarm(Context context, int id) {
        if (mAlarmManager == null) {
            mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        }
        Intent intent = new Intent(context, AlarmReceiver.class);
        mAlarmIntent = PendingIntent.getBroadcast(context, id, intent, 0);
        mAlarmManager.cancel(mAlarmIntent);
        setBootReceiver(PackageManager.COMPONENT_ENABLED_STATE_DISABLED, context);
    }
}
