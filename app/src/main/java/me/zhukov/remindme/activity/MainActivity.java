package me.zhukov.remindme.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import me.zhukov.remindme.R;
import me.zhukov.remindme.adapter.ReminderListAdapter;
import me.zhukov.remindme.database.ReminderMapper;
import me.zhukov.remindme.model.Reminder;
import me.zhukov.remindme.receiver.AlarmReceiver;

public class MainActivity extends AppCompatActivity {

    public static final int ADD_REMINDER_REQUEST = 1;
    public static final int UPDATE_REMINDER_REQUEST = 2;
    public static final String UPDATE_REMINDER_INTENT = "updateReminder";
    public static final String UPDATE_REMINDER_ID_INTENT = "updateReminderId";
    public static final String ADD_REMINDER_INTENT = "addReminder";

    private TextView mTvNoReminder;
    private FloatingActionButton mFabAddReminder;
    private RecyclerView mRvReminderList;

    private ReminderMapper mReminderMapper;
    private ReminderListAdapter mAdapter;
    private List<Reminder> mReminders;
    private AlarmReceiver mAlarmReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mFabAddReminder = (FloatingActionButton) findViewById(R.id.fab_add_reminder);
        mRvReminderList = (RecyclerView) findViewById(R.id.rv_reminder_list);
        mTvNoReminder = (TextView) findViewById(R.id.tv_no_reminder);

        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        mAlarmReceiver = new AlarmReceiver();
        mReminderMapper = new ReminderMapper(this);
        mReminders = mReminderMapper.getAllReminders();

        mAdapter = new ReminderListAdapter(MainActivity.this, mReminders, mTvNoReminder);
        mRvReminderList.setAdapter(mAdapter);
        mRvReminderList.setLayoutManager(getLayoutManager());

        mFabAddReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ReminderActivity.class);
                startActivityForResult(intent, ADD_REMINDER_REQUEST);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mReminders.isEmpty()) {
            mTvNoReminder.setVisibility(View.VISIBLE);
        } else {
            mTvNoReminder.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ADD_REMINDER_REQUEST:
                if (resultCode == RESULT_OK) {
                    Reminder reminder = (Reminder) data.getSerializableExtra(ADD_REMINDER_INTENT);
                    addReminder(reminder);
                }
                break;
            case UPDATE_REMINDER_REQUEST:
                if (resultCode == RESULT_OK) {
                    Reminder reminder = (Reminder) data.getSerializableExtra(UPDATE_REMINDER_INTENT);
                    updateReminder(reminder);
                }
                break;
        }
    }

    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    }

    protected void setAlarm(Reminder reminder) {
        if (reminder.getRepeat()) {
            mAlarmReceiver.setRepeatAlarm(
                    this,
                    reminder.asCalendar(),
                    reminder.getRepeatTimeInMillis(),
                    reminder.getId(),
                    reminder.getTitle()
            );
        } else {
            mAlarmReceiver.setAlarm(
                    this,
                    reminder.asCalendar(),
                    reminder.getId(),
                    reminder.getTitle()
            );
        }
    }

    public void addReminder(Reminder reminder) {
        mReminderMapper.insertReminder(reminder);
        mReminders.add(reminder);
        int position = mReminders.indexOf(reminder);
        mAdapter.notifyItemInserted(position);
        mAdapter.notifyDataSetChanged();
        setAlarm(reminder);
    }

    public void updateReminder(Reminder reminder) {
        mReminderMapper.updateReminder(reminder);
        int position = getIntent().getIntExtra(UPDATE_REMINDER_ID_INTENT, -1);
        mReminders.set(position, reminder);
        mAdapter.notifyItemChanged(position);
        mAdapter.notifyDataSetChanged();
        setAlarm(reminder);
    }

    public void deleteReminder(Reminder reminder) {
        mReminderMapper.deleteReminder(reminder);
        int position = mReminders.indexOf(reminder);
        mReminders.remove(position);
        mAdapter.notifyItemRemoved(position);
        mAdapter.notifyItemRangeChanged(position, mReminders.size());
        mAlarmReceiver.cancelAlarm(this, reminder.getId());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:

                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
