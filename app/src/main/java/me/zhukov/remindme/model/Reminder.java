package me.zhukov.remindme.model;

public class Reminder {

    private int mId;
    private String mTitle;
    private String mDate;
    private String mTime;
    private boolean mRepeat;
    private String mRepeatNumber;
    private String mRepeatType;
    private boolean mSilent;

    public Reminder() {}

    public Reminder(String title, String date, String time, boolean repeat, String repeatNumber,
                    String repeatType, boolean silent) {
        mTitle = title;
        mDate = date;
        mTime = time;
        mRepeat = repeat;
        mRepeatNumber = repeatNumber;
        mRepeatType = repeatType;
        mSilent = silent;
    }

    public Reminder(int id, String title, String date, String time, boolean repeat, String repeatNumber,
                    String repeatType, boolean silent) {
        this.mId = id;
        this.mTitle = title;
        this.mDate = date;
        this.mTime = time;
        this.mRepeat = repeat;
        this.mRepeatNumber = repeatNumber;
        this.mRepeatType = repeatType;
        this.mSilent = silent;
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

    public String getRepeatNumber() {
        return mRepeatNumber;
    }

    public void setRepeatNumber(String repeatNumber) {
        this.mRepeatNumber = repeatNumber;
    }

    public String getRepeatType() {
        return mRepeatType;
    }

    public void setRepeatType(String repeatType) {
        this.mRepeatType = repeatType;
    }

    public boolean getSilent() {
        return mSilent;
    }

    public void setSilent(boolean silent) {
        this.mSilent = silent;
    }

}
