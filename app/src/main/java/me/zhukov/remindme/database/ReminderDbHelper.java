package me.zhukov.remindme.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ReminderDbHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "remindMeTable";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_REPEAT = "repeat";
    public static final String COLUMN_REPEAT_NUM = "repeat_number";
    public static final String COLUMN_REPEAT_TYPE = "repeat_type";
    public static final String COLUMN_SILENT = "activity";

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "RemindMe.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ", ";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY, " +
                    COLUMN_TITLE + TEXT_TYPE + COMMA_SEP +
                    COLUMN_DATE + TEXT_TYPE + COMMA_SEP +
                    COLUMN_TIME + TEXT_TYPE + COMMA_SEP +
                    COLUMN_REPEAT + INTEGER_TYPE + COMMA_SEP +
                    COLUMN_REPEAT_NUM + TEXT_TYPE + COMMA_SEP +
                    COLUMN_REPEAT_TYPE + TEXT_TYPE + COMMA_SEP +
                    COLUMN_SILENT + INTEGER_TYPE + ");";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public ReminderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
