package me.zhukov.remindme.util;

import java.util.Calendar;

public class DateAndTimeManager {

    private static DateAndTimeManager instance;
    private Calendar mCalendar = Calendar.getInstance();

    private DateAndTimeManager() {}

    public static DateAndTimeManager getInstance() {
        if (instance == null) {
            instance = new DateAndTimeManager();
        }
        return instance;
    }

    public int getCurrentYear() {
        return mCalendar.get(Calendar.YEAR);
    }

    public int getCurrentMonth() {
        return mCalendar.get(Calendar.MONTH) + 1;
    }

    public int getCurrentDay() {
        return mCalendar.get(Calendar.DATE);
    }

    public int getCurrentHour() {
        return mCalendar.get(Calendar.HOUR);
    }

    public int getCurrentMinute() {
        return mCalendar.get(Calendar.MINUTE);
    }

    public String getCurrentDate() {
        return convertDate(
                getCurrentDay(),
                getCurrentMonth(),
                getCurrentYear()
        );
    }

    public String getCurrentTime() {
        return convertTime(
                getCurrentHour(),
                getCurrentMinute()
        );
    }

    public String convertDate(int day, int month, int year) {
        return day + "/" + month + "/" + year;
    }

    public String convertTime(int hour, int minute) {
        if (minute < 10) {
            return hour + ":0" + minute;
        } else {
            return hour + ":" + minute;
        }
    }
}
