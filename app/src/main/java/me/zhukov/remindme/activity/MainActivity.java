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

public class MainActivity extends AppCompatActivity {

    public static final int INSERT_REMINDER_REQUEST = 1;
    public static final int UPDATE_REMINDER_REQUEST = 2;
    public static final String UPDATE_REMINDER_INTENT = "updateReminder";
    public static final String INSERT_REMINDER_INTENT = "insertReminder";

    private TextView mTvNoReminder;

    private ReminderMapper mReminderMapper;
    private ReminderListAdapter mAdapter;
    private List<Reminder> mReminders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        FloatingActionButton fabAddReminder = (FloatingActionButton) findViewById(R.id.fab_add_reminder);
        RecyclerView rvReminderList = (RecyclerView) findViewById(R.id.rv_reminder_list);
        mTvNoReminder = (TextView) findViewById(R.id.tv_no_reminder);

        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        mReminderMapper = new ReminderMapper(this);
        mReminders = mReminderMapper.getAllReminders();

        mAdapter = new ReminderListAdapter(MainActivity.this, mReminders, mTvNoReminder);
        rvReminderList.setAdapter(mAdapter);
        rvReminderList.setLayoutManager(getLayoutManager());

        fabAddReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddOrEditReminderActivity.class);
                startActivityForResult(intent, INSERT_REMINDER_REQUEST);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mReminders = mReminderMapper.getAllReminders();
        if (mReminders.isEmpty()) {
            mTvNoReminder.setVisibility(View.VISIBLE);
        } else {
            mTvNoReminder.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case INSERT_REMINDER_REQUEST:
                if (resultCode == RESULT_OK) {
                    Reminder reminder = (Reminder) data.getSerializableExtra(INSERT_REMINDER_INTENT);
                    mAdapter.addItem(reminder);
                }
                break;
            case UPDATE_REMINDER_REQUEST:
                if (resultCode == RESULT_OK) {
                    Reminder reminder = (Reminder) data.getSerializableExtra(UPDATE_REMINDER_INTENT);

                }
                break;
        }
    }

    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
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
