package me.zhukov.remindme.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.List;

import me.zhukov.remindme.database.ReminderMapper;
import me.zhukov.remindme.model.Reminder;

public class BootReceiver extends BroadcastReceiver {

    private ReminderMapper mReminderMapper;
    private AlarmReceiver mAlarmReceiver;
    private List<Reminder> mReminders;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            mAlarmReceiver = new AlarmReceiver();
            mReminderMapper = new ReminderMapper(context);
            mReminders = mReminderMapper.getAllReminders();

            for (Reminder reminder : mReminders) {
                if (!reminder.getSilent()) continue;
                if (reminder.getRepeat()) {
                    mAlarmReceiver.setRepeatAlarm(
                            context,
                            reminder.asCalendar(),
                            reminder.getRepeatTimeInMillis(),
                            reminder.getId(),
                            reminder.getTitle()
                    );
                } else {
                    mAlarmReceiver.setAlarm(
                            context,
                            reminder.asCalendar(),
                            reminder.getId(),
                            reminder.getTitle()
                    );
                }
            }
        }
    }
}
