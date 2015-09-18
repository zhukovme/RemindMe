package me.zhukov.remindme.model;

import java.io.Serializable;
import java.util.Calendar;

import me.zhukov.remindme.util.DateAndTimeManager;

public class Reminder implements Serializable {

    private int mId;
    private String mTitle;
    private String mDate;
    private String mTime;
    private boolean mRepeat;
    private int mRepeatNumber;
    private RepeatType mRepeatType;
    private boolean mSilent;

    public Reminder() {}

    public static Reminder getDefaultReminder() {
        Reminder reminder = new Reminder();
        DateAndTimeManager dtConverter = DateAndTimeManager.getInstance();
        reminder.setDate(dtConverter.getCurrentDate());
        reminder.setTime(dtConverter.getCurrentTime());
        reminder.setRepeat(true);
        reminder.setRepeatNumber(5);
        reminder.setRepeatType(RepeatType.MINUTE);
        reminder.setSilent(false);
        return reminder;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        this.mDate = date;
    }

    public String getTime() {
        return mTime;
    }

    public void setTime(String time) {
        this.mTime = time;
    }

    public boolean getRepeat() {
        return mRepeat;
    }

    public void setRepeat(boolean repeat) {
        this.mRepeat = repeat;
    }

    public int getRepeatNumber() {
        return mRepeatNumber;
    }

    public void setRepeatNumber(int repeatNumber) {
        this.mRepeatNumber = repeatNumber;
    }

    public RepeatType getRepeatType() {
        return mRepeatType;
    }

    public void setRepeatType(RepeatType repeatType) {
        this.mRepeatType = repeatType;
    }

    public boolean getSilent() {
        return mSilent;
    }

    public void setSilent(boolean silent) {
        this.mSilent = silent;
    }

    public String repeatToString() {
        if (getRepeat()) {
            return "Every " + getRepeatNumber() + " " + getRepeatType() + "(s)";
        } else {
            return "Repeat off";
        }
    }

    public Calendar asCalendar() {
        String[] dateSplit = getDate().split("/");
        String[] timeSplit = getTime().split(":");

        int day = Integer.parseInt(dateSplit[0]);
        int month = Integer.parseInt(dateSplit[1]);
        int year = Integer.parseInt(dateSplit[2]);
        int hour = Integer.parseInt(timeSplit[0]);
        int minute = Integer.parseInt(timeSplit[1]);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, day);
        calendar.set(Calendar.MONTH, --month);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.HOUR, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        return calendar;
    }

    public long getRepeatTimeInMillis() {
        switch (getRepeatType()) {
            case MINUTE:
                return getRepeatNumber() * 60000L;
            case HOUR:
                return getRepeatNumber() * 3600000L;
            case DAY:
                return getRepeatNumber() * 86400000L;
            case WEEK:
                return getRepeatNumber() * 604800000L;
            case MONTH:
                return getRepeatNumber() * 2592000000L;
            default:
                return 0;
        }
    }
}
