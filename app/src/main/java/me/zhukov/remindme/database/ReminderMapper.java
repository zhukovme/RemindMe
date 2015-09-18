package me.zhukov.remindme.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import me.zhukov.remindme.model.Reminder;
import me.zhukov.remindme.model.RepeatType;

import static me.zhukov.remindme.database.ReminderDbHelper.COLUMN_DATE;
import static me.zhukov.remindme.database.ReminderDbHelper.COLUMN_ID;
import static me.zhukov.remindme.database.ReminderDbHelper.COLUMN_REPEAT;
import static me.zhukov.remindme.database.ReminderDbHelper.COLUMN_REPEAT_NUM;
import static me.zhukov.remindme.database.ReminderDbHelper.COLUMN_REPEAT_TYPE;
import static me.zhukov.remindme.database.ReminderDbHelper.COLUMN_SILENT;
import static me.zhukov.remindme.database.ReminderDbHelper.COLUMN_TIME;
import static me.zhukov.remindme.database.ReminderDbHelper.COLUMN_TITLE;
import static me.zhukov.remindme.database.ReminderDbHelper.TABLE_NAME;

public class ReminderMapper {

    private ReminderDbHelper mDbHelper;
    private SQLiteDatabase mDatabase;

    public ReminderMapper(Context context) {
        mDbHelper = new ReminderDbHelper(context);
    }

    public long insertReminder(Reminder reminder) {
        mDatabase = mDbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TITLE, reminder.getTitle());
        contentValues.put(COLUMN_DATE, reminder.getDate());
        contentValues.put(COLUMN_TIME, reminder.getTime());
        contentValues.put(COLUMN_REPEAT, reminder.getRepeat() ? 1 : 0);
        contentValues.put(COLUMN_REPEAT_NUM, reminder.getRepeatNumber());
        contentValues.put(COLUMN_REPEAT_TYPE, reminder.getRepeatType().toString());
        contentValues.put(COLUMN_SILENT, reminder.getSilent() ? 1 : 0);

        long rowId = mDatabase.insert(TABLE_NAME, null, contentValues);
        reminder.setId((int)rowId);
        mDatabase.close();
        mDbHelper.close();
        return rowId;
    }

    public boolean updateReminder(Reminder reminder) {
        mDatabase = mDbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TITLE, reminder.getTitle());
        contentValues.put(COLUMN_DATE, reminder.getDate());
        contentValues.put(COLUMN_TIME, reminder.getTime());
        contentValues.put(COLUMN_REPEAT, reminder.getRepeat() ? 1 : 0);
        contentValues.put(COLUMN_REPEAT_NUM, reminder.getRepeatNumber());
        contentValues.put(COLUMN_REPEAT_TYPE, reminder.getRepeatType().toString());
        contentValues.put(COLUMN_SILENT, reminder.getSilent() ? 1 : 0);

        int rows = mDatabase.update(
                TABLE_NAME,
                contentValues,
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(reminder.getId())}
        );
        mDatabase.close();
        mDbHelper.close();
        return rows > 0;
    }

    public List<Reminder> getAllReminders() {
        mDatabase = mDbHelper.getReadableDatabase();
        List<Reminder> reminders = new ArrayList<>();
        Cursor cursor = mDatabase.query(TABLE_NAME, null, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int idColIndex = cursor.getColumnIndex(COLUMN_ID);
            int titleColIndex = cursor.getColumnIndex(COLUMN_TITLE);
            int dateColIndex = cursor.getColumnIndex(COLUMN_DATE);
            int timeColIndex = cursor.getColumnIndex(COLUMN_TIME);
            int repeatColIndex = cursor.getColumnIndex(COLUMN_REPEAT);
            int repeatNumColIndex = cursor.getColumnIndex(COLUMN_REPEAT_NUM);
            int repeatTypeColIndex = cursor.getColumnIndex(COLUMN_REPEAT_TYPE);
            int silentColIndex = cursor.getColumnIndex(COLUMN_SILENT);
            do {
                Reminder reminder = new Reminder();
                reminder.setId(cursor.getInt(idColIndex));
                reminder.setTitle(cursor.getString(titleColIndex));
                reminder.setDate(cursor.getString(dateColIndex));
                reminder.setTime(cursor.getString(timeColIndex));
                reminder.setRepeat(cursor.getInt(repeatColIndex) == 1);
                reminder.setRepeatNumber(cursor.getInt(repeatNumColIndex));
                reminder.setRepeatType(
                        RepeatType.valueOf(cursor.getString(repeatTypeColIndex).toUpperCase())
                );
                reminder.setSilent(cursor.getInt(silentColIndex) == 1);

                reminders.add(reminder);
            } while (cursor.moveToNext());
        }
        cursor.close();
        mDatabase.close();
        mDbHelper.close();
        return reminders;
    }

    public boolean deleteReminder(Reminder reminder) {
        mDatabase = mDbHelper.getWritableDatabase();
        int rows = mDatabase.delete(
                TABLE_NAME, COLUMN_ID + " = ?",
                new String[]{String.valueOf(reminder.getId())}
        );
        mDatabase.close();
        mDbHelper.close();
        return rows > 0;
    }
}
